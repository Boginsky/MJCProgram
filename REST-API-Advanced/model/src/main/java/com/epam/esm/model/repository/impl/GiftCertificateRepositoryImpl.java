package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.AbstractRepository;
import com.epam.esm.model.repository.GiftCertificateRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class GiftCertificateRepositoryImpl extends AbstractRepository<GiftCertificate> implements GiftCertificateRepository {


    public GiftCertificateRepositoryImpl(EntityManager entityManager) {
        super(entityManager, GiftCertificate.class);
    }

    @Override
    public List<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns, List<String> orderType,
                                                               List<String> filterBy, Pageable pageable) {
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);

        if (sortColumns != null) {
            List<Order> orderList = criteriaBuilderHelper.buildOrderList(root, sortColumns, orderType);
            query.orderBy(orderList);
        }
        if (filterBy != null) {
            List<Predicate> predicateList = criteriaBuilderHelper.buildFilterBy(root, filterBy);
            query.select(root).where(predicateList.toArray(new Predicate[0]));
        }
        return entityManager.createQuery(query)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<GiftCertificate> getAllByTagNames(List<String> tagNames, Pageable pageable) {
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        Join<GiftCertificate, Tag> joinTag = root.join("tags");
        Predicate predicate = criteriaBuilderHelper.buildOrEqualPredicates(joinTag, "name", tagNames);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(predicate);
        if (!predicates.isEmpty()) {
            query.where(criteriaBuilderHelper.buildAndPredicates(predicates));
            if (tagNames != null) {
                query.groupBy(root.get("id"));
                query.having(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.count(root), (long) tagNames.size()));
            }
        }
        return entityManager.createQuery(query)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}

