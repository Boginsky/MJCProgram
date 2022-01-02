package com.epam.esm.model.repository;

import com.epam.esm.model.entity.ApplicationBaseEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EntityRepository<T extends ApplicationBaseEntity> {

    T create(T entity);

    List<T> getAll(Pageable pageable);

    Optional<T> getByField(String fieldName, Object value);

    T update(T entity);

    void delete(T entity);

}
