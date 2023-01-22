package com.thedinehouse.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String password;
    private String deviceId;

    public LoginRequest() {
    }

    public LoginRequest(String userId, String password, String deviceId) {
        this.userId = userId;
        this.password = password;
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
