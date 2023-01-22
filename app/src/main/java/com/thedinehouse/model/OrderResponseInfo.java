package com.thedinehouse.model;

import java.util.List;

public class OrderResponseInfo {
    private String status;
    private List<OrderResponse> data;

    public String getStatus() {
        return status;
    }

    public List<OrderResponse> getData() {
        return data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(List<OrderResponse> data) {
        this.data = data;
    }
}
