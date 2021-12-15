package com.epam.esm.model.dao.impl;

import com.epam.esm.model.constant.Query;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.model.constant.Query.DELETE_TAG_BY_ID;
import static com.epam.esm.model.constant.Query.FIND_TAG_BY_ID;
import static com.epam.esm.model.constant.Query.FIND_TAG_BY_NAME;
import static com.epam.esm.model.constant.Query.GET_ALL_TAGS;
import static com.epam.esm.model.constant.Query.GET_TAGS_BY_GIFT_CERTIFICATE_ID;
import static com.epam.esm.model.constant.Query.UPDATE_TAG_BY_ID;


@Repository
public class TagDaoImpl implements TagDao {

    private final RowMapper<Tag> rowMapper;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new BeanPropertyRowMapper<>(Tag.class);
    }

    @Override
    public void create(Tag tag) {
        jdbcTemplate.update(Query.CREATE_TAG, tag.getName());
    }


    @Override
    public Optional<Tag> getByName(String value) {
        return jdbcTemplate.query(FIND_TAG_BY_NAME, rowMapper, value).stream().findAny();
    }

    @Override
    public Optional<Tag> getById(Long id) {
        return jdbcTemplate.query(FIND_TAG_BY_ID, rowMapper, id).stream().findAny();
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL_TAGS, rowMapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_TAG_BY_ID, id);
    }

    @Override
    public List<Tag> getTagsByGiftCertificateId(Long giftCertificateId) {
        return jdbcTemplate.query(GET_TAGS_BY_GIFT_CERTIFICATE_ID, rowMapper, giftCertificateId);
    }

    @Override
    public void updateNameById(Long id, String name) {
        jdbcTemplate.update(UPDATE_TAG_BY_ID, name, id);
    }
}
