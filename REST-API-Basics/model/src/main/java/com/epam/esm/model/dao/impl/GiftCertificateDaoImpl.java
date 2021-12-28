package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.entity.GiftCertificate;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.esm.model.constant.Query.CREATE_GIFT_CERTIFICATE;
import static com.epam.esm.model.constant.Query.CREATE_GIFT_CERTIFICATE_TAG_REFERENCE;
import static com.epam.esm.model.constant.Query.DELETE_GIFT_CERTIFICATE_BY_ID;
import static com.epam.esm.model.constant.Query.GET_ALL_GIFT_CERTIFICATES;
import static com.epam.esm.model.constant.Query.GET_COUNT_OF_ALL_GIFT_CERTIFICATES;
import static com.epam.esm.model.constant.Query.GET_GIFT_CERTIFICATES_BY_USER_ID;
import static com.epam.esm.model.constant.Query.GET_GIFT_CERTIFICATE_BY_ID;
import static com.epam.esm.model.constant.Query.GET_GIFT_CERTIFICATE_BY_NAME;
import static com.epam.esm.model.constant.Query.GET_TAG_IDS_BY_GIFT_CERTIFICATE_ID;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> rowMapper;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new BeanPropertyRowMapper<>(GiftCertificate.class);
    }

    @Override
    public Long create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(CREATE_GIFT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, giftCertificate.getPrice());
            ps.setInt(4, giftCertificate.getDuration());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Page<GiftCertificate> getAll(Pageable pageable) {
        Integer count = jdbcTemplate.queryForObject(GET_COUNT_OF_ALL_GIFT_CERTIFICATES, Integer.class);
        String querySql = new QueryBuildHelper().buildPaginationQuery(GET_ALL_GIFT_CERTIFICATES, pageable);
        List<GiftCertificate> giftCertificateList = jdbcTemplate.query(querySql, rowMapper);
        return new PageImpl<>(giftCertificateList, pageable, count);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_BY_ID, id);
    }

    @Override
    public List<GiftCertificate> getAllByUserId(Long userId) {
        return jdbcTemplate.query(GET_GIFT_CERTIFICATES_BY_USER_ID, rowMapper, userId);
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
    public List<GiftCertificate> getGiftCertificateByTagName(List<String> tagNameList) {
        String query = new QueryBuildHelper().buildQueryWithMultiTagNames(tagNameList);
        return jdbcTemplate.query(query, rowMapper);
    }

    @Override
    public List<Long> getTagIdsByGiftCertificateId(Long certificateId) {
        return jdbcTemplate.query(GET_TAG_IDS_BY_GIFT_CERTIFICATE_ID,
                (resultSet, i) -> resultSet.getLong("tag.id"), certificateId);
    }

    public List<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns,
                                                               List<String> orderType,
                                                               List<String> filterBy) {
        String query = new QueryBuildHelper().buildSortingQuery(sortColumns, orderType, filterBy);
        return jdbcTemplate.query(query, rowMapper);
    }

    @Override
    public void createGiftCertificateTagReference(Long giftCertificateId, Long tagId) {
        jdbcTemplate.update(CREATE_GIFT_CERTIFICATE_TAG_REFERENCE, giftCertificateId, tagId);
    }


}
