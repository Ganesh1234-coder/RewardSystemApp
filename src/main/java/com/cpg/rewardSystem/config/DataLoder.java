package com.cpg.rewardSystem.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cpg.rewardSystem.entity.Transaction;
import com.cpg.rewardSystem.repository.TranscationRepository;

@Component
public class DataLoder implements CommandLineRunner {

	@Autowired
	private TranscationRepository repo;
	
	@Override
	public void run(String... args) throws Exception {

		repo.saveAll(Arrays.asList(
				new Transaction(1L, 120.0, LocalDate.now().minusMonths(1)),
				new Transaction(1L, 80.0, LocalDate.now().minusMonths(2)),
				new Transaction(2L, 200.0, LocalDate.now().minusMonths(1)),
				new Transaction(2L, 50.0, LocalDate.now().minusMonths(3)),
				new Transaction(3L, 120.0, LocalDate.now().minusMonths(0))
				));
		

	}

}

