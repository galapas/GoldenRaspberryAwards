package com.texo.raspberryawards.model;

import lombok.Data;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;


@CsvRecord(separator = ";", skipFirstLine = true)
@Data
public class NomineeCSV {
	
	@DataField(pos = 1, required = true, trim = true)
    private Integer year;
	
	@DataField(pos = 2, required = true, trim = true)
    private String title;
	
	@DataField(pos = 3, required = true, trim = true)
    private String studios;
	
	@DataField(pos = 4, required = true, trim = true)
    private String producers;
	
	@DataField(pos = 5, required = false, trim = true, defaultValue = "")
    private String winner;

    @Override
    public String toString() {
        return "[CSV Nominee:: Year: " + this.year+
                "; title: " + this.title + 
                "; studios: " + this.studios+ 
                "; producers: " + this.producers+
                "; winner: " + this.winner + 
                "]";
    }
}