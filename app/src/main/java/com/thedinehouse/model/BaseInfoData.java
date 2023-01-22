package com.thedinehouse.model;

import java.util.List;

public class BaseInfoData {

    private List<OrderLocation> locations;
    private List<Item> items;
    private List<ItemCategory> categories;
    private List<String> paymentMethods;
    private List<String> tranGroups;
    private List<String> servers;

    public List<String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<String> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public List<String> getTranGroups() {
        return tranGroups;
    }

    public void setTranGroups(List<String> tranGroups) {
        this.tranGroups = tranGroups;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setCategories(List<ItemCategory> categories) {
        this.categories = categories;
    }

    public List<ItemCategory> getCategories() {
        return categories;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<OrderLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<OrderLocation> locations) {
        this.locations = locations;
    }

}
