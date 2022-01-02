package com.epam.esm.model.repository;

import com.epam.esm.model.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateRepository extends EntityRepository<GiftCertificate> {

    List<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns,
                                                        List<String> orderType,
                                                        List<String> filterBy);

    List<GiftCertificate> getAllByTagNames(List<String> tagName);
}
