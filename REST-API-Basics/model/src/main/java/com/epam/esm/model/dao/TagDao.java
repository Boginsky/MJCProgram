package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    void create(Tag tag);

    List<Tag> getAll();

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);

    void updateNameById(String name,Long id);

    void deleteById(Long id);

    void deleteByName(String name);

    List<Tag> getTagsByGiftCertificateId(Long giftCertificateId);

}
