package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.util.QueryBuildHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.model.constant.Query.CREATE_TAG;
import static com.epam.esm.model.constant.Query.DELETE_TAG_BY_ID;
import static com.epam.esm.model.constant.Query.GET_ALL_TAGS;
import static com.epam.esm.model.constant.Query.GET_COUNT_OF_ALL_TAGS;
import static com.epam.esm.model.constant.Query.GET_MOST_WILDLY_USED_TAG_WITH_HIGHEST_COST;
import static com.epam.esm.model.constant.Query.GET_TAGS_BY_GIFT_CERTIFICATE_ID;
import static com.epam.esm.model.constant.Query.GET_TAG_BY_ID;
import static com.epam.esm.model.constant.Query.GET_TAG_BY_NAME;
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
    public Optional<BestTag> getHighestCostTag() {
        RowMapper<BestTag> rowMapper = (resultSet, i) -> BestTag.builder()
                .tag(Tag.builder()
                        .id(resultSet.getLong(1))
                        .name(resultSet.getString(2))
                        .build())
                .totalPrice(resultSet.getBigDecimal(3))
                .build();
        return jdbcTemplate.query(GET_MOST_WILDLY_USED_TAG_WITH_HIGHEST_COST, rowMapper).stream().findAny();
    }

    @Override
    public Long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(CREATE_TAG, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateNameById(Long id, String name) {
        jdbcTemplate.update(UPDATE_TAG_BY_ID, name, id);
    }

    @Override
    public Optional<Tag> getByName(String value) {
        return jdbcTemplate.query(GET_TAG_BY_NAME, rowMapper, value).stream().findAny();
    }

    @Override
    public Optional<Tag> getById(Long id) {
        return jdbcTemplate.query(GET_TAG_BY_ID, rowMapper, id).stream().findAny();
    }

    @Override
    public Page<Tag> getAll(Pageable pageable) {
        Integer count = jdbcTemplate.queryForObject(GET_COUNT_OF_ALL_TAGS, Integer.class);
        String querySql = new QueryBuildHelper().buildPaginationQuery(GET_ALL_TAGS, pageable);
        List<Tag> tagList = jdbcTemplate.query(querySql, rowMapper);
        return new PageImpl<>(tagList, pageable, count);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_TAG_BY_ID, id);
    }

    @Override
    public List<Tag> getTagsByGiftCertificateId(Long giftCertificateId) {
        return jdbcTemplate.query(GET_TAGS_BY_GIFT_CERTIFICATE_ID, rowMapper, giftCertificateId);
    }
}
