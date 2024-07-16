package com.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.model.ZonalData;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

@Service
public class ZonalService {

    private static final String REDIS_HASH_KEY = "ZoneRegionBranchData";

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOperations;
    private final ObjectMapper objectMapper;

    public ZonalService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.objectMapper = objectMapper;
    }

    public boolean storeZonalData(List<ZonalData> zonalDataList) {
        for (ZonalData zonalData : zonalDataList) {
            try {
                String jsonData = objectMapper.writeValueAsString(zonalData);
                hashOperations.put(REDIS_HASH_KEY, generateHashKey(zonalData), jsonData);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public List<ZonalData> getZonalData(String zonalBrcode) {
        List<ZonalData> zonalDataList = new ArrayList<>();
        Set<String> uniqueKeys = new HashSet<>(); 
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            if (key.startsWith(zonalBrcode)) {
                String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
                try {
                    ZonalData zonalData = objectMapper.readValue(jsonData, ZonalData.class);
                    if (zonalData != null && zonalData.getData() != null && uniqueKeys.add(key)) {
                        zonalDataList.add(zonalData);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return zonalDataList;
    }


    
    public List<String> getRegData(String regCode) {
        List<String> regDataList = new ArrayList<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                ZonalData zonalData = objectMapper.readValue(jsonData, ZonalData.class);
                if (zonalData != null && zonalData.getData() != null && regCode.equals(zonalData.getData().getReg_code())) {
                    String regData = "Branchcode: " + zonalData.getData().getC_branchcode() +
                            ", Branch_name: " + zonalData.getData().getBr_name() +
                            ", Region_name: " + zonalData.getData().getReg_name() +
                            ", Region_code: " + zonalData.getData().getReg_code();
                    regDataList.add(regData);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return regDataList;
    }
    
    public List<String> getCBranchcodeData(String cBranchcode) {
        List<String> cBranchcodeDataList = new ArrayList<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                ZonalData zonalData = objectMapper.readValue(jsonData, ZonalData.class);
                if (cBranchcode.equals(zonalData.getData().getC_branchcode())) {
                    String branchData = "Branchcode: " + zonalData.getData().getC_branchcode() +
                            ", Branch_name: " + zonalData.getData().getBr_name() +
                            ", Region_name: " + zonalData.getData().getReg_name() +
                            ", Zonal_name: " + zonalData.getData().getZonal_name();
                    cBranchcodeDataList.add(branchData);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return cBranchcodeDataList;
    }

    public List<ZonalData> getAllData() {
        List<ZonalData> allDataList = new ArrayList<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                ZonalData zonalData = objectMapper.readValue(jsonData, ZonalData.class);
                allDataList.add(zonalData);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return allDataList;
    }

    public boolean deleteAll() {
        redisTemplate.delete(REDIS_HASH_KEY);
        return true;
    }

    public boolean deleteDataByZonalBrcode(String zonalBrcode) {
        hashOperations.delete(REDIS_HASH_KEY, zonalBrcode);
        return true;
    }

    public List<String> getAllZonalNames() {
        List<String> zonalNamesList = new ArrayList<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                ZonalData zonalData = objectMapper.readValue(jsonData, ZonalData.class);
                String zonalName = zonalData.getData().getZonal_name();
                if (!zonalNamesList.contains(zonalName)) {
                    zonalNamesList.add(zonalName);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return zonalNamesList;
    }


    public List<Map<String, String>> getAllZonalInfo() {
        List<Map<String, String>> zonalInfoList = new ArrayList<>();
        Set<String> uniqueZonalNames = new HashSet<>(); // Use a Set to store unique zonal names
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                ZonalData zonalData = objectMapper.readValue(jsonData, ZonalData.class);
                String zonalName = zonalData.getData().getZonal_name();
                if (!uniqueZonalNames.contains(zonalName)) { // Check if zonal name is already encountered
                    Map<String, String> zonalInfo = new HashMap<>();
                    zonalInfo.put("zonal_name", zonalName);
                    zonalInfo.put("zonal_brcode", zonalData.getZonal_brcode());
                    zonalInfoList.add(zonalInfo);
                    uniqueZonalNames.add(zonalName); // Add zonal name to the set to mark it as encountered
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return zonalInfoList;
    }
    
  
	 public List<Map<String, String>> getAllBranch() {
	     List<Map<String, String>> branchList = new ArrayList<>();
	     Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
	     for (String key : keys) {
	         String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
	         try {
	             ZonalData zonalData = objectMapper.readValue(jsonData, ZonalData.class);
	             Map<String, String> branchInfo = new HashMap<>();
	             branchInfo.put("br_name", zonalData.getData().getBr_name());
	             branchInfo.put("c_branchcode", zonalData.getData().getC_branchcode());
	             branchList.add(branchInfo);
	         } catch (JsonProcessingException e) {
	             e.printStackTrace();
	         }
	     }
	     return branchList;
	 }

	 public List<Map<String, String>> getRegDataForZonal(String zonalBrcode) {
		    List<Map<String, String>> regDataList = new ArrayList<>();
		    Set<String> uniqueRegCodes = new HashSet<>(); // Store unique reg codes to avoid duplicates
		    Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
		    for (String key : keys) {
		        String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
		        try {
		            ZonalData zonalData = objectMapper.readValue(jsonData, ZonalData.class);
		            if (zonalBrcode.equals(zonalData.getZonal_brcode()) && uniqueRegCodes.add(zonalData.getData().getReg_code())) {
		                Map<String, String> regInfo = new HashMap<>();
		                regInfo.put("reg_name", zonalData.getData().getReg_name());
		                regInfo.put("reg_code", zonalData.getData().getReg_code());
		                regDataList.add(regInfo);
		            }
		        } catch (JsonProcessingException e) {
		            e.printStackTrace();
		        }
		    }
		    return regDataList;
		}
	 
	public List<Map<String, String>> getRegBrData(String regCode) {
	    List<Map<String, String>> regBrDataList = new ArrayList<>();
	    Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
	    for (String key : keys) {
	        String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
	        try {
	            ZonalData zonalData = objectMapper.readValue(jsonData, ZonalData.class);
	            if (regCode.equals(zonalData.getData().getReg_code())) {
	                Map<String, String> regBrData = new HashMap<>();
	                regBrData.put("Br_name", zonalData.getData().getBr_name());
	                regBrData.put("c_branchcode", zonalData.getData().getC_branchcode());
	                regBrDataList.add(regBrData);
	            }
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
	    }
	    return regBrDataList;
	}

	public List<Map<String, String>> getAllRegion() {
	    Set<String> uniqueRegions = new HashSet<>();
	    List<Map<String, String>> allRegionList = new ArrayList<>();
	    Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
	    for (String key : keys) {
	        String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
	        try {
	            ZonalData zonalData = objectMapper.readValue(jsonData, ZonalData.class);
	            String regCode = zonalData.getData().getReg_code();
	            // Check if the region code is unique, if yes, add it to the result list
	            if (!uniqueRegions.contains(regCode)) {
	                Map<String, String> regionInfo = new HashMap<>();
	                regionInfo.put("reg_name", zonalData.getData().getReg_name());
	                regionInfo.put("reg_code", regCode);
	                allRegionList.add(regionInfo);
	                uniqueRegions.add(regCode); // Add the region code to the set
	            }
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
	    }
	    return allRegionList;
	}



    private String generateHashKey(ZonalData zonalData) {
        return zonalData.getZonal_brcode() + ":" + zonalData.getData().getC_branchcode();
    }
}
