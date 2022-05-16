package com.flightapp.airlineservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AirlineserviceApplicationTests {

	@Autowired
	private MockMvc mvc;
	private final String baseUrlAirline = "http://localhost:3060/api/v1/flight/airlines";

	@Test
	void contextLoads() {
	}

	@Test
	public void shouldReturnAirlineList()
			throws Exception {

		String url = baseUrlAirline.concat("/airline");
		mvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].airlineId").isNotEmpty());
	}

}
