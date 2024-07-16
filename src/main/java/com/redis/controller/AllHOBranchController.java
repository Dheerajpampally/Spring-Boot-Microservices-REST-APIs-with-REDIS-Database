package com.redis.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.redis.model.HOBranchModel;
import com.redis.service.HOBranchService;
	@RestController
	@RequestMapping("/ALLBRANCHES")
	public class AllHOBranchController {
		private final HOBranchService hobranchService;

	    public AllHOBranchController(HOBranchService hobranchService) {
	        this.hobranchService = hobranchService;
	    }

	    @PostMapping("/storeHOBranchData")
	    public String storeStateBranchData(@RequestBody List<HOBranchModel> HOBranchDataList) {
	        boolean stored = hobranchService.storeHOBranchData(HOBranchDataList);
	        if (stored) {
	            return "StateBranchData stored successfully!";
	        } else {
	            return "Failed to store StateBranchData.";
	        }
	    }
	    
	    @GetMapping("/getAll")
	    public List<HOBranchModel> getAllData() {
	        return hobranchService.getAllData();
	    }

	    @GetMapping("/getAllStateNames")
	    public List<Map<String, String>> getAllStateNames() {
	        return hobranchService.getAllStateNames();
	    }
	    
	    @GetMapping("/getBranchNamesByStateName/{stateName}")
	    public List<String> getBranchNamesByStateName(@PathVariable String stateName) {
	        return hobranchService.getBranchNamesByStateName(stateName);
	    } 

	    @GetMapping("/getBranchNamesByStateCode/{stateCode}")
	    public List<Map<String, String>> getBranchNamesByStateCode(@PathVariable String stateCode) {
	        return hobranchService.getBranchNamesByStateCode(stateCode);
	    }
	    
	    @GetMapping("/getAllBranchDetails/{stateCode}/{branchCode}")
	    public Map<String, String> getBranchDetails(@PathVariable String stateCode, @PathVariable String branchCode) {
	        return hobranchService.getBranchDetailsAll(stateCode, branchCode);
	    }
	    
	}

