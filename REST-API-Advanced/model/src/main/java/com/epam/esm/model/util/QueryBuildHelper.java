package com.epam.esm.model.util;

import lombok.NoArgsConstructor;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class QueryBuildHelper {

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

    public <T> Predicate buildOrEqualPredicates(Path<T> root, String columnName, List<?> values) {
        int counter = 0;
        Predicate predicate = null;
        for (Object value : values) {
            Predicate currentPredicate = criteriaBuilder.equal(root.get(columnName), value);
            if (counter++ == 0) {
                predicate = currentPredicate;
            } else {
                predicate = criteriaBuilder.or(predicate, currentPredicate);
            }
        }
        return predicate;
    }

    public Predicate buildAndPredicates(List<Predicate> predicates) {
        if (predicates == null || predicates.isEmpty()) {
            return null;
        }
        Predicate resultPredicate = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            resultPredicate = criteriaBuilder.and(resultPredicate, predicates.get(i));
        }
        return resultPredicate;
    }

}
