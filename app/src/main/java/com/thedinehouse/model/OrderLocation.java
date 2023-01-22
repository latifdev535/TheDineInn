package com.thedinehouse.model;

public class OrderLocation {
    private Integer id;
    private String name;
    private String status;
    private String type;
    private String createdOn;

    public OrderLocation(){}

    public OrderLocation(Integer id, String name, String status, String type, String createdOn) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.type = type;
        this.createdOn = createdOn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
