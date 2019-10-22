package com.texo.raspberryawards.model;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PrizeInterval {
	private String producer;
	private Integer interval; 
	private Integer previousWin;
	private Integer followingWin;
}
