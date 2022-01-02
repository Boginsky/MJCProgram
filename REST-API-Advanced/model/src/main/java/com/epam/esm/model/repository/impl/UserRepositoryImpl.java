package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.AbstractRepository;
import com.epam.esm.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class UserRepositoryImpl extends AbstractRepository<User> implements UserRepository {

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        super(entityManager, User.class);
    }
}
