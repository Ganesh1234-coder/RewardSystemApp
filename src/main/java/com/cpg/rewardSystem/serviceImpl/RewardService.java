package com.cpg.rewardSystem.serviceImpl;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cpg.rewardSystem.Iservice.IRewardService;
import com.cpg.rewardSystem.config.CustomerData;
import com.cpg.rewardSystem.entity.Transaction;
import com.cpg.rewardSystem.exception.DataNotFoundExcepition;
import com.cpg.rewardSystem.repository.TranscationRepository;

@Service
public class RewardService implements IRewardService{
	
	private Logger log=LoggerFactory.getLogger(RewardService.class);

	@Autowired
	private TranscationRepository transcationRepository;

	/**
	 * Retrieve reward points for a given customer over the last three months.
	 * @param customerId The ID of the customer.
	 * @return A map of months and corresponding reward points. 
	 */

	@Override
	public ResponseEntity<CustomerData> getRewardByCustomerResponse(Long customerId) {
		 LocalDate today=LocalDate.now();
		 CustomerData csutomerData=new CustomerData();
		 Map<String, Integer> monthData = new LinkedHashMap();
		 
		 try {
			 csutomerData.setCustomerId(customerId);
			 for(int i=0;i<3;i++) {
				 LocalDate start=today.minusMonths(i+1).minusDays(1);
				 log.info("start Date::"+start);
				 LocalDate end=start.plusMonths(1).plusDays(1);
				 log.info("end Date::"+end);
				 List<Transaction> transcations=transcationRepository.findByCustomerIdAndDateRange(customerId, start, end);
				 transcations.forEach(val-> log.info("Data from DB: Id-"+val.getId()+", CustomerId- "+val.getCustomerId()+", Amount- "+val.getAmount()+", Date- "+val.getTransactionDate()));
				 int totalPoints=transcations.stream().mapToInt(t->calculatePoints(t.getAmount())).peek(p-> log.info("returing sum:"+p)).sum();
				 monthData.put(end.getMonth().toString(), totalPoints);
			 }
			 int sum = monthData.values().stream().mapToInt(Integer :: intValue).sum();
			 csutomerData.setMonth_points(monthData);
			 csutomerData.setTotal(sum);
		 }catch (Exception e) {
			throw new DataNotFoundExcepition("Data is not found for mentioned customer id.");
		}
		 
		 
		return ResponseEntity.ok(csutomerData);
	}
	
	
	/**
	 * Calculate reward points based on transaction amount.
	 * @param amount The transaction amount.
	 * @return The calculated reward points.
	 */
	private int calculatePoints(Double amount) {
		int points=0;
		if(amount>100) {
			points += (amount.intValue()-100)*2;
			points += 50;
			log.info("Above 100 points :"+points);
		}else if(amount>50) {
			points += (amount.intValue()-50);
			log.info("Above 50 points :"+points);
		}
		return points;
	}
}
