package com.epam.esm.model.repository;

import com.epam.esm.model.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepository extends EntityRepository<Order> {

    List<Order> getAllByUserId(Long userId, Pageable pageable);
}
