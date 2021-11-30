package com.epam.esm.service.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.epam.esm.service.service.TagService;
import com.epam.esm.service.validator.Validator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final Validator<Tag> tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, Validator<Tag> tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    // FIXME: 01.12.2021 rollback SQLException спросить
    @Transactional
    @Override
    public void create(Tag tag) throws ServiceException {
        if (!tagValidator.isValid(tag)) {
            throw new ServiceException("Exception in com.epam.esm.service.service: tag is invalid");
        }
        String tagName = tag.getName();
        boolean isTagExist = tagDao.findByName(tagName).isPresent();
        if (isTagExist) {
            throw new ServiceException("Exception in com.epam.esm.service.service: tag already exists");
        }
        tagDao.create(tag);
    }

    @Override
    public Tag findByName(String name) throws ServiceException {
        Optional<Tag> optionalTag = tagDao.findByName(name);
        if (optionalTag.isPresent()) {
            return optionalTag.get();
        } else {
            throw new ServiceException("Exception in com.epam.esm.service.service: can't find tag by name");
        }
    }

    @Override
    public void updateNameById(Map<String, Object> tagInfoForUpdate) {

    }

    @Transactional
    @Override
    public void deleteById(Long id) throws ServiceException {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (!optionalTag.isPresent()) {
            throw new ServiceException("Exception in com.epam.esm.service.service: there is no such tag");
        }
        tagDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByName(String name) throws ServiceException {
        Optional<Tag> optionalTag = tagDao.findByName(name);
        if (!optionalTag.isPresent()) {
            throw new ServiceException("Exception in com.epam.esm.service.service: this is no such tag");
        }
        tagDao.deleteByName(name);
    }

    @Override
    public List<Tag> getTagsByGiftCertificateId(Long giftCertificateId) {
        return tagDao.getTagsByGiftCertificateId(giftCertificateId);
    }

    @Override
    public Tag findById(Long id) throws ServiceException {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (optionalTag.isPresent()) {
            return optionalTag.get();
        } else {
            throw new ServiceException("Exception in com.epam.esm.service.service: can't find tag by id");
        }
    }

    @Override
    public List<Tag> getAll() {
        return tagDao.getAll();
    }
}
