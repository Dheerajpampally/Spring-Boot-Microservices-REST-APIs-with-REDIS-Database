package com.redis.model;

import lombok.Data;

@lombok.Data
public class DataModel {
    private String code;
    private Object data;
   
    public DataModel(String code, Object data) {
        this.code = code;
        this.data = data;
    }
}
