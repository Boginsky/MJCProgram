package com.epam.esm.model.repository;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.Order;
import org.springframework.data.domain.Pageable;

public interface OrderRepository extends EntityRepository<Order> {

    CustomPage<Order> getAllByUserId(Long userId, Pageable pageable);
}
