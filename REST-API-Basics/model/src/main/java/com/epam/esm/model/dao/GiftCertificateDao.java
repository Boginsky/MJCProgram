package com.epam.esm.model.dao;

import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateDao {

    Long create(GiftCertificate giftCertificate);

    void createGiftCertificateTagReference(Long giftCertificateId, Long tagId);

    Page<GiftCertificate> getAll(Pageable pageable);

    void deleteById(Long id);

    List<GiftCertificate> getAllByUserId(Long userId);

    Optional<GiftCertificate> getById(Long id);

    Optional<GiftCertificate> getByName(String name);

    void updateById(Long giftCertificateId, Map<String, Object> giftCertificateInfoForUpdate);

    List<GiftCertificate> getGiftCertificateByTagName(List<String> tagName);

    List<Long> getTagIdsByGiftCertificateId(Long giftCertificateId);

    List<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns,
                                                        List<String> orderType,
                                                        List<String> filterBy);


}
