package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.BaseDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public class TagDaoImpl extends BaseDao<Tag> implements TagDao {

    public TagDaoImpl(RowMapper<Tag> rowMapper) {
        super(rowMapper);
    }

    @Override
    public void create(Tag tag) {

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
        return Optional.empty();
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
