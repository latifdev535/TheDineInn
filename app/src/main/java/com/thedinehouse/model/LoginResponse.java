package com.thedinehouse.model;

public class LoginResponse {
    private String status;
    private LoginData data;

    public LoginData getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
