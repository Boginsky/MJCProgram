package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.BaseDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.query.TagQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl extends BaseDao<Tag> implements TagDao {

    private static final String TABLE_NAME = "tags";
    private static final String COLUMN_NAME = "name";
    private static final RowMapper<Tag> ROW_MAPPER = new BeanPropertyRowMapper<>(Tag.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        super(ROW_MAPPER, TABLE_NAME, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Tag tag) {
        jdbcTemplate.update(TagQuery.CREATE_TAG, tag.getName());
    }

    @Override
    public List<Tag> getAll() {
        return null;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return findByColumn(COLUMN_NAME,name);
    }

    @Override
    public void updateNameById(String name, Long id) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public List<Tag> getTagsByGiftCertificateId(Long giftCertificateId) {
        return null;
    }
}
