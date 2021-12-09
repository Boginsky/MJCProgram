package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.util.QueryBuildHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.esm.model.constant.ColumnName.*;
import static com.epam.esm.model.constant.Query.*;

@Repository
public class GiftCertificateImpl implements GiftCertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> rowMapper;

    @Autowired
    public GiftCertificateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new RowMapper<GiftCertificate>() {
            @Override
            public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
                return GiftCertificate.builder()
                        .setId(rs.getLong(GIFT_CERTIFICATE_ID))
                        .setName(rs.getString(GIFT_CERTIFICATE_NAME))
                        .setDescription(rs.getString(GIFT_CERTIFICATE_DESCRIPTION))
                        .setPrice(rs.getBigDecimal(GIFT_CERTIFICATE_PRICE))
                        .setDuration(rs.getInt(GIFT_CERTIFICATE_DURATION))
                        .setCreateTime(rs.getTimestamp(GIFT_CERTIFICATE_CREATE_TIME).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                        .setLastUpdateDate(rs.getTimestamp(GIFT_CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                        .build();
            }
        };
    }

    @Override
    public void create(GiftCertificate giftCertificate) {
        jdbcTemplate.update(CREATE_GIFT_CERTIFICATE,giftCertificate.getName()
                ,giftCertificate.getDescription(), giftCertificate.getPrice()
                ,giftCertificate.getDuration());
    }

    @Override
    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(GET_ALL_GIFT_CERTIFICATES, rowMapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_BY_ID,id);
    }

    @Override
    public void deleteByName(String name) {
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_BY_NAME, name);
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return jdbcTemplate.query(GET_GIFT_CERTIFICATE_BY_ID,rowMapper,id).stream().findAny();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return jdbcTemplate.query(GET_GIFT_CERTIFICATE_BY_NAME, rowMapper, name).stream().findAny();
    }

    // FIXME: 08.12.2021
    @Override
    public void updateById(Long giftCertificateId, Map<String, Object> giftCertificateInfoForUpdate) {
        List<Object> values = new ArrayList<>(giftCertificateInfoForUpdate.values());
        jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE_BY_ID,values.toArray(),giftCertificateId);
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagId(Long tagId) {
        return jdbcTemplate.query(GET_GIFT_CERTIFICATE_BY_TAG_ID,rowMapper,tagId);
    }

    @Override
    public List<Long> getTagIdsByGiftCertificateId(Long certificateId) {
        return jdbcTemplate.query(GET_TAG_IDS_BY_GIFT_CERTIFICATE_ID,
                (resultSet, i) -> resultSet.getLong("tag.id"),certificateId);
    }

    @Override
    public List<GiftCertificate> getAllWithSorting(List<String> sortColumns, List<String> orderTypes) {
        String query = new QueryBuildHelper().buildSortingQuery(sortColumns,orderTypes);
        return jdbcTemplate.query(query,rowMapper);
    }

    @Override
    public List<GiftCertificate> findWithFiltering(String name, String description) {
        String query = new QueryBuildHelper().buildFilteringQuery(name,description);
        return jdbcTemplate.query(query,rowMapper);
    }

    @Override
    public void createGiftCertificateTagReference(Long giftCertificateId, Long tagId) {
        jdbcTemplate.update(CREATE_GIFT_CERTIFICATE_TAG_REFERENCE,giftCertificateId,tagId);
    }


}
