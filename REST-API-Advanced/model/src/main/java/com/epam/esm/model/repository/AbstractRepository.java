package com.epam.esm.model.repository;

import com.epam.esm.model.entity.ApplicationBaseEntity;
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
    public List<T> getAll(Pageable pageable) {
        CriteriaQuery<T> query = criteriaBuilder.createQuery(entityType);
        Root<T> root = query.from(entityType);
        query.select(root);
        return entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
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
}
