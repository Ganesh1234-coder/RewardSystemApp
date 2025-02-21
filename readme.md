RewardApp Application
========================

The Reward System Application is a Spring Boot application designed to calculate and retrieve reward points for customers
based on their transaction history. The application provides RESTful endpoints to fetch reward points for a given customer over 
the last three months.

Project Structure
--------------------

The project follows a typical Spring Boot structure:

RewardSystemApp/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── cpg/
│   │   │           └── rewardSystem/
│   │   │				└── RewardAppApplication.java/
│   │   │               ├── config/
│   │   │               │   └── DataLoder.java
│   │   │ 				│   └── CustomerData.java
│   │   │               ├── controller/
│   │   │               │   └── RewardController.java
│   │   │               ├── entity/
│   │   │               │   └── Transaction.java
│   │   │               ├── excepition/
│   │   │               │   └── GlobalExcepitionHandler.java
│   │   │               ├── Iservice/
│   │   │               │   └── IRewardService.java
│   │   │               ├── repository/
│   │   │               │   └── TranscationRepository.java
│   │   │               └── serviceImpl/
│   │   │                   └── RewardService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── cpg/
│                   └── rewardSystem/
│                       ├── RewardAppApplicationTests.java
│                       ├── RewardControllerIntegrationTest.java
│				        └── RewardServiceTest.java
└── pom.xml

Implementation Details
--------------------------
Entity
------
Transaction.java

The Transaction entity represents a transaction made by a customer. 
It includes fields for the  ID, customer ID, transaction amount, and transaction date.

@Entity
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long customerId;
	private Double amount;
	private LocalDate transactionDate;
	
	//Constructor
	//Setter & Getter methods
}

Repository
-------------

TranscationRepository.java

The TranscationRepository interface extends JpaRepository and provides a custom query method 
to find transactions by customer ID and date range.

public interface TranscationRepository extends JpaRepository<Transaction, Long> {

	@Query("SELECT t FROM Transaction t WHERE t.customerId=:customerId AND t.transactionDate BETWEEN 			:startDate AND :endDate")
	public List<Transaction> findByCustomerIdAndDateRange(@Param("customerId") Long customerId,
					@Param("startDate")LocalDate startDate,@Param("endDate")LocalDate endDate);

}

Service
----------

IRewardService.java

The IRewardService interface defines the contract for the reward service.

public interface IRewardService {
	public ResponseEntity<CustomerData> getRewardByCustomerResponse(Long customerId);
}

RewardService.java

The RewardService class implements the IRewardService interface and provides methods to 
calculate reward points and retrieve reward points for a given customer.

@Service
public class RewardService implements IRewardService{
	@Autowired
	private TranscationRepository transcationRepository;

	private int calculatePoints(Double amount) {
		//Calculate reward points
	}

	@Override
	public ResponseEntity<CustomerData> getRewardByCustomer(Long customerId) {
		  // Retrieve reward points for a given customer
	}
}

Exception
-----------

GlobalExceptionHandler.java

Handle the unexpected Exception if occurred in the project

@ControllerAdvice
public class GlobalExcepitionHandler {
	

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public Map<String, String> handleNoSuchElementExcepition(NoSuchElementException ex){
		return Collections.singletonMap("error", "Resource not found.");
	}
	

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Map<String, String> handelExcepition(Exception ex){
		return Collections.singletonMap("error", "An unexpected error occurred.");
	}
	
	/**
	 *	Handle if any error occurred
	 *
	 */
	@ExceptionHandler(DataNotFoundExcepition.class)
	public Map<String, String> handelDataNotFound(DataNotFoundExcepition ex){
		return Collections.singletonMap("error", ex.getMessage());
	}
}

Controller
---------------

RewardController.java

The RewardController class provides a RESTful end-point to retrieve reward points for a given customer.

@RestController
@RequestMapping("/rewards")
public class RewardController {

	@Autowired
	private IRewardService service;
	
	
	@GetMapping("/{customerId}")
	public Map<String, Integer> getRewards(@PathVariable Long customerId){
		// return the result to customer
	}
}

Test
----------

RewardServiceTest.java

JUnit test for the RewardService class.

public class RewardServiceTest {

	@Mock
	private TranscationRepository transcationRepository;
	
	@InjectMocks
	private RewardService rewardService;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	
	@Test
	void testGetRewardByCustomer_withValidCustomerId() {
		// Positive test case
	}
	
	@Test
	void testGetRewardByCustomer_withNoTransactions() {
		// Negative test case
	}
		
}

RewardAppApplicationTests.java

Integration test for the RewardController class.

@SpringBootTest
class RewardAppApplicationTests {

	 
	@Test
	void contextLoads() {
	}
	 
}

package com.cg.rewardSystem;

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

import com.cg.rewardSystem.Iservice.IRewardService;
import com.cg.rewardSystem.config.CustomerData;
import com.cg.rewardSystem.controller.RewardController;  

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

Running the Application
-------------------------

To run the application, use the following command:
mvn spring-boot:run

Running the Tests
--------------------

To run the tests, use the following command:
mvn test

Conclusion
-----------

This project demonstrates a simple reward system application using Spring Boot. It includes a RESTful API to retrieve reward points for customers, 
along with unit and integration tests to ensure the functionality of the service and controller layers.
