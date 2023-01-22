package com.thedinehouse.model;

import java.io.Serializable;
import java.util.List;

public class OrderResponse implements Serializable {

    private String price;
    private String status;
    private String id;
    private String address;
    private String servedBy;
    private List<Item> orderItems;

    public OrderResponse(String price, String status, String id, String address, String servedBy, List<Item> orderItems) {
        this.price = price;
        this.status = status;
        this.id = id;
        this.address = address;
        this.servedBy = servedBy;
        this.orderItems = orderItems;
    }

    public String getServedBy() {
        return servedBy;
    }

    public void setServedBy(String servedBy) {
        this.servedBy = servedBy;
    }

    public OrderResponse(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<Item> orderItems) {
        this.orderItems = orderItems;
    }
}
