package com.texo.raspberryawards.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.texo.raspberryawards.model.NomineeCSV;
import com.texo.raspberryawards.util.ArrayListAggregationStrategy;
import com.texo.raspberryawards.util.CsvRecordToNomineeMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MovieListCSVRoute extends RouteBuilder {

    private final CsvRecordToNomineeMapper mapper;

    @Value("${source.file}")
    private String movieListFileName;
    
    @Value("${source.location}")
    private String folderLocation;

    @Override
    public void configure() {
    	
        final BindyCsvDataFormat bindyCsvDataFormat = new BindyCsvDataFormat(NomineeCSV.class);
        bindyCsvDataFormat.setLocale("default");

        from("file:"+folderLocation+"?noop=true&recursive=false&fileName="+movieListFileName)
	        .transacted()
	        .unmarshal(bindyCsvDataFormat)
	        .split(body())
	        .streaming()
	        .bean(mapper, "convertAndTransform")
	        .aggregate(constant(true), new ArrayListAggregationStrategy())
	        .completionTimeout(10000)
	        .to("jpa:com.texo.raspberry.model.Nominee?entityType=java.util.List")
	        .end();
    }

}