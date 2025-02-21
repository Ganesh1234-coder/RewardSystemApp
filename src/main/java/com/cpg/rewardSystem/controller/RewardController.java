package com.cpg.rewardSystem.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cpg.rewardSystem.Iservice.IRewardService;
import com.cpg.rewardSystem.config.CustomerData;

@RestController
@RequestMapping("/rewards")
public class RewardController {

	@Autowired
	private IRewardService service;
	
	/**
	 * Retrieve reward points for a given customers.
	 * @param customerId The ID of the customer.
	 * @return Customer Data with total and 3 month rewards points. 
	 */
	@GetMapping("/id/{customerId}")
	public ResponseEntity<CustomerData> getRewardsResponse(@PathVariable Long customerId){
		 ResponseEntity<CustomerData> rewardByCustomerResponse = service.getRewardByCustomerResponse(customerId);
		return rewardByCustomerResponse;
	}
}

