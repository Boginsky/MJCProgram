package com.epam.esm.model.repository;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GiftCertificateRepository extends EntityRepository<GiftCertificate> {

    CustomPage<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns, List<String> orderType,
                                                              List<String> filterBy, Pageable pageable);

    CustomPage<GiftCertificate> getAllByTagNames(List<String> tagName, Pageable pageable);
}
