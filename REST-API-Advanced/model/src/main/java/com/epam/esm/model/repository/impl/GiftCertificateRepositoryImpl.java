package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.AbstractRepository;
import com.epam.esm.model.repository.GiftCertificateRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import java.util.List;

@Repository
@Transactional
public class GiftCertificateRepositoryImpl extends AbstractRepository<GiftCertificate> implements GiftCertificateRepository {


    public GiftCertificateRepositoryImpl(EntityManager entityManager) {
        super(entityManager, GiftCertificate.class);
    }

    @Override
    public List<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns, List<String> orderType, List<String> filterBy) {
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
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<GiftCertificate> getAllByTagNames(List<String> tagNames) {
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        SetJoin<GiftCertificate, Tag> join = root.joinSet("tags");
        query.select(join.getParent()).distinct(true);
        Predicate[] predicates = new Predicate[tagNames.size()];
        for (int i = 0; i < tagNames.size(); i++) {
            Predicate predicate = criteriaBuilder.like(join.get("name"), tagNames.get(i).concat("%"));
            predicates[i] = predicate;
        }
        query.where(predicates);
        return entityManager.createQuery(query).getResultList();
    }

}
