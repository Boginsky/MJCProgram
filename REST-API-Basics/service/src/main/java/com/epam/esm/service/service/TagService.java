package com.epam.esm.service.service;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Map;


public interface TagService {

    void create(Tag tag) throws ServiceException;

    Tag findByName(String name) throws ServiceException;

    void updateNameById(Long id, String name) throws ServiceException;

    void deleteById(Long id) throws ServiceException;

    void deleteByName(String name) throws ServiceException;

    List<Tag> getTagsByGiftCertificateId(Long giftCertificateId);

    Tag findById(Long id) throws ServiceException;

    List<Tag> getAll();
}
