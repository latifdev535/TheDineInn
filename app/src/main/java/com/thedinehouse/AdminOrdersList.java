package com.thedinehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thedinehouse.adapters.OrderItemAdapter;
import com.thedinehouse.api.DineHouseApiInterface;
import com.thedinehouse.model.OrderResponse;
import com.thedinehouse.model.OrderResponseInfo;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminOrdersList extends AppCompatActivity implements Callback<OrderResponseInfo>, AdapterView.OnItemClickListener {

    private ListView lv_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_orders_list);
        lv_orders = findViewById(R.id.lv_orders);
        loadOrders();
    }

    private void loadOrders() {

//        List<OrderResponse> responseList = TheDineHouseConstants.getOrderResponse();
//
//            OrderItemAdapter orderItemAdapter = new OrderItemAdapter(this, responseList);
//            lv_orders.setAdapter(orderItemAdapter);
//            lv_orders.setOnItemClickListener(this);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheDineHouseHelper.getBaseURL(this))
                .addConverterFactory(GsonConverterFactory.create()).build();
        DineHouseApiInterface apiCalls = retrofit.create(DineHouseApiInterface.class);
        Call<OrderResponseInfo> pastOrderApi = apiCalls.getAllOrderAPI();
        pastOrderApi.enqueue(this);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        OrderResponse selectedOrder = (OrderResponse) adapterView.getItemAtPosition(i);

        Intent intent = new Intent(AdminOrdersList.this, Payment.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TheDineHouseConstants.ORDER_ID,selectedOrder);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}