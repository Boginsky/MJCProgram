package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.AbstractRepository;
import com.epam.esm.model.repository.TagRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Query;
import java.util.Optional;

@Repository
@Transactional
@NamedNativeQuery(name = "GET_MOST_WILDLY_USED_TAG_WITH_HIGHEST_COST",
        query = "SELECT tag.id, tag.name, MAX(orders.total_price) " +
                "as 'highestCost' FROM orders JOIN gift_certificate " +
                "ON orders.gift_certificate_id = gift_certificate.id " +
                "JOIN gift_certificate_has_tag " +
                "ON gift_certificate.id = gift_certificate_has_tag.gift_certificate_id " +
                "JOIN tag ON tag_id = tag.id GROUP BY tag.id " +
                "ORDER BY COUNT(tag.id) DESC, MAX(orders.total_price) DESC LIMIT 1")
public class TagRepositoryImpl extends AbstractRepository<Tag> implements TagRepository {

    public TagRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Tag.class);
    }

    @Override
    public Optional<Tag> getByName(String name) {
        return getByField("name", name);
    }

    @Override
    public Optional<BestTag> getHighestCostTag(Long userId) {
        Query query = entityManager.createNamedQuery("GET_MOST_WILDLY_USED_TAG_WITH_HIGHEST_COST", BestTag.class);
        query.setParameter("userId", userId);
        return criteriaBuilderHelper.getOptionalQueryResult(query);
    }
}
