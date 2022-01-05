package com.epam.esm.service.service;

import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> getAll(Integer page, Integer size);

    List<GiftCertificateDto> getRoute(List<String> tagName, List<String> sortColumns,
                                      List<String> orderType, List<String> filterBy,
                                      Long giftCertificateId, Integer page, Integer size);

    void deleteById(Long id);

    List<GiftCertificateDto> getById(Long id);

    GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> getAllWithSortingAndFiltering(List<String> sortColumns, List<String> orderType, List<String> filterBy,Integer page,Integer size);

    List<GiftCertificateDto> getAllByTagName(List<String> tagName,Integer page,Integer size);

}
