package com.redis.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PincodeData {
    private String state_code;

    @JsonProperty("data")
    private Data data;

    @lombok.Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {
        @JsonProperty("state_name")
        private String state_name;

        @JsonProperty("district_code")
        private String district_code;

        @JsonProperty("district_name")
        private String district_name;

        @JsonProperty("pincode")
        private String pincode;

        @JsonProperty("place")
        private String place;
    }

     public static List<PincodeData> fromString(String PincodeDataString) {
     return null;
     }
}
