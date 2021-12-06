package com.epam.esm.service.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final Validator<GiftCertificate> giftCertificateValidator;
    private final Validator<Tag> tagValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      Validator<GiftCertificate> giftCertificateValidator, Validator<Tag> tagValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;

    }

    @Override
    @Transactional
    public void create(GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificate giftCertificate = giftCertificateDto.getGiftCertificate();
        validateGiftCertificate(giftCertificate);
        validateTags(giftCertificateDto.getCertificateTags());
        String giftCertificateName = giftCertificate.getName();
        boolean isCertificateExist = giftCertificateDao.findByName(giftCertificateName).isPresent();
        if (isCertificateExist) {
            throw new ServiceException("Exception in service: gift certificate already exists");
        }
        giftCertificateDao.create(giftCertificate);
        Long giftCertificateId = giftCertificateDao.findByName(giftCertificateName).map(GiftCertificate::getId).orElse(-1L);
        createGiftCertificateTagReference(giftCertificateDto.getCertificateTags(), giftCertificateId);
    }

    private void createGiftCertificateTagReference(List<Tag> tags, Long giftCertificateId) {
        for (Tag tag : tags) {
            String tagName = tag.getName();
            Optional<Tag> tagOptional = tagDao.findByName(tagName);
//            Tag fullTag =
        }


    }

    @Override
    public List<GiftCertificate> getAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public void updateById(Long giftCertificateId, Map<String, Object> giftCertificateInfoForUpdate) {

    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagId(Long tagId) {
        return null;
    }

    @Override
    public List<GiftCertificate> getAllWithSorting(List<String> sortColumns, List<String> orderTypes) {
        return null;
    }

    @Override
    public List<GiftCertificate> findWithFiltering(String name, String description) {
        return null;
    }

    private void validateGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        if (!giftCertificateValidator.isValid(giftCertificate)) {
            throw new ServiceException("Exception in service: invalid gift certificate");
        }
    }

    private void validateTags(List<Tag> tags) throws ServiceException {
        boolean isCorrectTags = tags.stream().allMatch(tagValidator::isValid);
        if (!isCorrectTags) {
            throw new ServiceException("Exception in service: invalid tag");
        }
    }
}
