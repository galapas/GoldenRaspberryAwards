package com.texo.raspberryawards.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.texo.raspberryawards.RaspberryAwardsApplication;
import com.texo.raspberryawards.model.Nominee;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RaspberryAwardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class NomineeControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private HttpHeaders headers = new HttpHeaders();
	
	String URICreateNominee = "/nominees";
	
	@Test
	public void testCreateNominee() throws Exception {
		Nominee nominee = Nominee.builder()
				.id(1L)
				.producers("Allan Carr")
				.studios("Associated Film Distribution")
				.title("Can't Stop the Music")
				.winner(true)
				.year(1980)
				.build();
		
	    String inputInJson = this.mapToJson(nominee);
		
		HttpEntity<Nominee> entity = new HttpEntity<Nominee>(nominee, headers);
		ResponseEntity<String> response = testRestTemplate.exchange(
				formFullURLWithPort(URICreateNominee),
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
				.winner(true)
				.year(1980)
				.build();
		
		String inputInJson = this.mapToJson(nominee);
		
		HttpEntity<Nominee> entity = new HttpEntity<Nominee>(nominee, headers);
		ResponseEntity<String> responseCreate = testRestTemplate.exchange(formFullURLWithPort(URICreateNominee),
				HttpMethod.POST, entity, String.class);
		
		String URI = URICreateNominee+"/1";

	    String bodyJsonResponse = testRestTemplate.getForObject(formFullURLWithPort(URI), String.class);
	    
		assertThat(bodyJsonResponse).isEqualTo(inputInJson);
	}
	
	@Test
	public void testGetPrizeIntervals() throws Exception {
		String URI = URICreateNominee+"/intervals";

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