package com.redis.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZonalData {
    private String zonal_brcode;

    @JsonProperty("data")
    private Data data;

    @lombok.Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {
        @JsonProperty("reg_name")
        private String reg_name;

        @JsonProperty("Br_name")
        private String br_name;

        @JsonProperty("c_branchcode")
        private String c_branchcode;

        @JsonProperty("reg_code")
        private String reg_code;

        @JsonProperty("zonal_name")
        private String zonal_name;
    }

    public static List<ZonalData> fromString(String zonalDataString) {
        return null;
    }
}

