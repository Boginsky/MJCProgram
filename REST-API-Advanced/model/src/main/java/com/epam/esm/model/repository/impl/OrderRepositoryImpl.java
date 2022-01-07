package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.CustomPage;
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
    public CustomPage<Order> getAllByUserId(Long userId, Pageable pageable) {
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);

        Join<User, Order> userOrderJoin = root.join("user");
        Predicate joinIdPredicate = criteriaBuilder.equal(userOrderJoin.get("id"), userId);
        query.where(joinIdPredicate);

        int totalAmount = getTotalAmount(root,joinIdPredicate);

        return CustomPage.<Order>builder()
                .content(entityManager.createQuery(query)
                        .setFirstResult((int) pageable.getOffset())
                        .setMaxResults(pageable.getPageSize())
                        .getResultList())
                .amountOfPages(totalAmount / pageable.getPageSize())
                .currentPage(pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber())
                .firstPage(1)
                .lastPage(totalAmount / pageable.getPageSize())
                .pageSize(pageable.getPageSize())
                .build();
    }

    private int getTotalAmount(Root<Order> root,Predicate joinIdPredicate){
        CriteriaQuery<Long> query= criteriaBuilder.createQuery(Long.class);
        query.select(criteriaBuilder.count(root));
        query.where(joinIdPredicate);
        return entityManager.createQuery(query).getSingleResult().intValue();
    }
}
