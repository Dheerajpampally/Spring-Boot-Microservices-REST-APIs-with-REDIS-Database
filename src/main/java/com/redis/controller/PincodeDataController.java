//package com.redis.controller;
//
//import com.redis.service.PincodeDataService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/data")
//public class PincodeDataController {
//
//    private final PincodeDataService pincodeDataService;
//
//    @Autowired
//    public PincodeDataController(PincodeDataService pincodeDataService) {
//        this.pincodeDataService = pincodeDataService;
//    }
//
//    @GetMapping("/fetch")
//    public String fetchDataAndStoreInRedis() {
//        pincodeDataService.fetchDataAndStoreInRedis();
//        return "Data fetched and stored in Redis successfully";
//    }
//}
