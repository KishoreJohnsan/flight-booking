package com.flightapp.bookingservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookingserviceApplicationTests {

	@Autowired
	private MockMvc mvc;
	@Test
	void contextLoads() {
	}

	private final String baseUrlBooking = "http://localhost:3080/api/v1/flight/bookings";

	@Test
	public void shouldReturnBookingById()
			throws Exception {

		String url = baseUrlBooking.concat("/booking/").concat("1");
		mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.bookingId").isNotEmpty())
				.andExpect(jsonPath("$.bookingId").value(1));

	}

	@Test
	public void shouldReturnBookingByUser()
			throws Exception {

		String url = baseUrlBooking.concat("/booking/user/").concat("user");
		mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].bookingId").isNotEmpty());


	}

	@Test
	public void shouldReturnBookingNotFoundById()
			throws Exception {

		String url = baseUrlBooking.concat("/booking/").concat("5");
		mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> Assertions.assertEquals("Booking Not Found",
						result.getResponse().getContentAsString()));
	}

	@Test
	public void shouldReturnBookingNotFoundByUser()
			throws Exception {

		String url = baseUrlBooking.concat("/booking/user/").concat("user3");
		mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> Assertions.assertEquals("Booking Not Found",
						result.getResponse().getContentAsString()));
	}

}
