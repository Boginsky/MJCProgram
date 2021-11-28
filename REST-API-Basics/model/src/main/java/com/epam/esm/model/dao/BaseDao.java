package com.epam.esm.model.dao;

import com.epam.esm.model.entity.AbstractEntity;
import org.springframework.jdbc.core.RowMapper;

public abstract class BaseDao<T extends AbstractEntity> {

    private final RowMapper<T> rowMapper;

    public BaseDao(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }
}
