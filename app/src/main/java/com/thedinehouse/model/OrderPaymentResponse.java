package com.thedinehouse.model;

public class OrderPaymentResponse {

    private String status;
    private OrderPaymentResData data;

    public String getStatus() {
        return status;
    }

    public OrderPaymentResData getData() {
        return data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(OrderPaymentResData data) {
        this.data = data;
    }
}
