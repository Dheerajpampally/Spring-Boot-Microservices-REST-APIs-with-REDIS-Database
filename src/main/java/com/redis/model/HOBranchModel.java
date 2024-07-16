package com.redis.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HOBranchModel {
    private String stateCode;
    @JsonProperty("data")
    private Data data;

    @lombok.Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {
    	
    	 @JsonProperty("Retail_Branch")
         private String Retail_Branch;

    	
        @JsonProperty("branchCode")
        private String branchCode;

        @JsonProperty("branchName")
        private String branchName;

        @JsonProperty("address1")
        private String address1;

        @JsonProperty("address2")
        private String address2;

        @JsonProperty("address3")
        private String address3;

        @JsonProperty("phone")
        private String phone;

        @JsonProperty("email")
        private String email;

        @JsonProperty("branchHead")
        private String branchHead;

        @JsonProperty("bhMobNo")
        private String bhMobNo;

        @JsonProperty("latitude")
        private String latitude;

        @JsonProperty("longitude")
        private String longitude;

        @JsonProperty("pincode")
        private String pincode;

        @JsonProperty("mobile")
        private String mobile;

        @JsonProperty("operationalDate")
        private String operationalDate;

        @JsonProperty("inaugrationalDate")
        private String inaugrationalDate;

        @JsonProperty("district")
        private String district;

        @JsonProperty("stateName")
        private String stateName;
    }

    public static List<StateModel> fromString(String stateModelString) {
        // Implement JSON deserialization logic here
        return null;		
    }
}
