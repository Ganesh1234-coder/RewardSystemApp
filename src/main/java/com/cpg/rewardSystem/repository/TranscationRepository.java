package com.cpg.rewardSystem.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cpg.rewardSystem.entity.Transaction;

public interface TranscationRepository extends JpaRepository<Transaction, Long> {
	
	/**
	 * Fetch transaction for a given customer within a specified date range.
	 * @param customerId The ID of the customer.
	 * @param startDate The start date of the range.
	 * @param endDate The end date of the range.
	 * @return List of Transaction within the range.
	 */
	
	@Query("SELECT t FROM Transaction t WHERE t.customerId=:customerId AND t.transactionDate BETWEEN :startDate AND :endDate")
	public List<Transaction> findByCustomerIdAndDateRange(@Param("customerId") Long customerId,@Param("startDate")LocalDate startDate,
			@Param("endDate")LocalDate endDate);

}