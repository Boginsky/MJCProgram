package com.epam.esm.model.repository;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    Page<GiftCertificate> findAllByTagsIn(List<Tag> tags, Pageable pageable);

    Page<GiftCertificate> findAllByNameContainingOrDescriptionContaining(String name, String address, Pageable pageable);

}
