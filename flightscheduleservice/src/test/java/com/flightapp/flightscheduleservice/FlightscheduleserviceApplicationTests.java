package com.flightapp.flightscheduleservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class FlightscheduleserviceApplicationTests {

	@Autowired
	private MockMvc mvc;
	@Test
	void contextLoads() {
	}

	private final String baseUrlFlightSchedule = "http://localhost:3070/api/v1/flight/schedules" ;
	@Test
	public void shouldReturnSchedule()
			throws Exception {

		String url = baseUrlFlightSchedule.concat("/scheduleByStn/")
				.concat("Delhi")
				.concat("/").concat("Mumbai");
		mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].scheduleId").isNotEmpty());

	}

	@Test
	public void shouldReturnScheduleNotFound()
			throws Exception {

		String url = baseUrlFlightSchedule.concat("/scheduleByStn/")
				.concat("Chennai")
				.concat("/").concat("Mumbai");
		mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> Assertions.assertEquals("FlightSchedule not found",
						result.getResponse().getContentAsString()));
	}

}
