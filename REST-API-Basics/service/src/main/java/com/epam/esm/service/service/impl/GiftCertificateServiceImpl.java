package com.epam.esm.service.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.GiftCertificateValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final Validator<GiftCertificate> giftCertificateValidator;
    private final Validator<Tag> tagValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      Validator<GiftCertificate> giftCertificateValidator,
                                      Validator<Tag> tagValidator) {
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
        Long giftCertificateId = giftCertificateDao.findByName(giftCertificateName)
                .map(GiftCertificate::getId).orElse(-1L);
        createGiftCertificateTagReference(giftCertificateDto.getCertificateTags(), giftCertificateId);
    }

    private void createGiftCertificateTagReference(List<Tag> tags, Long giftCertificateId) {
        for (Tag tag : tags) {
            String tagName = tag.getName();
            Optional<Tag> tagOptional = tagDao.findByName(tagName);
            Tag fullTag = tagOptional.orElseGet(() -> createGiftCertificateTag(tag));
            Long tagId = fullTag.getId();
            giftCertificateDao.createGiftCertificateTagReference(giftCertificateId, tagId);
        }
    }

    private Tag createGiftCertificateTag(Tag tag) {
        tagDao.create(tag);
        return tagDao.findByName(tag.getName()).get();
    }

    @Override
    public List<GiftCertificate> getAll() {
        return giftCertificateDao.getAll();
    }

    @Transactional
    @Override
    public void deleteById(Long id) throws ServiceException {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(id);
        if (!giftCertificateOptional.isPresent()) {
            throw new ServiceException("Exception in service: can't delete gift certificate by id");
        }
        giftCertificateDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByName(String name) throws ServiceException {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findByName(name);
        if (!giftCertificateOptional.isPresent()) {
            throw new ServiceException("Exception in service: can't delete gift certificate by name");
        }
        giftCertificateDao.deleteByName(name);
    }

    @Override
    public GiftCertificate findById(Long id) throws ServiceException {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(id);
        if (!giftCertificateOptional.isPresent()) {
            throw new ServiceException("Exception in service: can't find gift certificate by id");
        }
        return giftCertificateOptional.get();
    }

    @Override
    public GiftCertificate findByName(String name) throws ServiceException {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findByName(name);
        if (!giftCertificateOptional.isPresent()) {
            throw new ServiceException("Exception in service: can't find gift certificate by name");
        }
        return giftCertificateOptional.get();
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagId(Long tagId) {
        return giftCertificateDao.getGiftCertificateByTagId(tagId);
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

    @Override
    public List<GiftCertificateDto> getAllByTagId(Long tagId) throws ServiceException {
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getGiftCertificateByTagId(tagId);
        Optional<Tag> tagOptional = tagDao.findById(tagId);
        if (!tagOptional.isPresent()) {
            throw new ServiceException("Exception in service: can't find gift certificate with tag by tag id");
        }
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tagOptional.get());
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        for (GiftCertificate giftCertificate : giftCertificateList) {
            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificate);
            giftCertificateDto.setCertificateTags(tagList);
            giftCertificateDtoList.add(giftCertificateDto);
        }
        return giftCertificateDtoList;
    }

    @Transactional
    @Override
    public GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto) throws ServiceException {
        GiftCertificate giftCertificate = giftCertificateDto.getGiftCertificate();
        if (giftCertificate != null) {
            if (!giftCertificateDao.findById(giftCertificateId).isPresent()) {
                throw new ServiceException("Exception in service: can't update gift certificate by id");
            }
            giftCertificateDao.updateById(giftCertificateId,updateInfo(giftCertificate));
        }
        List<Tag> tagList = giftCertificateDto.getCertificateTags();
        if(tagList != null){
            updateCertificateTags(tagList,giftCertificateId);
        }
        return buildGiftCertificateDto(giftCertificateDao.findById(giftCertificateId).get());
    }

    private Map<String, Object> updateInfo(GiftCertificate giftCertificate) throws ServiceException {
        Map<String, Object> updateInfo = new HashMap<>();
        GiftCertificateValidatorImpl giftCertificateValidator =
                (GiftCertificateValidatorImpl) this.giftCertificateValidator;
        String name = giftCertificate.getName();
        if(name != null){
            if(!giftCertificateValidator.isNameValid(name)){
                throw new ServiceException("Exception in service: can't update info, name invalid");
            }
            updateInfo.put("name",name);
        }
        String description = giftCertificate.getDescription();
        if(description != null){
            if(!giftCertificateValidator.isDescriptionValid(description)){
                throw new ServiceException("Exception in service: can't update info, description invalid");
            }
            updateInfo.put("description",description);
        }
        BigDecimal price = giftCertificate.getPrice();
        if(price != null){
            if(!giftCertificateValidator.isPriceValid(price)){
                throw new ServiceException("Exception in service: can't update info, price invalid");
            }
            updateInfo.put("price",price);
        }
        Integer duration = giftCertificate.getDuration();
        if(duration != null){
            if(!giftCertificateValidator.isDurationValid(duration)){
                throw new ServiceException("Exception in service: can't update info, duration invalid");
            }
            updateInfo.put("duration",duration);
        }
        return updateInfo;
    }

    private void updateCertificateTags(List<Tag> tagList, Long giftCertificateId){
        for(Tag tag : tagList){
            String tagName = tag.getName();
            Optional<Tag> tagOptional = tagDao.findByName(tagName);
            Tag fullTag = tagOptional.orElseGet(() -> createGiftCertificateTag(tag));
            Long tagId = fullTag.getId();
            if(!giftCertificateDao.getTagIdsByGiftCertificateId(giftCertificateId).contains(tagId)){
                giftCertificateDao.createGiftCertificateTagReference(giftCertificateId,tagId);
            }
        }
    }

    private GiftCertificateDto buildGiftCertificateDto (GiftCertificate giftCertificate){
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificate);
        List<Optional<Tag>> optionalTagList = new ArrayList<>();
        List<Long> tagIds = giftCertificateDao.getTagIdsByGiftCertificateId(giftCertificate.getId());
        tagIds.forEach(id -> optionalTagList.add(tagDao.findById(id)));
        optionalTagList.stream()
                .filter(Optional::isPresent)
                .forEach(tag -> giftCertificateDto.addTag(tag.get()));
        return giftCertificateDto;
    }



    @Override
    public List<GiftCertificate> getAllWithSorting(List<String> sortColumns, List<String> orderTypes) {
        return null;
    }

    @Override
    public List<GiftCertificate> findWithFiltering(String name, String description) {
        return null;
    }

}
