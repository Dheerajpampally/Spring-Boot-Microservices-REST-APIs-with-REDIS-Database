package com.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.model.HOBranchModel;


import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class HOBranchService {

    private static final String REDIS_HASH_KEY = "HOBRANCHDATA";

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOperations; // Added HashOperations
    private final ObjectMapper objectMapper;

    public HOBranchService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash(); // Initialize HashOperations
        this.objectMapper = objectMapper;
    }
    
    public boolean storeHOBranchData(List<HOBranchModel> HOBranchDataList) {
        try {
            for (HOBranchModel hobrData : HOBranchDataList) {
                String jsonData = objectMapper.writeValueAsString(hobrData);
                hashOperations.put(REDIS_HASH_KEY, generateHashKey(hobrData), jsonData);
            }
            return true; // Return true if all data stored successfully
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false; // Return false if there's an error in JSON processing
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false for any other unexpected errors
        }
    }

    public List<HOBranchModel> getAllData() {
        List<HOBranchModel> allDataList = new ArrayList<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
            	HOBranchModel hobrData = objectMapper.readValue(jsonData, HOBranchModel.class);
                allDataList.add(hobrData);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return allDataList;
    }
    
    public List<Map<String, String>> getAllStateNames() {
        List<Map<String, String>> allStateNames = new ArrayList<>();
        Set<String> uniqueStateNames = new HashSet<>(); // Use a Set to store unique zonal names
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
            	HOBranchModel hobrData = objectMapper.readValue(jsonData, HOBranchModel.class);
                String stateName = hobrData.getData().getStateName();
                if (!uniqueStateNames.contains(stateName)) { // Check if zonal name is already encountered
                    Map<String, String> stateInfo = new HashMap<>();
                    stateInfo.put("state_name", stateName);
                    stateInfo.put("state_code", hobrData.getStateCode());
                    allStateNames.add(stateInfo);
                    uniqueStateNames.add(stateName); // Add zonal name to the set to mark it as encountered
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return allStateNames;
    }
    
    
    public List<Map<String, String>> getBranchNamesByStateCode(String stateCode) {
        List<Map<String, String>> branchNamesList = new ArrayList<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
            	HOBranchModel stateData = objectMapper.readValue(jsonData, HOBranchModel.class);
                if (stateData.getStateCode().trim().equalsIgnoreCase(stateCode.trim())) {
                    Map<String, String> branchInfo = new HashMap<>();
                    branchInfo.put("branch_code", stateData.getData().getBranchCode().trim());
                    branchInfo.put("branch_name", stateData.getData().getBranchName().trim());
                    branchNamesList.add(branchInfo);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return branchNamesList;
    }

    
    public List<String> getBranchNamesByStateName(String stateName) {
        List<String> branchNames = new ArrayList<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
            	HOBranchModel stateData = objectMapper.readValue(jsonData, HOBranchModel.class);
                if (stateData.getData().getStateName().equals(stateName)) {
                    branchNames.add(stateData.getData().getBranchName());
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return branchNames;
    }   
 
    
    public Map<String, String> getBranchDetails(String stateName, String branchName) {
        Map<String, String> branchDetails = new HashMap<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
            	HOBranchModel stateData = objectMapper.readValue(jsonData, HOBranchModel.class);
                if (stateName.equals(stateData.getData().getStateName()) && branchName.equals(stateData.getData().getBranchName())) {
                    branchDetails.put("address1", stateData.getData().getAddress1());
                    branchDetails.put("address2", stateData.getData().getAddress2());
                    branchDetails.put("address3", stateData.getData().getAddress3());
                    branchDetails.put("mobile", stateData.getData().getMobile());
                    branchDetails.put("latitude", stateData.getData().getLatitude());
                    branchDetails.put("longitude", stateData.getData().getLongitude());
                    branchDetails.put("email", stateData.getData().getEmail());
                    branchDetails.put("branchHead", stateData.getData().getBranchHead());
                    branchDetails.put("branch", stateData.getData().getRetail_Branch());
                    branchDetails.put("district", stateData.getData().getDistrict());
                    branchDetails.put("stateName", stateData.getData().getStateName());
                    break; // Stop searching after finding the matching branch
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return branchDetails;
    }
    
    public Map<String, String> getBranchDetailsAll(String stateCode, String branchCode) {
        Map<String, String> branchDetails = new HashMap<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
            	HOBranchModel stateData = objectMapper.readValue(jsonData, HOBranchModel.class);
                if (stateCode.equalsIgnoreCase(stateData.getStateCode().trim()) && branchCode.equalsIgnoreCase(stateData.getData().getBranchCode().trim())) {
                	  branchDetails.put("address1", stateData.getData().getAddress1());
                      branchDetails.put("address2", stateData.getData().getAddress2());
                      branchDetails.put("address3", stateData.getData().getAddress3());
                      branchDetails.put("mobile", stateData.getData().getMobile());
                      branchDetails.put("latitude", stateData.getData().getLatitude());
                      branchDetails.put("longitude", stateData.getData().getLongitude());
                      branchDetails.put("email", stateData.getData().getEmail());
                      branchDetails.put("branchHead", stateData.getData().getBranchHead());
                      branchDetails.put("branch", stateData.getData().getRetail_Branch());
                      branchDetails.put("district", stateData.getData().getDistrict());
                      branchDetails.put("stateName", stateData.getData().getStateName());
                    break; // Stop searching after finding the matching branch
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return branchDetails;
    }

    private String generateHashKey(HOBranchModel hobrData) {
        return hobrData.getStateCode()+ ":" +  hobrData.getData().getBranchCode();
    }
}
