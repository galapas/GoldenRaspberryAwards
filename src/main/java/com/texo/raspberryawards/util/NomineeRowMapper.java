package com.texo.raspberryawards.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.texo.raspberryawards.model.Nominee;

public class NomineeRowMapper implements RowMapper<Nominee> {
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";

    @Override
    public Nominee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Nominee.builder()
                .title("ROW MaPpEr")
                .build();
    }
}
