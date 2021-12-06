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

    @Transactional
    @Override
    public void create(Tag tag) throws ServiceException {
        if (!tagValidator.isValid(tag)) {
            throw new ServiceException("Exception in service: tag is invalid");
        }
        String tagName = tag.getName();
        boolean isTagExist = tagDao.findByName(tagName).isPresent();
        if (isTagExist) {
            throw new ServiceException("Exception in service: tag already exists");
        }
        tagDao.create(tag);
    }

    @Override
    public Tag findByName(String name) throws ServiceException {
        Optional<Tag> optionalTag = tagDao.findByName(name);
        if (optionalTag.isPresent()) {
            return optionalTag.get();
        } else {
            throw new ServiceException("Exception in service: can't find tag by name");
        }
    }

    @Transactional
    @Override
    public void updateNameById(Long id, String name) throws ServiceException {
        if(!tagDao.findById(id).isPresent()){
            throw new ServiceException("Exception in service: can't update tag");
        }
        tagDao.updateNameById(id,name);
    }

    @Transactional
    @Override
    public void deleteById(Long id) throws ServiceException {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (!optionalTag.isPresent()) {
            throw new ServiceException("Exception in service: there is no such tag");
        }
        tagDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByName(String name) throws ServiceException {
        Optional<Tag> optionalTag = tagDao.findByName(name);
        if (!optionalTag.isPresent()) {
            throw new ServiceException("Exception in service: this is no such tag");
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
            throw new ServiceException("Exception in service: can't find tag by id");
        }
    }

    @Override
    public List<Tag> getAll() {
        return tagDao.getAll();
    }
}
