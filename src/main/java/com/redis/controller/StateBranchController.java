package com.redis.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.redis.model.StateModel;

import com.redis.service.StateBranchService;

@RestController
@RequestMapping("/state")
public class StateBranchController {
	private final StateBranchService stateBranchService;

    public StateBranchController(StateBranchService stateBranchService) {
        this.stateBranchService = stateBranchService;
    }

    @PostMapping("/storeStateBranchData")
    public String storeStateBranchData(@RequestBody List<StateModel> stateBranchDataList) {
        boolean stored = stateBranchService.storeStateBranchData(stateBranchDataList);
        if (stored) {
            return "StateBranchData stored successfully!";
        } else {
            return "Failed to store StateBranchData.";
        }
    }
    
    @GetMapping("/getAll")
    public List<StateModel> getAllData() {
        return stateBranchService.getAllData();
    }

    @GetMapping("/getAllStateNames")
    public List<Map<String, String>> getAllStateNames() {
        return stateBranchService.getAllStateNames();
    }
    
    @GetMapping("/getBranchNamesByStateName/{stateName}")
    public List<String> getBranchNamesByStateName(@PathVariable String stateName) {
        return stateBranchService.getBranchNamesByStateName(stateName);
    }

    @GetMapping("/getBranchNamesByStateCode/{stateCode}")
    public List<Map<String, String>> getBranchNamesByStateCode(@PathVariable String stateCode) {
        return stateBranchService.getBranchNamesByStateCode(stateCode);
    }
    
    @GetMapping("/getAllBranchDetails/{stateCode}/{branchCode}")
    public Map<String, String> getBranchDetails(@PathVariable String stateCode, @PathVariable String branchCode) {
        return stateBranchService.getBranchDetailsAll(stateCode, branchCode);
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @GetMapping("/getAllStateNamesForHOBranch")
    public List<Map<String, String>> getAllStateNamesForHOBranch() {
        return stateBranchService.getAllStateNamesForHOBranch();
    }
    
    @GetMapping("/getBranchNamesByStateCodeForHOBranch/{stateCode}")
    public List<Map<String, String>> getBranchNamesByStateCodeForHOBranch(@PathVariable String stateCode) {
        return stateBranchService.getBranchNamesByStateCodeForHOBranch(stateCode);
    }
    
    @GetMapping("/getBranchDetailsForHOBranch/{stateCode}/{branchCode}")
    public Map<String, String> getBranchDetailsForHOBranch(@PathVariable String stateCode, @PathVariable String branchCode) {
        return stateBranchService.getBranchDetailsForHOBranch(stateCode, branchCode);
    

    }
}


