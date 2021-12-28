package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.util.QueryBuildHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.model.constant.Query.GET_ALL_USERS;
import static com.epam.esm.model.constant.Query.GET_BY_ID;
import static com.epam.esm.model.constant.Query.GET_COUNT_OF_ALL_USERS;

@Repository
public class UserDaoImpl implements UserDao {

    private final RowMapper<User> rowMapper;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new BeanPropertyRowMapper<>(User.class);
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        Integer count = jdbcTemplate.queryForObject(GET_COUNT_OF_ALL_USERS, Integer.class);
        String querySql = new QueryBuildHelper().buildPaginationQuery(GET_ALL_USERS, pageable);
        List<User> userList = jdbcTemplate.query(querySql, rowMapper);
        return new PageImpl<>(userList, pageable, count);
    }

    @Override
    public Optional<User> getById(Long id) {
        return jdbcTemplate.query(GET_BY_ID, rowMapper, id).stream().findAny();
    }
}
