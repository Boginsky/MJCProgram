package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderDao {

    Long create(Order order);

    Page<Order> getAll(Pageable pageable);

    List<Order> getByUserId(Long userId);

    Optional<Order> getById(Long id);

    Map<Long, Long> getAllOrderIds();
}
