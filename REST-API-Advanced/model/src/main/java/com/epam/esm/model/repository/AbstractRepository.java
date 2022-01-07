package com.epam.esm.model.repository;

import com.epam.esm.model.entity.ApplicationBaseEntity;
import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.util.QueryBuildHelper;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractRepository<T extends ApplicationBaseEntity> implements EntityRepository<T> {

    @PersistenceContext
    protected final EntityManager entityManager;

    protected final CriteriaBuilder criteriaBuilder;
    protected final Class<T> entityType;
    protected final QueryBuildHelper criteriaBuilderHelper;

    public AbstractRepository(EntityManager entityManager, Class<T> entityType) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.entityType = entityType;
        this.criteriaBuilderHelper = new QueryBuildHelper(this.criteriaBuilder);
    }

    @Override
    public T create(T entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        }
        return entityManager.merge(entity);
    }

    @Override
    public CustomPage<T> getAll(Pageable pageable) {
        CriteriaQuery<T> query = criteriaBuilder.createQuery(entityType);
        Root<T> root = query.from(entityType);
        query.select(root);
        int totalAmount = getTotalAmount();
        return CustomPage.<T>builder()
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

    @Override
    public Optional<T> getByField(String fieldName, Object value) {
        CriteriaQuery<T> query = criteriaBuilder.createQuery(entityType);
        Root<T> root = query.from(entityType);
        query.select(root);

        Predicate fieldPredicate = criteriaBuilder.equal(root.get(fieldName), value);
        query.where(fieldPredicate);

        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        return criteriaBuilderHelper.getOptionalQueryResult(typedQuery);
    }

    @Override
    public T update(T entity) {
        return create(entity);
    }

    @Override
    public void delete(T entity) {
        T mergedEntity = entityManager.merge(entity);
        entityManager.remove(mergedEntity);
    }

    private int getTotalAmount() {
        if (User.class.equals(entityType)) {
            return ((BigInteger) entityManager.createNativeQuery("SELECT COUNT(*) FROM user").getSingleResult()).intValue();
        } else if (GiftCertificate.class.equals(entityType)) {
            return ((BigInteger) entityManager.createNativeQuery("SELECT COUNT(*) FROM gift_certificate").getSingleResult()).intValue();
        } else if (Tag.class.equals(entityType)) {
            return ((BigInteger) entityManager.createNativeQuery("SELECT COUNT(*) FROM tag").getSingleResult()).intValue();
        } else if (Order.class.equals(entityType)) {
            return ((BigInteger) entityManager.createNativeQuery("SELECT COUNT(*) FROM orders").getSingleResult()).intValue();
        } else {
            return 0;
        }
    }
}
