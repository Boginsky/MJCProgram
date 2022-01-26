package com.epam.esm.model.repository;

import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;

import java.util.Optional;

public interface TagRepository extends EntityRepository<Tag> {

    Optional<Tag> getByName(String name);

    Optional<BestTag> getHighestCostTag(Long userId);

}
