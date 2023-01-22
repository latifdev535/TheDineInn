package com.thedinehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.thedinehouse.api.DineHouseApiInterface;
import com.thedinehouse.model.BillResponse;
import com.thedinehouse.model.OrderPaymentRequest;
import com.thedinehouse.model.OrderPaymentResponse;
import com.thedinehouse.model.OrderResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Payment extends AppCompatActivity implements View.OnClickListener, Callback<OrderPaymentResponse> {

    private Spinner spnPaymentType;
    private TextView tvTotalAmount,tvOrderId,tvTableNumber;
    private EditText amount,comment;
    private Button btnSubmitAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        spnPaymentType = findViewById(R.id.spnPayment);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvTableNumber = findViewById(R.id.tvTableNumber);
        amount = findViewById(R.id.amount);
        comment = findViewById(R.id.comment);
        btnSubmitAmount = findViewById(R.id.btnSubmitAmount);
        btnSubmitAmount.setOnClickListener(this);
        spnPaymentType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, TheDineHouseHelper.getPayments(this)));
        loadPaymentData(getIntent().getExtras());
    }



    private void loadPaymentData(Bundle extras) {

        OrderResponse orderResponse = (OrderResponse) extras.getSerializable(TheDineHouseConstants.ORDER_ID);
        tvTotalAmount.setText(orderResponse.getPrice());
        tvOrderId.setText(orderResponse.getId());
        tvTableNumber.setText(orderResponse.getAddress());


    }

    @Override
    public void onClick(View view) {
        String comment = this.comment.getText().toString().trim();
        if (validate(comment)) {
            if (spnPaymentType.getSelectedItemPosition() != 0) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(TheDineHouseHelper.getBaseURL(Payment.this))
                        .addConverterFactory(GsonConverterFactory.create()).build();
                DineHouseApiInterface apiCalls = retrofit.create(DineHouseApiInterface.class);
                OrderPaymentRequest orderPaymentRequest = new OrderPaymentRequest();
                orderPaymentRequest.setOrderId(tvOrderId.getText().toString());
                orderPaymentRequest.setUserId(TheDineHouseHelper.getSharesPref(this, TheDineHouseConstants.ServerSetting.USER_ID));
                orderPaymentRequest.setAmount(amount.getText().toString());
                orderPaymentRequest.setMethod(spnPaymentType.getSelectedItem().toString());
                orderPaymentRequest.setDescription(comment);
                Call<OrderPaymentResponse> orderPaymentResponseCall = apiCalls.orderPayment(orderPaymentRequest);
                orderPaymentResponseCall.enqueue(this);
            } else {
                TheDineHouseHelper.toast(this, "Please select payment method.");
            }
        } else {
            TheDineHouseHelper.toast(this, "Please enter explanation for difference amount.");
        }
    }

    private boolean validate(String comment) {
        Double actualAmount = Double.valueOf(tvTotalAmount.getText().toString().trim());
        Double paidAmount;
        if (amount.getText().toString().trim().length()==0){
            paidAmount = 0.00;
        } else {
            paidAmount = Double.valueOf(amount.getText().toString().trim());
        }

        return actualAmount.equals(paidAmount) || (!comment.isEmpty());
    }

    @Override
    public void onResponse(Call<OrderPaymentResponse> call, Response<OrderPaymentResponse> response) {

        OrderPaymentResponse body = response.body();
        if (body.getStatus().equalsIgnoreCase(TheDineHouseConstants.SUCCESS)) {
            TheDineHouseHelper.toast(this,"Successfully sent payment");
            startActivity(new Intent(this,OrdersList.class));
        } else {
            TheDineHouseHelper.toast(this,"Failed to send payment, please contact admin");
        }

    }

    @Override
    public void onFailure(Call<OrderPaymentResponse> call, Throwable t) {
        TheDineHouseHelper.toast(this,"Error submitting payment ".concat(TheDineHouseHelper.getErrorMessage(t)));
    }
}