package com.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.model.DataModel;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RedisService {

    private static final String REDIS_HASH_KEY = "RedisDataStorage";

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, String> hashOperations;
    private final ObjectMapper objectMapper;

    public RedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.objectMapper = objectMapper;
    }

    public void storeData(List<DataModel> dataModels) {
        for (DataModel dataModel : dataModels) {
            try {
                String jsonData = objectMapper.writeValueAsString(dataModel);
                hashOperations.put(REDIS_HASH_KEY, dataModel.getCode(), jsonData);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public DataModel getData(String code) {
        String jsonData = hashOperations.get(REDIS_HASH_KEY, code);
        if (jsonData != null) {
            try {
                return objectMapper.readValue(jsonData, DataModel.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean deleteData(String code) {
        return hashOperations.delete(REDIS_HASH_KEY, code) > 0;
    }

    public List<DataModel> getAllData() {
        Map<String, String> hashEntries = hashOperations.entries(REDIS_HASH_KEY);
        List<DataModel> allData = new ArrayList<>();
        hashEntries.forEach((key, value) -> {
            try {
                allData.add(objectMapper.readValue(value, DataModel.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return allData;
    }

    public boolean deleteAll() {
        return redisTemplate.delete(REDIS_HASH_KEY);
    }     
}


