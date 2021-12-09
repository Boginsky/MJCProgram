package com.epam.esm.service.service;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface GiftCertificateService {

    void create(GiftCertificateDto giftCertificateDto) throws ServiceException;

    List<GiftCertificate> getAll();

    void deleteById(Long id) throws ServiceException;

    void deleteByName(String name) throws ServiceException;

    GiftCertificate findById(Long id) throws ServiceException;

    GiftCertificate findByName(String name) throws ServiceException;

    GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto) throws ServiceException;

    List<GiftCertificate> getGiftCertificateByTagId(Long tagId);

    List<GiftCertificate> getAllWithSorting(List<String> sortColumns, List<String> orderTypes);

    List<GiftCertificate> findWithFiltering(String name, String description);

    List<GiftCertificateDto> getAllByTagId(Long tagId) throws ServiceException;

}
