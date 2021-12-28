package com.epam.esm.model.dao;

import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    Long create(Tag tag);

    Optional<Tag> getByName(String name);

    void updateNameById(Long id, String name);

    void deleteById(Long id);

    List<Tag> getTagsByGiftCertificateId(Long giftCertificateId);

    Optional<Tag> getById(Long id);

    Page<Tag> getAll(Pageable pageable);

    Optional<BestTag> getHighestCostTag();
}
