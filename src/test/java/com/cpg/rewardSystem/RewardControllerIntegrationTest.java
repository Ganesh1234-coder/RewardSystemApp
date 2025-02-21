package com.cpg.rewardSystem;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.cpg.rewardSystem.Iservice.IRewardService;
import com.cpg.rewardSystem.config.CustomerData;
import com.cpg.rewardSystem.controller.RewardController;  

@WebMvcTest(RewardController.class)
public class RewardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRewardService rewardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRewardsResponse_Positive() throws Exception {
        Long customerId = 1L;
        
        CustomerData customerData = new CustomerData();
        customerData.setCustomerId(customerId);
        customerData.setMonth_points(Map.of("JANUARY", 120, "FEBRUARY", 90, "DECEMBER", 30));
        customerData.setTotal(240);

        when(rewardService.getRewardByCustomerResponse(customerId)).thenReturn(ResponseEntity.ok(customerData));

        mockMvc.perform(get("/rewards/id/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(customerId.intValue())))
                .andExpect(jsonPath("$.month_points.JANUARY", is(120)))
                .andExpect(jsonPath("$.month_points.FEBRUARY", is(90)))
                .andExpect(jsonPath("$.month_points.DECEMBER", is(30)))
                .andExpect(jsonPath("$.total", is(240)));
    }

    @Test
    public void testGetRewardsResponse_Negative() throws Exception {
        Long customerId = 1L;

        when(rewardService.getRewardByCustomerResponse(customerId)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/rewards/id/{customerId}", customerId))
                .andExpect(status().isNotFound());
    }
}


