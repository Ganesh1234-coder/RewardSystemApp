package com.cpg.rewardSystem.Iservice;

import org.springframework.http.ResponseEntity;

import com.cpg.rewardSystem.config.CustomerData;

public interface IRewardService {
	
	public ResponseEntity<CustomerData> getRewardByCustomerResponse(Long customerId);
}

