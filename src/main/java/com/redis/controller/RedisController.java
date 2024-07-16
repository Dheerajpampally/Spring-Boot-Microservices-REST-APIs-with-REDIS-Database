package com.redis.controller;


import com.redis.model.DataModel;
import com.redis.service.RedisService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisService dataService;


    public RedisController(RedisService dataService) {
        this.dataService = dataService;
    }


    @PostMapping({"/save"})
    public ResponseEntity<String> storeData(@RequestBody List<DataModel> dataModels) {
       this.dataService.storeData(dataModels);
       return ResponseEntity.ok("Data stored successfully.");
    }
    

    @GetMapping("/get/{code}")
    public ResponseEntity<Map<String, Object>> getData(@PathVariable String code) {
        DataModel dataModel = dataService.getData(code);
        Map<String, Object> response = new HashMap<>();

        if (dataModel != null) {
            response.put("code", dataModel.getCode());
            response.put("data", dataModel.getData());
            response.put("status", "S");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", "null");
            response.put("data", "null");
            response.put("status", "F");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    
    @DeleteMapping("/delete/{code}") 
    public ResponseEntity<String> deleteData(@PathVariable String code) {
        boolean result = dataService.deleteData(code);
        if (result) {
            return ResponseEntity.ok("Data deleted successfully for code: " + code);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found for code: " + code);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DataModel>> getAllData() {
    	List<DataModel> allData = dataService.getAllData();
        return ResponseEntity.ok(allData);
    }

    
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAll() {
        boolean result = dataService.deleteAll();
        if (result) {
            return ResponseEntity.ok("All data deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete all data.");
        }
    }
}
