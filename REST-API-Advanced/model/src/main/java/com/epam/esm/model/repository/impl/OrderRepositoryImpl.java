package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.AbstractRepository;
import com.epam.esm.model.repository.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class OrderRepositoryImpl extends AbstractRepository<Order> implements OrderRepository {

    public OrderRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Order.class);
    }

    @Override
    public List<Order> getAllByUserId(Long userId, Pageable pageable) {
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);

        Join<User, Order> userOrderJoin = root.join("user");
        Predicate joinIdPredicate = criteriaBuilder.equal(userOrderJoin.get("id"), userId);
        query.where(joinIdPredicate);

        return entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
