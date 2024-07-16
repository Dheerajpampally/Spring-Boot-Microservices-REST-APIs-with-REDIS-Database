//package com.redis.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.uae.Syb.core.UAESybAccess;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class PincodeDataService {
//
//    private static final String REDIS_HASH_KEY = "PINCODEDATA";
//
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final HashOperations<String, String, String> hashOperations;
//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public PincodeDataService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
//        this.redisTemplate = redisTemplate;
//        this.hashOperations = redisTemplate.opsForHash();
//        this.objectMapper = objectMapper;
//    }
//
//    public void fetchDataAndStoreInRedis() {
//        try {
//            String DB = "SybaseDBMATRIX";
//            String QUERY1 = "ums..prc_ums_state_sel";
//            Object[] PARAMS1 = {}; // Add parameters if needed
//
//            // Execute stored procedure using the provided jar file
//            List<Map<String, Object>> resultList = UAESybAccess.executeQueryPrcList(DB, QUERY1, PARAMS1, true);
//            
//            System.out.println("resultList"+resultList);
//
//            // Process the result list and store data in Redis
//            for (Map<String, Object> result : resultList) {
//                Map<String, Object> pincodeData = new HashMap<>();
//                pincodeData.put("state_code", result.get("state_code"));
//
//                Map<String, Object> data = new HashMap<>();
//                data.put("state_name", result.get("state_name"));
//                data.put("district_code", result.get("district_code"));
//                data.put("district_name", result.get("district_name"));
//                data.put("pincode", result.get("pincode"));
//                data.put("place", result.get("place"));
//
//                pincodeData.put("data", data);
//
//                // Convert pincodeData to JSON string
//                String jsonData = objectMapper.writeValueAsString(pincodeData);
//
//                // Store pincodeData in Redis hash
//                hashOperations.put(REDIS_HASH_KEY, "key_" + result.get("state_code"), jsonData);
//            }
//
//        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
//            e.printStackTrace();
//            // Handle exceptions
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Handle other exceptions
//        }
//    }
//}
