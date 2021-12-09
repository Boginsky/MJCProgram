package com.epam.esm.service.service;

import com.epam.esm.model.constant.SortParamsContext;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface GiftCertificateService {

    void create(GiftCertificateDto giftCertificateDto) throws ServiceException;

    List<GiftCertificate> getAll();

    void deleteById(Long id) throws ServiceException;

    GiftCertificate findById(Long id) throws ServiceException;

    GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto) throws ServiceException;

    List<GiftCertificateDto> getAllWithTags();

    List<GiftCertificate> getAllWithSorting(SortParamsContext sortParamsContext);

    List<GiftCertificate> findWithFiltering(String name, String description);

    List<GiftCertificate> getAllByTagName(String tagName);

}
