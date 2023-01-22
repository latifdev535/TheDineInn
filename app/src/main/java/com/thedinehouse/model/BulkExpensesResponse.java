package com.thedinehouse.model;

public class BulkExpensesResponse {

    private String status;
    private BulkExpensesResData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BulkExpensesResData getData() {
        return data;
    }

    public void setData(BulkExpensesResData data) {
        this.data = data;
    }
}
