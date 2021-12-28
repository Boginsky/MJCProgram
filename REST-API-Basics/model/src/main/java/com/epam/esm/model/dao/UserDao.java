package com.epam.esm.model.dao;

import com.epam.esm.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserDao {

    Page<User> getAll(Pageable pageable);

    Optional<User> getById(Long id);
}
