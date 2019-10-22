package com.texo.raspberryawards.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.texo.raspberryawards.model.Nominee;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {
	List<Nominee> findByOrderByYearAsc();
}