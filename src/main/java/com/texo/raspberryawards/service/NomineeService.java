package com.texo.raspberryawards.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.texo.raspberryawards.model.Nominee;
import com.texo.raspberryawards.model.PrizeInterval;
import com.texo.raspberryawards.model.PrizeIntervals;
import com.texo.raspberryawards.repository.NomineeRepository;

@Service
public class NomineeService {
	@Autowired
	private NomineeRepository nomineeRepository;
	
	public Nominee save(Nominee nominee) {
		return nomineeRepository.save(nominee);
	}

	public Nominee findById(Long id) {
		return nomineeRepository.findById( id ).orElse( null );
	}

	public void delete(Long id) {
		nomineeRepository.deleteById( id );
	}

	public List<Nominee> findAll() {
		return nomineeRepository.findAll();
	}

	public Nominee update(Nominee nominee) {
		return nomineeRepository.save(nominee);
	}
	
	public List<Nominee> findByOrderByYearAsc(){
		return nomineeRepository.findByOrderByYearAsc();
	}
	
	
	public PrizeIntervals prizeIntervals(List<Nominee> nominees) {
		ArrayList<String> allProducers = new ArrayList<String>();
		
		// TRATA OS NOMES DOS PRODUTORES SEPARADOS POR ',' e 'and'
		for (Nominee nominee : nominees) {
			String[] producers = nominee.getProducers().split(", | and ");
			
			for (int i = 0; i < producers.length; i++) {
				
				if(!allProducers.contains(producers[i])) {
					allProducers.add(producers[i]);
				}
			}
		}
		
		// INICIA OS OBJETOS DE RETORNO APENAS COM VALORES MINIMO E MAXIMO
		PrizeInterval min = PrizeInterval.builder()
				.producer(null)
				.previousWin(null)
				.followingWin(null)
				.interval(Integer.MAX_VALUE)
				.build();
		
		PrizeInterval max = PrizeInterval.builder()
				.producer(null)
				.previousWin(null)
				.followingWin(null)
				.interval(Integer.MIN_VALUE)
				.build();
		
		
		// PARA CADA PRODUTOR BUSCA OS INDICADOS DE CADA ANO, SE O PRODUTOR FOI VENCEDOR CALCULA OS INTERVALOS ENTRE AS PREMIACOES
		for(String producer : allProducers) {
			Integer minInterval = Integer.MAX_VALUE;
			Integer maxInterval = Integer.MIN_VALUE;
			Integer previousYear = null;
			Integer followingYear = null;
			
			for(Nominee nominee : nominees){
				if(nominee.getWinner() == true && nominee.getProducers().contains(producer)){
					if(previousYear == null) {
						previousYear = nominee.getYear();
					}
						
					followingYear = nominee.getYear();
					
					Integer interval = followingYear - previousYear;
					
					maxInterval = Math.max(maxInterval, interval);
					
					if(interval > 0) {
						minInterval = Math.min(minInterval, interval);
					}
				}
			}
			
			// COMPARA OS INTERVALOS MINIMO E MÁXIMO ENTRE AS PREMIACOES DESTE PRODUTOR COM OS INTERVALOS DOS PRODUTORES JÁ PROCESSADOS
			if(min == null || (minInterval < min.getInterval() && minInterval > 0)) {
				min = PrizeInterval.builder()
						.producer(producer)
						.previousWin(previousYear)
						.followingWin(followingYear)
						.interval(minInterval)
						.build();
			}
			
			if(max == null || maxInterval > max.getInterval()) {
				max = PrizeInterval.builder()
						.producer(producer)
						.previousWin(previousYear)
						.followingWin(followingYear)
						.interval(maxInterval)
						.build();
			}
			
		}
		
		// MONTA O OBJETO DE RETORNO
		PrizeIntervals intervals = PrizeIntervals.builder()
				.max(max)
				.min(min)
				.build();
		
		return intervals;
	}
	
}