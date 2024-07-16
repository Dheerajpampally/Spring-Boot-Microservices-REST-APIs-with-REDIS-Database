package com.redis.controller;

import com.redis.model.PincodeData;
import com.redis.service.PincodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/PinPlace")
public class PincodeController {

    private final PincodeService pincodeService;

    public PincodeController(PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> pincodeStoreData(@RequestBody List<PincodeData> pincodeDataList) {
        try {
            boolean result = pincodeService.pincodeStoreData(pincodeDataList);
            if (result) {
                return ResponseEntity.ok("Data stored successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to store data.");
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @GetMapping("/get/{stateCode}")
    public List<PincodeData> getZonalData(@PathVariable String stateCode) {
        return pincodeService.getZonalData(stateCode);
    }

    @GetMapping("/getAll")
    public List<PincodeData> getAllData() {
        return pincodeService.getAllData();
    }

    @GetMapping("/getAllStatename")
    public List<Map<String, String>> getAllStatename() {
        return pincodeService.getAllStatename();
    }

    @GetMapping("/getDisforState/{stateCode}")
    public List<Map<String, String>> getDisforState(@PathVariable String stateCode) {
        return pincodeService.getDisforState(stateCode);
    }

    @GetMapping("/getPincode/{districtCode}")
    public List<Map<String, String>> getPinforDis(@PathVariable String districtCode) {
        return pincodeService.getPinforDis(districtCode);
    }

    @GetMapping("/getPlace/{pinCode}")
    public List<Map<String, String>> getPlacPin(@PathVariable String pinCode) {
        return pincodeService.getPlacPin(pinCode);
    }

    @GetMapping("/getAllPincodeAndPlace/{districtCode}")
    public List<Map<String, String>> getAllPincodeAndPlaceForDistrict(@PathVariable String districtCode) {
        return pincodeService.getAllPincodeAndPlaceForDistrict(districtCode);
    }
}
