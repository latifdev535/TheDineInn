package com.thedinehouse.model;

import java.util.List;

public class OrderRequest {

    private String userId;
    private String address;
    private String id;
    private String servedBy;

    private List<Item> orderItems;

    public OrderRequest(String userId, String id, String address, String servedBy, List<Item> orderItems) {

        this.userId = userId;
        this.id = id;
        this.address = address;
        this.orderItems = orderItems;
        this.servedBy = servedBy;
    }

    public String getServedBy() {
        return servedBy;
    }

    public void setServedBy(String servedBy) {
        this.servedBy = servedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Item> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<Item> orderItems) {
        this.orderItems = orderItems;
    }
}
