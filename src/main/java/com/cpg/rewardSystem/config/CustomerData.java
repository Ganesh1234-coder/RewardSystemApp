package com.cpg.rewardSystem.config;

import java.util.Map;

public class CustomerData {

	private Long customerId;
	private Map<String, Integer> month_points;
	private int total;
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Map<String, Integer> getMonth_points() {
		return month_points;
	}
	public void setMonth_points(Map<String, Integer> month_points) {
		this.month_points = month_points;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public void addMonthReward(String month, Integer value) {
		this.month_points.put(month, value);
	}
	
	@Override
	public String toString() {
		return "CustomerData [customerId=" + customerId + ", month_points=" + month_points + ", total=" + total + "]";
	}
	
	
}
