package com.epam.esm.model.dao;

import com.epam.esm.model.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao {

    void create (GiftCertificate giftCertificate);

    void createGiftCertificateTagReference(GiftCertificate giftCertificate, Long tagId);

    List<GiftCertificate> getAll();

    void deleteById(Long id);

    void deleteByName(String name);

    void findById(Long id);

    void findByName(String name);

    void updateById(GiftCertificate giftCertificate, Long giftCertificateId);

    List<GiftCertificate> getGiftCertificateByTagId(Long tagId);

    // FIXME: 28.11.2021 add methods with sorting
}
