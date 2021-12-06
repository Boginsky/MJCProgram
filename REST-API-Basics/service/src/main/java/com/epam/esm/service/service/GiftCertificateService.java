package com.epam.esm.service.service;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateService {

    void create(GiftCertificateDto giftCertificateDto) throws ServiceException;

    List<GiftCertificate> getAll();

    void deleteById(Long id);

    void deleteByName(String name);

    Optional<GiftCertificate> findById(Long id);

    Optional<GiftCertificate> findByName(String name);

    void updateById(Long giftCertificateId, Map<String, Object> giftCertificateInfoForUpdate);

    List<GiftCertificate> getGiftCertificateByTagId(Long tagId);

    List<GiftCertificate> getAllWithSorting(List<String> sortColumns, List<String> orderTypes);

    List<GiftCertificate> findWithFiltering(String name, String description);

}
