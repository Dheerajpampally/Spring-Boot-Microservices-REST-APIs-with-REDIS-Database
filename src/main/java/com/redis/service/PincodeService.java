package com.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.model.PincodeData;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class PincodeService {

    private static final String REDIS_HASH_KEY = "PincodeData";

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOperations;
    private final ObjectMapper objectMapper;

    public PincodeService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.objectMapper = objectMapper;
    }

    public boolean pincodeStoreData(List<PincodeData> pincodeDataList) {
        try {
            int batchSize = 1000; // Adjust the batch size as needed
            for (int i = 0; i < pincodeDataList.size(); i += batchSize) {
                List<PincodeData> batchData = pincodeDataList.subList(i, Math.min(i + batchSize, pincodeDataList.size()));
                Map<String, String> batchMap = new HashMap<>();
                for (PincodeData pincodeData : batchData) {
                    String jsonData = objectMapper.writeValueAsString(pincodeData);
                    String hashKey = generateHashKey(pincodeData);
                    batchMap.put(hashKey, jsonData);
                }
                hashOperations.putAll(REDIS_HASH_KEY, batchMap);
            }
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateHashKey(PincodeData pincodeData) {
        String stateCode = pincodeData.getState_code();
        String districtCode = pincodeData.getData().getDistrict_code();
        return stateCode + ":" + districtCode;
    }


    public List<PincodeData> getZonalData(String stateCode) {
        List<PincodeData> pincodeDataList = new ArrayList<>();
        Map<String, String> entries = hashOperations.entries(REDIS_HASH_KEY);
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(stateCode + ":")) { // Update the condition to match the state code prefix
                String jsonData = entry.getValue();
                try {
                    PincodeData pincodeData = objectMapper.readValue(jsonData, PincodeData.class);
                    pincodeDataList.add(pincodeData);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return pincodeDataList;
    }

    public List<Map<String, String>> getAllPincodeAndPlaceForDistrict(String districtCode) {
        List<Map<String, String>> pinPlaceList = new ArrayList<>();
        Map<String, String> entries = hashOperations.entries(REDIS_HASH_KEY);
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            try {
                PincodeData pincodeData = objectMapper.readValue(entry.getValue(), PincodeData.class);
                if (districtCode.equals(pincodeData.getData().getDistrict_code())) {
                    Map<String, String> PinPlaceData = new HashMap<>();
                    PinPlaceData.put("pincode", pincodeData.getData().getPincode());
                    PinPlaceData.put("place", pincodeData.getData().getPlace().toUpperCase());
                    pinPlaceList.add(PinPlaceData);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return pinPlaceList;
    }


	//public List<Map<String, String>> getAllData() {
	//    List<Map<String, String>> allDataList = new ArrayList<>();
	//    Map<String, String> entries = hashOperations.entries(REDIS_HASH_KEY);
	//    for (String jsonData : entries.values()) {
	//        try {
	//            PincodeData pincodeData = objectMapper.readValue(jsonData, PincodeData.class);
	//            Map<String, String> dataInfo = new HashMap<>();
	//            dataInfo.put("pincode", pincodeData.getData().getPincode());
	//            dataInfo.put("place", pincodeData.getData().getPlace().toUpperCase());
	//            dataInfo.put("state_name", pincodeData.getData().getState_name().trim().toUpperCase());
	//            dataInfo.put("state_code", pincodeData.getState_code());
	//            dataInfo.put("district_name", pincodeData.getData().getDistrict_name().trim().toUpperCase());
	//            dataInfo.put("district_code", pincodeData.getData().getDistrict_code());
	//            allDataList.add(dataInfo);
	//        } catch (JsonProcessingException e) {
	//            e.printStackTrace();
	//        }
	//    }
	//    return allDataList;
	//}
	
	public List<Map<String, String>> getPlacPin(String pinCode) {
	    List<Map<String, String>> placPinList = new ArrayList<>();
	    Map<String, String> entries = hashOperations.entries(REDIS_HASH_KEY);
	    for (String jsonData : entries.values()) {
	        try {
	            PincodeData pincodeData = objectMapper.readValue(jsonData, PincodeData.class);
	            if (pinCode.equals(pincodeData.getData().getPincode())) {
	                Map<String, String> PlacData = new HashMap<>();
	                PlacData.put("place", pincodeData.getData().getPlace().toUpperCase());
	                placPinList.add(PlacData);
	            }
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
	    }
	    return placPinList;
	}
	public List<Map<String, String>> getPinforDis(String districtCode) {
	    List<Map<String, String>> pinList = new ArrayList<>();
	    Map<String, String> entries = hashOperations.entries(REDIS_HASH_KEY);
	    for (String jsonData : entries.values()) {
	        try {
	            PincodeData pincodeData = objectMapper.readValue(jsonData, PincodeData.class);
	            if (districtCode.equals(pincodeData.getData().getDistrict_code())) {
	                Map<String, String> PinData = new HashMap<>();
	                PinData.put("pincode", pincodeData.getData().getPincode());
	                pinList.add(PinData);
	            }
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
	    }
	    return pinList;
	}
	
	public List<Map<String, String>> getDisforState(String stateCode) {
	    List<Map<String, String>> districtList = new ArrayList<>();
	    Map<String, String> entries = hashOperations.entries(REDIS_HASH_KEY);
	    for (Map.Entry<String, String> entry : entries.entrySet()) {
	        String jsonData = entry.getValue();
	        try {
	            PincodeData pincodeData = objectMapper.readValue(jsonData, PincodeData.class);
	            if (stateCode.equals(pincodeData.getState_code())) {
	                Map<String, String> disInfo = new HashMap<>();
	                disInfo.put("district_name", pincodeData.getData().getDistrict_name().trim().toUpperCase());
	                disInfo.put("district_code", pincodeData.getData().getDistrict_code());
	                districtList.add(disInfo);
	            }
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
	    }
	    return districtList;
	}
	
	public List<Map<String, String>> getAllStatename() {
	    List<Map<String, String>> stateList = new ArrayList<>();
	    Map<String, String> entries = hashOperations.entries(REDIS_HASH_KEY);
	    for (String jsonData : entries.values()) {
	        try {
	            PincodeData pincodeData = objectMapper.readValue(jsonData, PincodeData.class);
	            Map<String, String> pinInfo = new HashMap<>();
	            String stateName = pincodeData.getData().getState_name().trim().toUpperCase();
	            pinInfo.put("state_name", stateName);
	            pinInfo.put("state_code", pincodeData.getState_code());
	            stateList.add(pinInfo);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
	    }
	    // Remove duplicates using distinct operation on state_name
	    return stateList.stream()
	            .collect(Collectors.collectingAndThen(
	                Collectors.toMap(
	                    // Key mapper
	                    map -> map.get("state_name"),
	                    // Value mapper
	                    map -> map,
	                    // Merge function (in case of duplicates)
	                    (existing, replacement) -> existing,
	                    // Supplier for the map implementation
	                    HashMap::new
	                ),
	                map -> new ArrayList<>(map.values())
	            ));
	}

	public List<PincodeData> getAllData() {
	    List<PincodeData> allDataList = new ArrayList<>();
	    Map<String, String> entries = hashOperations.entries(REDIS_HASH_KEY);
	    for (String jsonData : entries.values()) {
	        try {
	            PincodeData pincodeData = objectMapper.readValue(jsonData, PincodeData.class);
	            allDataList.add(pincodeData);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
	    }
	    return allDataList;
	}


}
