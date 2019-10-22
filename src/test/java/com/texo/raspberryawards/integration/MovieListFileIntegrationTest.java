package com.texo.raspberryawards.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.texo.raspberryawards.RaspberryAwardsApplication;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RaspberryAwardsApplication.class)
@Slf4j
public class MovieListFileIntegrationTest {
	@Value("${source.file}")
    private String movieListFileName;
    
    @Value("${source.location}")
    private String folderLocation;
    
    @Test
	public void testMovieListFileExists() throws Exception {
    	
    	File file = new File(folderLocation+"/"+movieListFileName);
    	
	    assertThat(file.exists()).isEqualTo(true);
	}
}
