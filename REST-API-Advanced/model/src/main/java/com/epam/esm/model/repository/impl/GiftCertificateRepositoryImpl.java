package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.CustomPage;
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
import java.util.List;

@Repository
@Transactional
public class GiftCertificateRepositoryImpl extends AbstractRepository<GiftCertificate> implements GiftCertificateRepository {


    public GiftCertificateRepositoryImpl(EntityManager entityManager) {
        super(entityManager, GiftCertificate.class);
    }

    @Override
    public CustomPage<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns, List<String> orderType,
                                                                     List<String> filterBy, Pageable pageable) {
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        addSort(sortColumns, orderType, query, root);
        addFilter(filterBy, query, root);
        int totalAmount = getTotalAmountForGetAllWithSortingAndFiltering(sortColumns, orderType, filterBy);
        return getGiftCertificateCustomPage(pageable, query, totalAmount);
    }

    @Override
    public CustomPage<GiftCertificate> getAllByTagNames(List<String> tagNames, Pageable pageable) {
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(root);
        Join<GiftCertificate, Tag> joinTag = root.join("tags");
        Predicate predicate = criteriaBuilderHelper.buildOrEqualPredicates(joinTag, "name", tagNames);
        query.where(criteriaBuilder.and(predicate));
        query.groupBy(root.get("id"));
        query.having(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.count(root), (long) tagNames.size()));
        int totalAmount = getTotalAmountForGetAllTagNames(tagNames);
        return getGiftCertificateCustomPage(pageable, query, totalAmount);
    }

    private int getTotalAmountForGetAllTagNames(List<String> tagNames) {
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(criteriaBuilder.count(root));
        Join<GiftCertificate,Tag> joinTag = root.join("tags");
        Predicate predicate = criteriaBuilderHelper.buildOrEqualPredicates(joinTag, "name", tagNames);
        query.where(criteriaBuilder.and(predicate));
        query.having(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.count(root), (long) tagNames.size()));
        return entityManager.createQuery(query).getSingleResult().intValue();
    }

    private int getTotalAmountForGetAllWithSortingAndFiltering(List<String> sortColumns, List<String> orderType, List<String> filterBy) {
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        query.select(criteriaBuilder.count(root));
        addSort(sortColumns, orderType, query, root);
        addFilter(filterBy, query, root);
        return entityManager.createQuery(query).getSingleResult().intValue();
    }

    private void addSort(List<String> sortColumns, List<String> orderType, CriteriaQuery query, Root<GiftCertificate> root) {
        if (sortColumns != null) {
            List<Order> orderList = criteriaBuilderHelper.buildOrderList(root, sortColumns, orderType);
            query.orderBy(orderList);
        }
    }

    private void addFilter(List<String> filterBy, CriteriaQuery query, Root<GiftCertificate> root) {
        if (filterBy != null) {
            List<Predicate> predicateList = criteriaBuilderHelper.buildFilterBy(root, filterBy);
            query.select(root).where(predicateList.toArray(new Predicate[0]));
        }
    }

    private CustomPage<GiftCertificate> getGiftCertificateCustomPage(Pageable pageable, CriteriaQuery<GiftCertificate> query, int totalAmount) {
        return CustomPage.<GiftCertificate>builder()
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
}

