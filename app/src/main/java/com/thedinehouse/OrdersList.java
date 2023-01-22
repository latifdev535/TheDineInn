package com.thedinehouse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thedinehouse.adapters.OrderItemAdapter;
import com.thedinehouse.api.DineHouseApiInterface;
import com.thedinehouse.model.BillResponse;
import com.thedinehouse.model.Item;
import com.thedinehouse.model.OrderResponse;
import com.thedinehouse.model.OrderResponseInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrdersList extends AppCompatActivity implements AdapterView.OnItemClickListener, Callback<OrderResponseInfo> {

    private ListView lv_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        lv_orders = findViewById(R.id.lv_orders);

        if (TheDineHouseConstants.TEST) {
//        Test
            OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this, TheDineHouseConstants.getOrderResponse());
            lv_orders.setAdapter(orderItemAdapter);
            lv_orders.setOnItemClickListener(this);
//        Test

        } else {
            loadOrders();
        }


    }

    private void loadOrders() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheDineHouseHelper.getBaseURL(this))
                .addConverterFactory(GsonConverterFactory.create()).build();
        DineHouseApiInterface apiCalls = retrofit.create(DineHouseApiInterface.class);
        Call<OrderResponseInfo> pastOrderApi;
        if (TheDineHouseHelper.getSharesPref(this, TheDineHouseConstants.ServerSetting.ADMIN)
                .equalsIgnoreCase(TheDineHouseConstants.ServerSetting.ADMIN.name())) {
            pastOrderApi = apiCalls.getAllOrderAPI();
        } else {
            pastOrderApi = apiCalls.getOrderAPI(TheDineHouseHelper.getSharesPref(this, TheDineHouseConstants.ServerSetting.USER_ID));
        }
        pastOrderApi.enqueue(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        OrderResponse selectedOrder = (OrderResponse) adapterView.getItemAtPosition(i);
        if (selectedOrder.getStatus().equalsIgnoreCase(TheDineHouseConstants.ORDER_STATUS_PAID)) return;
        System.out.println("Order Id :: "+selectedOrder);
        List<Item> items = selectedOrder.getOrderItems();

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Orders")
                .setMessage("What do you want?")
                .setPositiveButton("Bill", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(TheDineHouseHelper.getBaseURL(OrdersList.this))
                                .addConverterFactory(GsonConverterFactory.create()).build();
                        DineHouseApiInterface apiCalls = retrofit.create(DineHouseApiInterface.class);
                        Call<BillResponse> pastOrderApi = apiCalls.getBillAPI(selectedOrder.getId());
                        pastOrderApi.enqueue(new Callback<BillResponse>() {
                            @Override
                            public void onResponse(Call<BillResponse> call, Response<BillResponse> response) {

                                BillResponse billResponse = response.body();
                                if (billResponse.getStatus().equalsIgnoreCase(TheDineHouseConstants.SUCCESS)) {
                                    TheDineHouseHelper.toast(OrdersList.this, "Bill generated successfully");
                                    Intent intent = new Intent(OrdersList.this, MainActivity.class);
                                    TheDineHouseConstants.emptyCart();
                                    dialog.cancel();
                                    startActivity(intent);
                                } else {
                                    TheDineHouseHelper.toast(OrdersList.this, "Issue with  generated bill");
                                }

                            }

                            @Override
                            public void onFailure(Call<BillResponse> call, Throwable t) {
                                TheDineHouseHelper.toast(OrdersList.this, "Error response ".concat(TheDineHouseHelper.getErrorMessage(t)));
                            }
                        });


                        Intent intent = new Intent(OrdersList.this, MainActivity.class);
                        TheDineHouseConstants.emptyCart();
                        dialog.cancel();
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        TheDineHouseConstants.emptyCart();
                        Intent intent = new Intent(OrdersList.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(TheDineHouseConstants.CART, new ArrayList<>(items));
                        bundle.putString(TheDineHouseConstants.ORDER_ID, selectedOrder.getId());
                        bundle.putString(TheDineHouseConstants.TABLE_SERVER, selectedOrder.getServedBy());
                        bundle.putString(TheDineHouseConstants.TABLE_NAME, selectedOrder.getAddress());
                        intent.putExtras(bundle);
                        dialog.cancel();
                        startActivity(intent);

                    }
                });
        if (TheDineHouseHelper.getSharesPref(this, TheDineHouseConstants.ServerSetting.ADMIN)
                .equalsIgnoreCase(TheDineHouseConstants.ServerSetting.ADMIN.name())) {
            builder.setCancelable(true);
            builder.setNeutralButton("Payment", (dialog, id) -> {
                OrderResponse selectedOrder1 = (OrderResponse) adapterView.getItemAtPosition(i);

                Intent intent = new Intent(OrdersList.this, Payment.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(TheDineHouseConstants.ORDER_ID, selectedOrder1);
                intent.putExtras(bundle);
                dialog.cancel();
                startActivity(intent);


            }).create().show();
        } else {
            builder.setCancelable(false);
            builder.setNeutralButton("Cancel", (dialog, id) -> {
                dialog.cancel();

            }).create().show();
        }

    }

    @Override
    public void onResponse(Call<OrderResponseInfo> call, Response<OrderResponseInfo> response) {
        OrderResponseInfo orderResponseInfo = response.body();

        List<OrderResponse> responseList = orderResponseInfo.getData();
        if (orderResponseInfo.getStatus().equalsIgnoreCase(TheDineHouseConstants.SUCCESS) &&
                responseList!= null) {
            OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this, responseList);
            lv_orders.setAdapter(orderItemAdapter);
            lv_orders.setOnItemClickListener(this);
            TheDineHouseHelper.toast(this,"orders loaded successfully..! ");

        } else {
            TheDineHouseHelper.toast(this,"No orders processed by you..! ");
        }
    }

    @Override
    public void onFailure(Call<OrderResponseInfo> call, Throwable t) {
        TheDineHouseHelper.toast(this,"Failed to load orders..! ".concat(TheDineHouseHelper.getErrorMessage(t)));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }
}