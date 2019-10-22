package com.texo.raspberryawards.util;

import org.springframework.stereotype.Component;

import com.texo.raspberryawards.model.Nominee;
import com.texo.raspberryawards.model.NomineeCSV;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CsvRecordToNomineeMapper {

    public Nominee convertAndTransform(NomineeCSV csvRecord) {
        final Nominee nominee = Nominee.builder()
        		.producers(csvRecord.getProducers().trim())
        		.studios(csvRecord.getStudios().trim())
        		.title(csvRecord.getTitle().trim())
        		.winner("yes".equals(csvRecord.getWinner().toLowerCase()))
        		.year(csvRecord.getYear())
                .build();
        log.info("Converting ({}) into ({})", csvRecord, nominee);
        return nominee;
    }
}
