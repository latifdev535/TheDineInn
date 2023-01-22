package com.thedinehouse;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thedinehouse.api.DineHouseApiInterface;
import com.thedinehouse.model.Item;
import com.thedinehouse.model.OrderRequest;
import com.thedinehouse.model.OrderResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Cart extends AppCompatActivity implements View.OnClickListener, Callback<OrderResponse> {

    private TableLayout tl_cartItems;
    private Spinner tableNumber, tableServedBy;
    private TextView tv_total;
    private Button btn_cartSubmit;
    private ProgressBar pbLoading;
    private List<String> tableNameList,serverNameList;
    private String ORDER_ID,TABLE_NAME,TABLE_SERVER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        tl_cartItems = findViewById(R.id.tl_cartItems);
        tableNumber = findViewById(R.id.tableNumber);
        tableServedBy = findViewById(R.id.tableServer);
        tv_total = findViewById(R.id.tv_total);
        btn_cartSubmit = findViewById(R.id.btn_cartSubmit);
        pbLoading = findViewById(R.id.pbLoading);
        btn_cartSubmit.setOnClickListener(this);
        tl_cartItems.setStretchAllColumns(true);
        loadCartData(getIntent().getExtras());
        loadTablesAndServer();


    }

    private void loadCartData(Bundle bundle) {

        TABLE_NAME = bundle.getString(TheDineHouseConstants.TABLE_NAME);
        TABLE_SERVER = bundle.getString(TheDineHouseConstants.TABLE_SERVER);
        ORDER_ID = bundle.getString(TheDineHouseConstants.ORDER_ID);

        ArrayList<Item> cartItems = (ArrayList<Item>) bundle.getSerializable(TheDineHouseConstants.CART);
        int amount = 0;
        TableRow trHead = new TableRow(this);

        TextView itemNameHead = new TextView(this);
        itemNameHead.setTextSize(18);
        itemNameHead.setText("Name");
        itemNameHead.setTypeface(null, Typeface.BOLD);
        itemNameHead.setTextColor(Color.BLACK);
        trHead.addView(itemNameHead);
        TextView itemNameQty = new TextView(this);
        itemNameQty.setTypeface(null, Typeface.BOLD);
        itemNameQty.setTextSize(18);
        itemNameQty.setText("Count");
        itemNameQty.setTextColor(Color.BLACK);
        trHead.addView(itemNameQty);
        TextView itemPriceHead = new TextView(this);
        itemPriceHead.setTypeface(null, Typeface.BOLD);
        itemPriceHead.setTextSize(18);
        itemPriceHead.setText("Price");
        itemPriceHead.setTextColor(Color.BLACK);
        trHead.addView(itemPriceHead);
        tl_cartItems.addView(trHead);

        for (Item eachItem : cartItems) {

            TableRow tr = new TableRow(this);

            TextView itemName = new TextView(this);
            itemName.setTextSize(18);
            itemName.setText(eachItem.getName());
            itemName.setTextColor(Color.BLACK);
            tr.addView(itemName);

            TextView itemCount = new TextView(this);
            itemCount.setTextSize(18);
            itemCount.setText(String.valueOf(eachItem.getQuantity()));
            itemCount.setTextColor(Color.BLACK);
            tr.addView(itemCount);

            TextView itemPrice = new TextView(this);
            itemPrice.setTextSize(18);
            itemPrice.setText(String.valueOf((eachItem.getPrice() * eachItem.getQuantity())));
            itemPrice.setTextColor(Color.BLACK);
            tr.addView(itemPrice);
            amount = amount + (eachItem.getPrice() * eachItem.getQuantity());

            tl_cartItems.addView(tr);
        }
        tv_total.setText(String.valueOf(amount));

    }

    private void loadTablesAndServer() {

        tableNameList = TheDineHouseHelper.getTables(this);
        tableNumber.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                tableNameList));

        serverNameList = TheDineHouseHelper.getServers(this);
        tableServedBy.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                serverNameList));

        if (TABLE_NAME != null && (!TABLE_NAME.isEmpty())) {
            int index = tableNameList.indexOf(TABLE_NAME);
            if (index>=0) {
                tableNumber.setSelection(index);
                tableNumber.setEnabled(false);
            }
        }

        if (TABLE_SERVER != null && (!TABLE_SERVER.isEmpty())) {
            int index = serverNameList.indexOf(TABLE_SERVER);
            if (index>=0) {
                tableServedBy.setSelection(index);
                tableServedBy.setEnabled(false);
            }
        }

    }

    @Override
    public void onClick(View view) {

        if (validate()){
            TheDineHouseHelper.showLoading(pbLoading,btn_cartSubmit);
            Map<Integer, Item> cartItems = TheDineHouseConstants.getCartItems();

            Collection<Item> items = cartItems.values();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TheDineHouseHelper.getBaseURL(this))
                    .addConverterFactory(GsonConverterFactory.create()).build();
            DineHouseApiInterface apiCalls = retrofit.create(DineHouseApiInterface.class);

            String token = TheDineHouseHelper.getSharesPref(this, TheDineHouseConstants.ServerSetting.TOKEN);
            String userId = TheDineHouseHelper.getSharesPref(this, TheDineHouseConstants.ServerSetting.USER_ID);
            String address = tableNameList.get(tableNumber.getSelectedItemPosition());
            String server = serverNameList.get(tableServedBy.getSelectedItemPosition());
            Call<OrderResponse> orderResponseCall;

            if (ORDER_ID == null) {
                orderResponseCall = apiCalls.orderAPI(new OrderRequest( userId, ORDER_ID, address, server,new ArrayList<>(items)));
            } else {
                orderResponseCall = apiCalls.updateOrderAPI(new OrderRequest( userId, ORDER_ID, address, server,new ArrayList<>(items)));
            }
            orderResponseCall.enqueue(this);

        }
    }

    private boolean validate() {
        boolean result = true;
        if (tableNumber.getSelectedItemPosition() == 0) {
            TheDineHouseHelper.toast(Cart.this,"Please select Table Number");
            result = false;
        } else if (tableServedBy.getSelectedItemPosition() == 0) {
            TheDineHouseHelper.toast(Cart.this,"Please select server person");
            result = false;
        } else if (TheDineHouseConstants.getCartItems().isEmpty()) {
            TheDineHouseHelper.toast(Cart.this,"Invalid cart");
            result = false;
        }
        return result;
    }

    @Override
    public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
        TheDineHouseHelper.stopLoading(pbLoading,btn_cartSubmit);
        if (Objects.nonNull(response.body()) && response.body().getStatus().equalsIgnoreCase(TheDineHouseConstants.SUCCESS)) {
            TheDineHouseHelper.toast(Cart.this, "Order sent..!");
            TheDineHouseConstants.emptyCart();
            startActivity(new Intent(Cart.this, MainActivity.class));
        } else {
            TheDineHouseHelper.toast(Cart.this, "Order failed, please contact admin..!");
        }
    }

    @Override
    public void onFailure(Call<OrderResponse> call, Throwable t) {
        TheDineHouseHelper.stopLoading(pbLoading,btn_cartSubmit);
        TheDineHouseHelper.toast(Cart.this,"Order Failed..!".concat(TheDineHouseHelper.getErrorMessage(t)));

    }
}