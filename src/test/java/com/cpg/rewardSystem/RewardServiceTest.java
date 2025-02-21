package com.cpg.rewardSystem;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.cpg.rewardSystem.config.CustomerData;
import com.cpg.rewardSystem.entity.Transaction;
import com.cpg.rewardSystem.repository.TranscationRepository;
import com.cpg.rewardSystem.serviceImpl.RewardService;

public class RewardServiceTest {
	
	@Mock
    private TranscationRepository transactionRepository;

    @InjectMocks
    private RewardService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRewardByCustomer_withValidCustomerId(){
        Long customerId = 1L;
        LocalDate today = LocalDate.now();
        List<Transaction> transcatios=Arrays.asList(
				new Transaction(1L, 100.0, LocalDate.now().minusMonths(1)),
				new Transaction(1L, 150.0, LocalDate.now().minusMonths(2)),
				new Transaction(1L, 200.0, LocalDate.now().minusMonths(3))
				);
		

        // Mock repository responses
        when(transactionRepository.findByCustomerIdAndDateRange(eq(customerId), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(transcatios);

        // Call the method
        ResponseEntity<CustomerData> response = service.getRewardByCustomerResponse(customerId);

        // Verify the response
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        CustomerData customerData = response.getBody();
        assertNotNull(customerData);
        assertEquals(customerId, customerData.getCustomerId());
        assertEquals(3, customerData.getMonth_points().size());
        assertEquals(1350, customerData.getTotal());
        
    }
    
    @Test
    public void testGetRewardByCustomer_withNoTransactions() {
        Long customerId = 1L;
        LocalDate today = LocalDate.now();

        // Mock repository responses to return empty lists
        when(transactionRepository.findByCustomerIdAndDateRange(eq(customerId), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(Collections.emptyList());

        // Call the method
        ResponseEntity<CustomerData> response = service.getRewardByCustomerResponse(customerId);

        // Verify the response
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        CustomerData customerData = response.getBody();
        assertNotNull(customerData);
        assertEquals(customerId, customerData.getCustomerId());
        assertEquals(3, customerData.getMonth_points().size());
        assertEquals(0, customerData.getTotal());

        
    }
	
}
