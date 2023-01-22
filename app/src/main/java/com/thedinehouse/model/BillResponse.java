package com.thedinehouse.model;

public class BillResponse {
    private String status;
    private boolean data;

    public String getStatus() {
        return status;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
