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

import static com.epam.esm.model.constant.ColumnName.GIFT_CERTIFICATE_CREATE_TIME;
import static com.epam.esm.model.constant.ColumnName.GIFT_CERTIFICATE_DESCRIPTION;
import static com.epam.esm.model.constant.ColumnName.GIFT_CERTIFICATE_DURATION;
import static com.epam.esm.model.constant.ColumnName.GIFT_CERTIFICATE_ID;
import static com.epam.esm.model.constant.ColumnName.GIFT_CERTIFICATE_LAST_UPDATE_DATE;
import static com.epam.esm.model.constant.ColumnName.GIFT_CERTIFICATE_NAME;
import static com.epam.esm.model.constant.ColumnName.GIFT_CERTIFICATE_PRICE;
import static com.epam.esm.model.constant.Query.CREATE_GIFT_CERTIFICATE;
import static com.epam.esm.model.constant.Query.CREATE_GIFT_CERTIFICATE_TAG_REFERENCE;
import static com.epam.esm.model.constant.Query.DELETE_GIFT_CERTIFICATE_BY_ID;
import static com.epam.esm.model.constant.Query.DELETE_GIFT_CERTIFICATE_BY_NAME;
import static com.epam.esm.model.constant.Query.GET_ALL_GIFT_CERTIFICATES;
import static com.epam.esm.model.constant.Query.GET_GIFT_CERTIFICATE_BY_ID;
import static com.epam.esm.model.constant.Query.GET_GIFT_CERTIFICATE_BY_NAME;
import static com.epam.esm.model.constant.Query.GET_GIFT_CERTIFICATE_BY_TAG_NAME;
import static com.epam.esm.model.constant.Query.GET_TAG_IDS_BY_GIFT_CERTIFICATE_ID;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> rowMapper;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
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
        jdbcTemplate.update(CREATE_GIFT_CERTIFICATE, giftCertificate.getName()
                , giftCertificate.getDescription(), giftCertificate.getPrice()
                , giftCertificate.getDuration());
    }

    @Override
    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(GET_ALL_GIFT_CERTIFICATES, rowMapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_BY_ID, id);
    }

    @Override
    public Optional<GiftCertificate> getById(Long id) {
        return jdbcTemplate.query(GET_GIFT_CERTIFICATE_BY_ID, rowMapper, id).stream().findAny();
    }

    @Override
    public Optional<GiftCertificate> getByName(String name) {
        return jdbcTemplate.query(GET_GIFT_CERTIFICATE_BY_NAME, rowMapper, name).stream().findAny();
    }

    @Override
    public void updateById(Long giftCertificateId, Map<String, Object> giftCertificateInfoForUpdate) {
        List<Object> values = new ArrayList<>(giftCertificateInfoForUpdate.values());
        values.add(giftCertificateId);
        String query = new QueryBuildHelper()
                .buildQueryForUpdate(new ArrayList<>(giftCertificateInfoForUpdate.keySet()));
        jdbcTemplate.update(query, values.toArray());
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagName(String tagName) {
        return jdbcTemplate.query(GET_GIFT_CERTIFICATE_BY_TAG_NAME, rowMapper, tagName);
    }

    @Override
    public List<Long> getTagIdsByGiftCertificateId(Long certificateId) {
        return jdbcTemplate.query(GET_TAG_IDS_BY_GIFT_CERTIFICATE_ID,
                (resultSet, i) -> resultSet.getLong("tag.id"), certificateId);
    }

    public List<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns,
                                                               List<String> orderType,
                                                               List<String> filterBy) {
        String query = new QueryBuildHelper().buildSortingQuery(sortColumns,orderType,filterBy);
        return jdbcTemplate.query(query, rowMapper);
    }

    @Override
    public void createGiftCertificateTagReference(Long giftCertificateId, Long tagId) {
        jdbcTemplate.update(CREATE_GIFT_CERTIFICATE_TAG_REFERENCE, giftCertificateId, tagId);
    }


}
