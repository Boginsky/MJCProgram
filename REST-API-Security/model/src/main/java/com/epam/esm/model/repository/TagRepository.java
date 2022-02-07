package com.epam.esm.model.repository;

import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query(name = "findMostWidelyUsedTagWithHighestOrderCostByUserId", nativeQuery = true)
    Optional<BestTag> getHighestCostTagAndWidelyUsed(long userId);
}
