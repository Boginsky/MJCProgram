package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.AbstractRepository;
import com.epam.esm.model.repository.TagRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

import static com.epam.esm.model.query.Query.GET_MOST_WILDLY_USED_TAG_WITH_HIGHEST_COST;

@Repository
@Transactional
public class TagRepositoryImpl extends AbstractRepository<Tag> implements TagRepository {
    private static final String BEST_TAG_MAPPING_NAME = "BestTagMapping";

    public TagRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Tag.class);
    }

    @Override
    public Optional<Tag> getByName(String name) {
        return getByField("name", name);
    }

    @Override
    public Optional<BestTag> getHighestCostTag(Long userId) {
        Query query = entityManager.createNativeQuery(GET_MOST_WILDLY_USED_TAG_WITH_HIGHEST_COST, BEST_TAG_MAPPING_NAME);
        query.setParameter("userId", userId);
        return criteriaBuilderHelper.getOptionalQueryResult(query);
    }
}







