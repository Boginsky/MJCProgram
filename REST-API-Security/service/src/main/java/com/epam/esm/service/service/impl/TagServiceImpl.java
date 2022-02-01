package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.TagRepository;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.TagService;
import com.epam.esm.service.util.CommonUtil;
import com.epam.esm.service.util.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    @Qualifier("tagDtoConverter")
    private final DtoConverter<Tag, TagDto> tagDtoConverter;
    @Qualifier("tagValidator")
    private final TagValidator tagValidator;
    private final CommonUtil commonUtil;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, DtoConverter<Tag, TagDto> tagDtoConverter,
                          UserRepository userRepository, TagValidator tagValidator,
                          CommonUtil commonUtil) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.tagDtoConverter = tagDtoConverter;
        this.tagValidator = tagValidator;
        this.commonUtil = commonUtil;
    }

    @Override
    public CustomPage<TagDto> getRoute(Long tagId, Integer page, Integer size) {
        if (tagId != null) {
            return getById(tagId);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    public TagDto create(TagDto tagDto) {
        tagValidator.validateTagName(tagDto);
        Tag tag = tagDtoConverter.convertToEntity(tagDto);
        isExist(tag);
        tag = tagRepository.save(tag);
        return tagDtoConverter.convertToDto(tag);
    }

    @Override
    public void deleteById(Long id) {
        Tag tag = isPresent(id);
        List<GiftCertificate> giftCertificateList = tag.getGiftCertificateList();
        for (GiftCertificate giftCertificate : giftCertificateList) {
            giftCertificate.getTags().remove(tag);
        }
        tagRepository.deleteById(tag.getId());
    }

    @Override
    public TagDto update(TagDto tagDto) {
        tagValidator.validateTagName(tagDto);
        Tag tag = tagDtoConverter.convertToEntity(tagDto);
        isExist(tag);
        tag = tagRepository.save(tag);
        return tagDtoConverter.convertToDto(tag);
    }

    @Override
    public CustomPage<TagDto> getById(Long id) {
        Tag tag = isPresent(id);
        List<TagDto> tagDtoList = new ArrayList<>();
        TagDto tagDto = tagDtoConverter.convertToDto(tag);
        tagDtoList.add(tagDto);
        return CustomPage.<TagDto>builder()
                .content(tagDtoList)
                .build();
    }

    @Override
    public CustomPage<TagDto> getAll(Integer page, Integer size) {
        Pageable pageRequest = commonUtil.getPageable(page, size, Sort.unsorted());
        Page<Tag> tagPage = tagRepository.findAll(pageRequest);
        return tagDtoConverter.convertContentToDto(tagPage);
    }

    @Override
    public BestTag getHighestCostTag(Long userId) {
        isPresentUser(userId);
        return isPresentBestTag(userId);
    }

    public void isExist(Tag tag) {
        if (tagRepository.findByName(tag.getName()).isPresent()) {
            throw new DuplicateEntityException("message.tag.existent");
        }
    }

    private Tag isPresent(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("message.tag.nonexistent");
        }
        return optionalTag.get();
    }

    private void isPresentUser(Long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new NoSuchEntityException("message.user.missing");
        }
    }

    private BestTag isPresentBestTag(Long userId) {
        Optional<BestTag> bestTagOptional = tagRepository.getHighestCostTagAndWidelyUsed(userId);
        if (!bestTagOptional.isPresent()) {
            throw new NoSuchEntityException("message.tag.missing");
        }
        return bestTagOptional.get();
    }
}