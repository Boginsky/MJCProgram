package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TagDao {

    void create(Tag tag);

    Optional<Tag> findByName(String name);

    void updateNameById(Map<String,Object> tagInfoForUpdate);

    void deleteById(Long id);

    void deleteByName(String name);

    List<Tag> getTagsByGiftCertificateId(Long giftCertificateId);

    Optional<Tag> findById(Long id);

    List<Tag> getAll();

}
