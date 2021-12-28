package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.util.QueryBuildHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.HashMap;
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
import static com.epam.esm.model.constant.ColumnName.ORDER_DATE_OF_PURCHASE;
import static com.epam.esm.model.constant.ColumnName.ORDER_TOTAL_PRICE;
import static com.epam.esm.model.constant.ColumnName.USER_FIRST_NAME;
import static com.epam.esm.model.constant.ColumnName.USER_ID;
import static com.epam.esm.model.constant.ColumnName.USER_LAST_NAME;
import static com.epam.esm.model.constant.Query.CREATE_ORDER;
import static com.epam.esm.model.constant.Query.GET_ALL_ORDERS;
import static com.epam.esm.model.constant.Query.GET_ALL_ORDERS_BY_ID;
import static com.epam.esm.model.constant.Query.GET_ALL_ORDERS_BY_USER_ID;
import static com.epam.esm.model.constant.Query.GET_ALL_ORDER_IDS;
import static com.epam.esm.model.constant.Query.GET_COUNT_OF_ALL_ORDERS;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final RowMapper<Order> rowMapper;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = (rs, i) -> Order.builder()
                .id(rs.getLong(4))
                .dateOfPurchase(rs.getTimestamp(ORDER_DATE_OF_PURCHASE).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                .totalPrice(rs.getBigDecimal(ORDER_TOTAL_PRICE))
                .giftCertificate(GiftCertificate.builder()
                        .id(rs.getLong(7))
                        .name(rs.getString(GIFT_CERTIFICATE_NAME))
                        .description(rs.getString(GIFT_CERTIFICATE_DESCRIPTION))
                        .price(rs.getBigDecimal(GIFT_CERTIFICATE_PRICE))
                        .duration(rs.getInt(GIFT_CERTIFICATE_DURATION))
                        .createDate(rs.getTimestamp(GIFT_CERTIFICATE_CREATE_TIME).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                        .lastUpdateDate(rs.getTimestamp(GIFT_CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                        .build())
                .user(User.builder()
                        .id(rs.getLong(1))
                        .firstName(rs.getString(USER_FIRST_NAME))
                        .lastName(rs.getString(USER_LAST_NAME))
                        .build())
                .build();
    }

    @Override
    public Long create(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            ps.setBigDecimal(1, order.getTotalPrice());
            ps.setLong(2, order.getGiftCertificate().getId());
            ps.setLong(3, order.getUser().getId());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Page<Order> getAll(Pageable pageable) {
        Integer count = jdbcTemplate.queryForObject(GET_COUNT_OF_ALL_ORDERS, Integer.class);
        String querySql = new QueryBuildHelper().buildPaginationQuery(GET_ALL_ORDERS, pageable);
        List<Order> orderList = jdbcTemplate.query(querySql, rowMapper);
        return new PageImpl<>(orderList, pageable, count);
    }

    @Override
    public List<Order> getByUserId(Long userId) {
        return jdbcTemplate.query(GET_ALL_ORDERS_BY_USER_ID, rowMapper, userId);
    }

    @Override
    public Optional<Order> getById(Long id) {
        return jdbcTemplate.query(GET_ALL_ORDERS_BY_ID, rowMapper, id).stream().findAny();
    }

    @Override
    public Map<Long, Long> getAllOrderIds() {
        Map<Long, Long> map = new HashMap<>();
        jdbcTemplate.query(GET_ALL_ORDER_IDS, (ResultSetExtractor<Map>) resultSet -> {
            while (resultSet.next()) {
                map.put(resultSet.getLong(GIFT_CERTIFICATE_ID), resultSet.getLong(USER_ID));
            }
            return map;
        });
        return map;
    }
}
