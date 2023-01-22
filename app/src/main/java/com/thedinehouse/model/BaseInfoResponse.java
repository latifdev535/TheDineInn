package com.thedinehouse.model;

public class BaseInfoResponse {
    private String status;
    private BaseInfoData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BaseInfoData getData() {
        return data;
    }

    public void setData(BaseInfoData data) {
        this.data = data;
    }
}
