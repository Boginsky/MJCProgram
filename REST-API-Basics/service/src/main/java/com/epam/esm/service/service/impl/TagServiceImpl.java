package com.epam.esm.service.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.TagService;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final Validator<Tag> tagValidator;
    private final DtoConverter<Tag, TagDto> tagDtoConverter;


    @Autowired
    public TagServiceImpl(TagDao tagDao, Validator<Tag> tagValidator,
                          DtoConverter<Tag, TagDto> tagDtoConverter) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
        this.tagDtoConverter = tagDtoConverter;
    }

    @Override
    public List<TagDto> getRoute(Long tagId, Long giftCertificateId,
                                 Integer page, Integer size) {
        if (tagId != null) {
            return getById(tagId);
        } else if (giftCertificateId != null) {
            return getTagsByGiftCertificateId(giftCertificateId);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    public BestTag getHighestCostTag() {
        return tagDao.getHighestCostTag().orElseThrow(() -> new NoSuchEntityException("message.tagDoesntExist"));
    }

    @Override
    public List<TagDto> getTagsByGiftCertificateId(Long giftCertificateId) {
        List<Tag> tagList = tagDao.getTagsByGiftCertificateId(giftCertificateId);
        return tagList.stream()
                .map(tagDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDto> getById(Long id) {
        Tag tag = tagDao.getById(id).orElseThrow(
                () -> new NoSuchEntityException("message.cantFindTagById"));
        List<TagDto> listOfTags = new ArrayList<>();
        listOfTags.add(tagDtoConverter.convertToDto(tag));
        return listOfTags;
    }

    @Override
    public List<TagDto> getAll(Integer page, Integer size) {
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        List<Tag> tagList = tagDao.getAll(pageRequest).getContent();
        return tagList.stream()
                .map(tagDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TagDto create(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new InvalidEntityException("message.tagInvalid");
        }
        isExists(tag);
        tag = createTag(tag);
        return tagDtoConverter.convertToDto(tag);
    }

    private Tag createTag(Tag tag) {
        Long id = tagDao.create(tag);
        return isPresent(id);
    }

    @Transactional
    @Override
    public TagDto updateNameById(Long id, Tag tag) {
        isPresent(id);
        isDuplicate(tag);
        tagDao.updateNameById(id, tag.getName());
        tag.setId(id);
        return tagDtoConverter.convertToDto(tag);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        isPresent(id);
        tagDao.deleteById(id);
    }

    private Tag isPresent(Long id) {
        Optional<Tag> tagOptional = tagDao.getById(id);
        if (!tagOptional.isPresent()) {
            throw new NoSuchEntityException("message.tagDoesntExist");
        } else {
            return tagOptional.get();
        }
    }

    private void isDuplicate(Tag tag) {
        if (tag.getName() != null) {
            if (tagDao.getByName(tag.getName()).isPresent()) {
                throw new DuplicateEntityException("message.tagExists");
            }
        } else {
            throw new InvalidEntityException("message.tagInvalid");
        }
    }

    private void isExists(Tag tag) {
        String tagName = tag.getName();
        boolean isTagExist = tagDao.getByName(tagName).isPresent();
        if (isTagExist) {
            throw new DuplicateEntityException("message.tagExists");
        }
    }
}
