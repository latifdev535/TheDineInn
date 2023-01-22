package com.thedinehouse.model;

public class BaseInfoRequest {
    public BaseInfoRequest(String token) {
        this.token = token;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
