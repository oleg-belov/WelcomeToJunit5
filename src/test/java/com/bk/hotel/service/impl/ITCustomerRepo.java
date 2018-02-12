package com.bk.hotel.service.impl;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import com.bk.hotel.HotelApplication;
import com.bk.hotel.model.Customer;
import com.bk.hotel.repo.CustomerRepo;
import com.bk.hotel.service.CustomerService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HotelApplication.class)
@ContextConfiguration(initializers = ITCustomerRepo.Initializer.class)
public class ITCustomerRepo {

	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			EnvironmentTestUtils.addEnvironment("testcontainers", applicationContext.getEnvironment(),

					"spring.datasource.url=jdbc:tc:postgresql://localhost:" + postgres.getExposedPorts().get(0)
							+ "/test?TC_INITSCRIPT=init_customerdb.sql",
					"spring.datasource.username=" + postgres.getUsername(),
					"spring.datasource.password=" + postgres.getPassword(),
					"spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver");
		}
	}

	@ClassRule
	@SuppressWarnings("rawtypes")
	public static PostgreSQLContainer postgres = (PostgreSQLContainer) new PostgreSQLContainer();
	@Autowired
	private CustomerRepo repo;

	@MockBean
	private CustomerService customerService;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

	@Test
	public void testDatabaseCall() {
		Customer customer = repo.findById(1L).get();

		assertEquals("John", customer.getFirstName());
		assertEquals("Doe", customer.getLastName());
		assertEquals("Middle", customer.getMiddleName());
		assertEquals("", customer.getSuffix());
		assertEquals("2017-10-30", dateFormat.format(customer.getDateOfLastStay()));
	}
}
