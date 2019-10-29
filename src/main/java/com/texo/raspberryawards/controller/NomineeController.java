package com.texo.raspberryawards.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.texo.raspberryawards.model.Nominee;
import com.texo.raspberryawards.model.PrizeInterval;
import com.texo.raspberryawards.model.PrizeIntervals;
import com.texo.raspberryawards.service.NomineeService;

@RestController
@RequestMapping("nominees")
public class NomineeController {

	@Autowired
	private NomineeService nomineeService;

	@PostMapping
	public Nominee save(@RequestBody Nominee nominee) {
		return nomineeService.save(nominee);
	}
	
	@PutMapping("{id}")
	public Nominee update(@PathVariable("id") Long id, @RequestBody Nominee nominee){
		nominee.setId(id);
		return nomineeService.update(nominee);
	}

	@GetMapping
	public List<Nominee> findAll() {
		return nomineeService.findAll();
	}

	@GetMapping("{id}")
	public Nominee find(@PathVariable("id") Long id) {
		return nomineeService.findById( id );
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Long id) {
		nomineeService.delete( id );
	}
	
	
	@GetMapping("/intervals")
	public PrizeIntervals prizeIntervals() {
		// BUSCA OS INDICADOS ORDENADOS POR ANO
		List<Nominee> nominees = nomineeService.findByOrderByYearAsc();
		
		return nomineeService.prizeIntervals(nominees);
	}

}