package com.epam.esm.model.util;

import lombok.NoArgsConstructor;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class QueryBuildHelper {

    private static final String BLANK_FOR_UPDATE_GIFT_CERTIFICATE_BY_ID = "UPDATE gift_certificate SET ";
    private static final String BLANK_FOR_GIFT_CERTIFICATE = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate";
    private static final String BLANK_FOR_GIFT_CERTIFICATE_WITH_MULTI_TAG_NAMES = "SELECT DISTINCT gift_certificate.id,gift_certificate.name,description, price, duration, create_date, last_update_date FROM gift_certificate JOIN gift_certificate_has_tag ON gift_certificate.id = gift_certificate_id JOIN tag ON tag_id = tag.id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    private CriteriaBuilder criteriaBuilder;

    public QueryBuildHelper(CriteriaBuilder criteriaBuilder) {
        this.criteriaBuilder = criteriaBuilder;
    }

    public <T> Optional<T> getOptionalQueryResult(Query query) {
        try {
            T entity = (T) query.getSingleResult();
            return Optional.of(entity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public <T> List<Order> buildOrderList(Root<T> root, List<String> sortColumns, List<String> orderTypes) {
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < sortColumns.size(); i++) {
            String column = sortColumns.get(i);
            String orderType;
            if (orderTypes.size() > i) {
                orderType = orderTypes.get(i);
            } else {
                orderType = "ASC";
            }
            Order order;
            if (orderType.equalsIgnoreCase("ASC")) {
                order = criteriaBuilder.asc(root.get(column));
            } else {
                order = criteriaBuilder.desc(root.get(column));
            }
            orderList.add(order);
        }
        return orderList;
    }

    public <T> List<Predicate> buildFilterBy(Root<T> root, List<String> filterBy) {
        List<Predicate> predicateList = new ArrayList<>(filterBy.size());
        for (int i = 0; i < filterBy.size(); i++) {
            if (i == 0) {
                Predicate predicateName = criteriaBuilder.like(root.get(NAME), filterBy.get(i).concat("%"));
                predicateList.add(predicateName);
            } else if (i == 1) {
                Predicate predicateDescription = criteriaBuilder.like(root.get(DESCRIPTION), filterBy.get(i).concat("%"));
                predicateList.add(predicateDescription);
            }
        }
        return predicateList;
    }
}
