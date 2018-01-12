package com.bk.hotel.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParameterizedReservationServiceImplTest {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { "03/03/2018", "03/07/2018", new String[] {} },
				{ "", "11/27/2018", new String[] { "Must provide a check-in date." } },
				{ "", "", new String[] { "Must provide a check-in date.", "Must provide a check-out date." } },
				{ "02/30/2018", "03/07/2018", new String[] {
						"check-in date of: 02/30/2018 is not a valid date or does not match date format of: MM/DD/YYYY" } } });
	}

	private String startDate;
	private String endDate;
	private String[] errorMessages;

	public ParameterizedReservationServiceImplTest(String startDate, String endDate, String[] errorMessages) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.errorMessages = errorMessages;
	}

	@Test
	public void testVerifyReservationDates() {
		ReservationServiceImpl service = new ReservationServiceImpl();

		List<String> errorMessages = service.verifyReservationDates(startDate, endDate);

		assertThat(errorMessages).contains(this.errorMessages);
	}

}
