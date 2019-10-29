package com.texo.raspberryawards.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.texo.raspberryawards.RaspberryAwardsApplication;
import com.texo.raspberryawards.integration.NomineeControllerIntegrationTest;
import com.texo.raspberryawards.model.Nominee;
import com.texo.raspberryawards.model.PrizeIntervals;
import com.texo.raspberryawards.service.NomineeService;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RaspberryAwardsApplication.class)
@Slf4j
public class NomineeServiceUnitTest {
	
	@Autowired
	private NomineeService nomineeService;
	
	@Test
	public void testGetPrizeIntervals() throws Exception {
		
		Nominee min1 = Nominee.builder()
				.producers("Test Producer Min")
				.studios("Test Studios Min")
				.year(1900)
				.winner(true)
				.title("Test Title Min 1")
				.build();
		
		Nominee min2 = Nominee.builder()
				.producers("Test Producer Min")
				.studios("Test Studios Min")
				.year(1901)
				.winner(true)
				.title("Test Title Min 2")
				.build();
		
		Nominee max1 = Nominee.builder()
				.producers("Test Producer Max")
				.studios("Test Studios Min")
				.year(1950)
				.winner(true)
				.title("Test Title Max 1")
				.build();
		
		Nominee max2 = Nominee.builder()
				.producers("Test Producer Max")
				.studios("Test Studios Max")
				.year(2000)
				.winner(true)
				.title("Test Title Max 2")
				.build();
		
		ArrayList<Nominee> nominees = new ArrayList<Nominee>();
		nominees.add(min1);
		nominees.add(min2);
		nominees.add(max1);
		nominees.add(max2);
		
		PrizeIntervals prizeIntervals = nomineeService.prizeIntervals(nominees);
		
		assertThat(prizeIntervals.getMin().getInterval()).isEqualTo(1);
		assertThat(prizeIntervals.getMin().getPreviousWin()).isEqualTo(1900);
		assertThat(prizeIntervals.getMin().getFollowingWin()).isEqualTo(1901);
		assertThat(prizeIntervals.getMin().getProducer()).isEqualTo("Test Producer Min");
		
		assertThat(prizeIntervals.getMax().getInterval()).isEqualTo(50);
		assertThat(prizeIntervals.getMax().getPreviousWin()).isEqualTo(1950);
		assertThat(prizeIntervals.getMax().getFollowingWin()).isEqualTo(2000);
		assertThat(prizeIntervals.getMax().getProducer()).isEqualTo("Test Producer Max");
	}

}
