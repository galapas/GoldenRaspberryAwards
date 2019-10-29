package com.texo.raspberryawards.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.texo.raspberryawards.RaspberryAwardsApplication;
import com.texo.raspberryawards.model.Nominee;
import com.texo.raspberryawards.model.PrizeInterval;
import com.texo.raspberryawards.model.PrizeIntervals;
import com.texo.raspberryawards.service.NomineeService;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class NomineeControllerIntegrationTest {
	
	private int port = 8181;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private HttpHeaders headers = new HttpHeaders();
	
	String URINominee = "/nominees";
	
	@Autowired
	private NomineeService nomineeService;
	
	@Test
	@Order(1)
	public void testGetIntervalsIsPrizeIntervals() throws Exception {
		String URI = URINominee+"/intervals";
		
		log.info("URICreateNominee: "+formFullURLWithPort(URI));
		
		log.info("nomineeService "+nomineeService.findByOrderByYearAsc().size());
		
		PrizeIntervals prizeIntervals = PrizeIntervals.builder()
				.min(PrizeInterval.builder()
						.interval(1)
						.previousWin(1990)
						.followingWin(1991)
						.producer("Joel Silver")
						.build()
						)
				.max(PrizeInterval.builder()
						.interval(35)
						.previousWin(1980)
						.followingWin(2015)
						.producer("Matthew Vaughn")
						.build())
				.build();
		
	    TestRestTemplate testRestTemplate = new TestRestTemplate();
	    ResponseEntity<PrizeIntervals> response = testRestTemplate.getForEntity(formFullURLWithPort(URI), PrizeIntervals.class);
	    
	    PrizeIntervals prizeIntervalsResponse = (PrizeIntervals) response.getBody();
	    //assertThat((PrizeIntervals) response.getBody()).isEqualTo((PrizeIntervals) prizeIntervals);
	    assertThat(prizeIntervalsResponse.toString()).isEqualTo(prizeIntervals.toString());
	}
	
	@Test
	public void testCreateNominee() throws Exception {
		Nominee nominee = Nominee.builder()
				.id(1L)
				.producers("Allan Carr")
				.studios("Associated Film Distribution")
				.title("Can't Stop the Music")
				.winner(false)
				.year(1980)
				.build();
		
	    String inputInJson = this.mapToJson(nominee);
		
		HttpEntity<Nominee> entity = new HttpEntity<Nominee>(nominee, headers);
		ResponseEntity<String> response = testRestTemplate.exchange(
				formFullURLWithPort(URINominee),
				HttpMethod.POST, entity, String.class);
		
		String responseInJson = response.getBody();
		assertThat(responseInJson).isEqualTo(inputInJson);
	}

	@Test
	public void testGetNomineeById() throws Exception {
		Nominee nominee = Nominee.builder()
				.id(1L)
				.producers("Allan Carr")
				.studios("Associated Film Distribution")
				.title("Can't Stop the Music")
				.winner(false)
				.year(1980)
				.build();
		
		String inputInJson = this.mapToJson(nominee);
		
		HttpEntity<Nominee> entity = new HttpEntity<Nominee>(nominee, headers);
		ResponseEntity<String> responseCreate = testRestTemplate.exchange(formFullURLWithPort(URINominee),
				HttpMethod.POST, entity, String.class);
		
		String URI = URINominee+"/1";

	    String bodyJsonResponse = testRestTemplate.getForObject(formFullURLWithPort(URI), String.class);
	    
		assertThat(bodyJsonResponse).isEqualTo(inputInJson);
	}
	
	@Test
	public void testGetPrizeIntervalsIsOK() throws Exception {
		String URI = URINominee+"/intervals";

	    TestRestTemplate testRestTemplate = new TestRestTemplate();
	    ResponseEntity<String> response = testRestTemplate.getForEntity(formFullURLWithPort(URI), String.class);
	    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}

	private String formFullURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}