package com.epam.esm.model.dao;

import com.epam.esm.model.constant.SortParamsContext;
import com.epam.esm.model.entity.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateDao {

    void create(GiftCertificate giftCertificate);

    void createGiftCertificateTagReference(Long giftCertificateId, Long tagId);

    List<GiftCertificate> getAll();

    void deleteById(Long id);

    void deleteByName(String name);

    Optional<GiftCertificate> findById(Long id);

    Optional<GiftCertificate> findByName(String name);

    void updateById(Long giftCertificateId, Map<String, Object> giftCertificateInfoForUpdate);

    List<GiftCertificate> getGiftCertificateByTagName(String tagName);

    List<Long> getTagIdsByGiftCertificateId(Long giftCertificateId);

    List<GiftCertificate> getAllWithSorting(SortParamsContext sortParamsContext);

    List<GiftCertificate> findWithFiltering(String name, String description);

}
