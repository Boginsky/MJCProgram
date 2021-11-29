package com.epam.esm.model.dao;

import com.epam.esm.model.entity.AbstractEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Optional;

public abstract class BaseDao<T extends AbstractEntity> {

    protected final String findByColumnQuery;

    private final RowMapper<T> rowMapper;
    private final JdbcTemplate jdbcTemplate;

    public BaseDao(RowMapper<T> rowMapper, String tableName, JdbcTemplate jdbcTemplate) {
        this.rowMapper = rowMapper;
        this.jdbcTemplate = jdbcTemplate;

        findByColumnQuery = "SELECT tag_id,tag_name FROM " + tableName + " WHERE tag_id = ?";
    }

    public Optional<T> findByColumn(String columnName, String value){
        String query = String.format(findByColumnQuery,columnName);
        return jdbcTemplate.query(query,rowMapper,value).stream().findAny();
    }
}
