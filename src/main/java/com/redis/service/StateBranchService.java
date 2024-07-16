package com.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.model.StateModel;


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
public class StateBranchService {

    private static final String REDIS_HASH_KEY = "ALLBRANCHDETAILS";

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOperations; // Added HashOperations
    private final ObjectMapper objectMapper;

    public StateBranchService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash(); // Initialize HashOperations
        this.objectMapper = objectMapper;
    }
    
    public boolean storeStateBranchData(List<StateModel> stateBranchDataList) {
        try {
            // Clear the existing data
            redisTemplate.delete(REDIS_HASH_KEY);
            
            // Store new data
            for (StateModel stateData : stateBranchDataList) {
                String jsonData = objectMapper.writeValueAsString(stateData);
                hashOperations.put(REDIS_HASH_KEY, generateHashKey(stateData), jsonData);
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



    public List<StateModel> getAllData() {
        List<StateModel> allDataList = new ArrayList<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
            	StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
                allDataList.add(stateData);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return allDataList;
    }
    
//    public List<String> getAllStateNames() {
//        List<String> allStateNames = new ArrayList<>();
//        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
//        for (String key : keys) {
//            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
//            try {
//                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
//                String stateName = stateData.getData().getStateName();
//                if (!allStateNames.contains(stateName)) {
//                    allStateNames.add(stateName);
//                }
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//        return allStateNames;
//    }

//    public List<Map<String, String>> getAllStateNames() {
//        List<Map<String, String>> allStateNames = new ArrayList<>();
//        Set<String> uniqueStateNames = new HashSet<>(); // Use a Set to store unique zonal names
//        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
//        for (String key : keys) {
//            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
//            try {
//            	StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
//                String stateName = stateData.getData().getStateName();
//                if (!uniqueStateNames.contains(stateName)) { // Check if zonal name is already encountered
//                    Map<String, String> stateInfo = new HashMap<>();
//                    stateInfo.put("state_name", stateName);
//                    stateInfo.put("state_code", stateData.getStateCode());
//                    allStateNames.add(stateInfo);
//                    uniqueStateNames.add(stateName); // Add zonal name to the set to mark it as encountered
//                }
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//        return allStateNames;
//    }
//    
    
    public List<Map<String, String>> getAllStateNames() {
        List<Map<String, String>> allStateNames = new ArrayList<>();
        Set<String> uniqueStateNames = new HashSet<>(); // Use a Set to store unique state names
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
                if ("Yes".equals(stateData.getData().getRetail_Branch())) { // Check if Retail_Branch is "Yes"
                    String stateName = stateData.getData().getStateName();
                    if (!uniqueStateNames.contains(stateName)) { // Check if state name is already encountered
                        Map<String, String> stateInfo = new HashMap<>();
                        stateInfo.put("state_name", stateName);
                        stateInfo.put("state_code", stateData.getStateCode());
                        allStateNames.add(stateInfo);
                        uniqueStateNames.add(stateName); // Add state name to the set to mark it as encountered
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return allStateNames;
    }

    
    
    
//    public List<String> getBranchNamesByStateCode(String stateCode) {
//        List<String> branchNames = new ArrayList<>();
//        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
//        for (String key : keys) {
//            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
//            try {
//                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
//                if (stateData.getStateCode().trim().equalsIgnoreCase(stateCode.trim())) {
//                    branchNames.add(stateData.getData().getBranchName().trim());
//                }
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//        return branchNames;
//    }

//    public List<Map<String, String>> getBranchNamesByStateCode(String stateCode) {
//        List<Map<String, String>> branchNamesList = new ArrayList<>();
//        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
//        for (String key : keys) {
//            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
//            try {
//                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
//                if (stateData.getStateCode().trim().equalsIgnoreCase(stateCode.trim())) {
//                    Map<String, String> branchInfo = new HashMap<>();
//                    branchInfo.put("branch_code", stateData.getData().getBranchCode().trim());
//                    branchInfo.put("branch_name", stateData.getData().getBranchName().trim());
//                    branchNamesList.add(branchInfo);
//                }
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//        return branchNamesList;
//    }


    public List<Map<String, String>> getBranchNamesByStateCode(String stateCode) {
        List<Map<String, String>> branchNamesList = new ArrayList<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
                if (stateData.getStateCode().trim().equalsIgnoreCase(stateCode.trim()) && "Yes".equals(stateData.getData().getRetail_Branch())) {
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
                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
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
                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
                if (stateName.equals(stateData.getData().getStateName()) && branchName.equals(stateData.getData().getBranchName())) {
                    branchDetails.put("address1", stateData.getData().getAddress1());
                    branchDetails.put("address2", stateData.getData().getAddress2());
                    branchDetails.put("address3", stateData.getData().getAddress3());
                    branchDetails.put("mobile", stateData.getData().getMobile());
                    branchDetails.put("latitude", stateData.getData().getLatitude());
                    branchDetails.put("longitude", stateData.getData().getLongitude());
                    break; // Stop searching after finding the matching branch
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return branchDetails;
    }
    
//    public Map<String, String> getBranchDetailsAll(String stateCode, String branchCode) {
//        Map<String, String> branchDetails = new HashMap<>();
//        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
//        for (String key : keys) {
//            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
//            try {
//                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
//                if (stateCode.equalsIgnoreCase(stateData.getStateCode().trim()) && branchCode.equalsIgnoreCase(stateData.getData().getBranchCode().trim())) {
//                    branchDetails.put("address1", stateData.getData().getAddress1().trim());
//                    branchDetails.put("address2", stateData.getData().getAddress2().trim());
//                    branchDetails.put("address3", stateData.getData().getAddress3().trim());
//                    branchDetails.put("mobile", stateData.getData().getMobile().trim());
//                    branchDetails.put("latitude", stateData.getData().getLatitude().trim());
//                    branchDetails.put("longitude", stateData.getData().getLongitude().trim());
//                    break; // Stop searching after finding the matching branch
//                }
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//        return branchDetails;
//    }
    
    public Map<String, String> getBranchDetailsAll(String stateCode, String branchCode) {
        Map<String, String> branchDetails = new HashMap<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
                if (stateCode.equalsIgnoreCase(stateData.getStateCode().trim()) && branchCode.equalsIgnoreCase(stateData.getData().getBranchCode().trim()) && "Yes".equals(stateData.getData().getRetail_Branch())) {
                    branchDetails.put("address1", stateData.getData().getAddress1().trim());
                    branchDetails.put("address2", stateData.getData().getAddress2().trim());
                    branchDetails.put("address3", stateData.getData().getAddress3().trim());
                    branchDetails.put("mobile", stateData.getData().getMobile().trim());
                    branchDetails.put("latitude", stateData.getData().getLatitude().trim());
                    branchDetails.put("longitude", stateData.getData().getLongitude().trim());
                    break; // Stop searching after finding the matching branch
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return branchDetails;
    }

    
    

    private String generateHashKey(StateModel stateData) {
        return stateData.getStateCode()+ ":" +  stateData.getData().getBranchCode();
    }
    
////////////////////////////////////////////////////////////////////////////////////////////////////// 
    
    
    public List<Map<String, String>> getAllStateNamesForHOBranch() {
        List<Map<String, String>> allStateNames = new ArrayList<>();
        Set<String> uniqueStateNames = new HashSet<>(); // Use a Set to store unique state names
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
                if ("HO".equals(stateData.getData().getRetail_Branch())) { // Check if Retail_Branch is "HO"
                    String stateName = stateData.getData().getStateName();
                    if (!uniqueStateNames.contains(stateName)) { // Check if state name is already encountered
                        Map<String, String> stateInfo = new HashMap<>();
                        stateInfo.put("state_name", stateName);
                        stateInfo.put("state_code", stateData.getStateCode());
                        allStateNames.add(stateInfo);
                        uniqueStateNames.add(stateName); // Add state name to the set to mark it as encountered
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return allStateNames;
    }

    public List<Map<String, String>> getBranchNamesByStateCodeForHOBranch(String stateCode) {
        List<Map<String, String>> branchNamesList = new ArrayList<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
                if (stateData.getStateCode().trim().equalsIgnoreCase(stateCode.trim()) && "HO".equals(stateData.getData().getRetail_Branch())) {
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
    
    
    public Map<String, String> getBranchDetailsForHOBranch(String stateCode, String branchCode) {
        Map<String, String> branchDetails = new HashMap<>();
        Set<String> keys = hashOperations.keys(REDIS_HASH_KEY);
        for (String key : keys) {
            String jsonData = hashOperations.get(REDIS_HASH_KEY, key);
            try {
                StateModel stateData = objectMapper.readValue(jsonData, StateModel.class);
                if (stateCode.equalsIgnoreCase(stateData.getStateCode().trim()) && branchCode.equalsIgnoreCase(stateData.getData().getBranchCode().trim()) && "HO".equals(stateData.getData().getRetail_Branch())) {
                    branchDetails.put("address1", stateData.getData().getAddress1().trim());
                    branchDetails.put("address2", stateData.getData().getAddress2().trim());
                    branchDetails.put("address3", stateData.getData().getAddress3().trim());
                    branchDetails.put("mobile", stateData.getData().getMobile().trim());
                    branchDetails.put("latitude", stateData.getData().getLatitude().trim());
                    branchDetails.put("longitude", stateData.getData().getLongitude().trim());
                    break; // Stop searching after finding the matching branch
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return branchDetails;
    }


}









