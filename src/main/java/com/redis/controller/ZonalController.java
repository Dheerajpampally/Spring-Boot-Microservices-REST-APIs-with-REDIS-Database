package com.redis.controller;

import com.redis.model.ZonalData;
import com.redis.service.ZonalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ZonalController {

    private final ZonalService zonalService;

    public ZonalController(ZonalService zonalService) {
        this.zonalService = zonalService;
    }

    @PostMapping("/storeZonalRegionBranchInfo")
    public ResponseEntity<String> storeZonalData(@RequestBody List<ZonalData> zonalDataList) {
        boolean result = zonalService.storeZonalData(zonalDataList);
        if (result) {
            return ResponseEntity.ok("Data stored successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to store data.");
        }
    }

    @GetMapping("/getAllZonalRegionBranchList/{zonalBrcode}")
    public List<ZonalData> getZonalData(@PathVariable String zonalBrcode) {
        return zonalService.getZonalData(zonalBrcode);
    }

    @GetMapping("/get/{zonalBrcode}/{regCode}")
    public List<String> getRegData(@PathVariable String regCode) {
        return zonalService.getRegData(regCode);
    }

    @GetMapping("/get/region/{regCode}")
    public List<String> getRegion(@PathVariable String regCode) {
        return zonalService.getRegData(regCode);
    }

    @GetMapping("/get/{zonalBrcode}/{regCode}/{cBranchcode}")
    public List<String> getCBranchcodeData(@PathVariable String cBranchcode) {
        return zonalService.getCBranchcodeData(cBranchcode);
    }

    @GetMapping("/get/c_branchcode/{cBranchcode}")
    public List<String> getCBranchcode(@PathVariable String cBranchcode) {
        return zonalService.getCBranchcodeData(cBranchcode);
    }

    @GetMapping("/getAllZonRegBranchList")
    public List<ZonalData> getAllZonalDatas() {
        return zonalService.getAllData();
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAll() {
        boolean result = zonalService.deleteAll();
        if (result) {
            return ResponseEntity.ok("All data deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete all data.");
        }
    }

    @DeleteMapping("/deleteByZonalBrcode/{zonalBrcode}")
    public ResponseEntity<String> deleteDataByZonalBrcode(@PathVariable String zonalBrcode) {
        boolean result = zonalService.deleteDataByZonalBrcode(zonalBrcode);
        if (result) {
            return ResponseEntity.ok("Data deleted successfully for zonalBrcode: " + zonalBrcode);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete data for zonalBrcode: " + zonalBrcode);
        }
    }

    @GetMapping("/getZonalNames")
    public List<String> getAllZonalNames() {
        return zonalService.getAllZonalNames();
    }

    @GetMapping("/getAllZonalInfo")
    public List<Map<String, String>> getAllZonalInfo() {
        return zonalService.getAllZonalInfo();
    }

//    @GetMapping("/getReg")
//    public List<String> getAllReg() {
//        return zonalService.getAllReg();
//    }
//
    @GetMapping("/getAllRegions")
    public List<Map<String, String>> getAllRegions() {
        return zonalService.getAllRegion();
    }

//    @GetMapping("/getBrn")
//    public List<String> getAllBrn() {
//        return zonalService.getAllBrn();
//    }
//
    @GetMapping("/getAllBranches")
    public List<Map<String, String>> getAllBranch() {
        return zonalService.getAllBranch();
    }

    @GetMapping("/getRegionForZonal/{zonalBrcode}")
    public List<Map<String, String>> getRegDataForZonal(@PathVariable String zonalBrcode) {
        return zonalService.getRegDataForZonal(zonalBrcode);
    }
//
    @GetMapping("/getBranchForRegion/{regCode}")
    public List<Map<String, String>> getRegBrData(@PathVariable String regCode) {
        return zonalService.getRegBrData(regCode);
    }
}
