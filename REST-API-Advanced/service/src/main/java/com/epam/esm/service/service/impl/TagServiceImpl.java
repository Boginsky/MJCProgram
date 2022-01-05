package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.TagRepository;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.TagService;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    @Qualifier("tagDtoConverter")
    private final DtoConverter<Tag, TagDto> tagDtoConverter;
    @Qualifier("tagValidator")
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, DtoConverter<Tag, TagDto> tagDtoConverter,
                          UserRepository userRepository, TagValidator tagValidator) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.tagDtoConverter = tagDtoConverter;
        this.tagValidator = tagValidator;
    }

    @Override
    public List<TagDto> getRoute(Long tagId, Integer page, Integer size) {
        if (tagId != null) {
            return getById(tagId);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    @Transactional
    public TagDto create(TagDto tagDto) {
        tagValidator.validateTagName(tagDto);
        Tag tag = tagDtoConverter.convertToEntity(tagDto);
        isExist(tag);
        tag = tagRepository.create(tag);
        return tagDtoConverter.convertToDto(tag);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Tag tag = isPresent(id);
        tagRepository.delete(tag);
    }

    @Override
    public TagDto update(TagDto tagDto) {
        tagValidator.validateTagName(tagDto);
        Tag tag = tagDtoConverter.convertToEntity(tagDto);
        tag = tagRepository.update(tag);
        return tagDtoConverter.convertToDto(tag);
    }

    @Override
    public List<TagDto> getById(Long id) {
        Tag tag = isPresent(id);
        List<TagDto> tagDtoList = new ArrayList<>();
        TagDto tagDto = tagDtoConverter.convertToDto(tag);
        tagDtoList.add(tagDto);
        return tagDtoList;
    }

    @Override
    public List<TagDto> getAll(Integer page, Integer size) {
        Pageable pageRequest;
        try {
            pageRequest = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        List<Tag> tagList = tagRepository.getAll(pageRequest);
        return tagList.stream()
                .map(tagDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BestTag getHighestCostTag(Long userId) {
        isPresentUser(userId);
        return isPresentBestTag(userId);
    }

    public void isExist(Tag tag) {
        if (tagRepository.getByName(tag.getName()).isPresent()) {
            throw new DuplicateEntityException("message.tag.existent");
        }
    }

    private Tag isPresent(Long id) {
        Optional<Tag> optionalTag = tagRepository.getByField("id", id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("message.tag.nonexistent");
        }
        return optionalTag.get();
    }

    private void isPresentUser(Long userId) {
        if (!userRepository.getByField("id", userId).isPresent()) {
            throw new NoSuchEntityException("message.user.missing");
        }
    }

    private BestTag isPresentBestTag(Long userId) {
        Optional<BestTag> bestTagOptional = tagRepository.getHighestCostTag(userId);
        if (!bestTagOptional.isPresent()) {
            throw new NoSuchEntityException("message.tag.missing");
        }
        return bestTagOptional.get();
    }
}