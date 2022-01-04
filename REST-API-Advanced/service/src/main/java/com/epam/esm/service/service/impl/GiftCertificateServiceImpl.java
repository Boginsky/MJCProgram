package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.TagRepository;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final TagRepository tagRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    @Qualifier("tagDtoConverter")
    private final DtoConverter<Tag, TagDto> tagDtoConverter;
    @Qualifier("giftCertificateConverter")
    private final DtoConverter<GiftCertificate, GiftCertificateDto> giftCertificateDtoConverter;
    private final GiftCertificateValidator customValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository,
                                      DtoConverter<GiftCertificate, GiftCertificateDto> giftCertificateDtoConverter,
                                      DtoConverter<Tag, TagDto> tagDtoConverter, GiftCertificateValidator customValidator) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.giftCertificateDtoConverter = giftCertificateDtoConverter;
        this.tagDtoConverter = tagDtoConverter;
        this.customValidator = customValidator;
    }

    @Override
    public List<GiftCertificateDto> getRoute(List<String> tagName, List<String> sortColumns,
                                             List<String> orderType, List<String> filterBy,
                                             Long giftCertificateId, Integer page, Integer size) {
        if (giftCertificateId != null) {
            return getById(giftCertificateId);
        } else if (sortColumns != null || filterBy != null) {
            return getAllWithSortingAndFiltering(sortColumns, orderType, filterBy);
        } else if (tagName != null) {
            return getAllByTagName(tagName);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        GiftCertificate giftCertificate = isPresent(id);
        giftCertificateRepository.delete(giftCertificate);
    }

    @Override
    public List<GiftCertificateDto> getById(Long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoConverter.convertToDto(isPresent(id));
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        giftCertificateDtoList.add(giftCertificateDto);
        return giftCertificateDtoList;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        validateFields(giftCertificateDto);
        Set<TagDto> tagSet = new HashSet<>();
        if (giftCertificateDto.getCertificateTags() != null) {
            for (TagDto tag : giftCertificateDto.getCertificateTags()) {
                Optional<Tag> tagOptional = tagRepository.getByName(tag.getName());
                if (tagOptional.isPresent()) {
                    tagSet.add(tagDtoConverter.convertToDto(tagOptional.get()));
                } else {
                    tagSet.add(tag);
                }
            }
        }
        giftCertificateDto.setCertificateTags(tagSet);
        GiftCertificate giftCertificate = giftCertificateRepository
                .create(giftCertificateDtoConverter
                        .convertToEntity(giftCertificateDto));
        return giftCertificateDtoConverter.convertToDto(giftCertificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto) {
        GiftCertificate sourceCertificate = isPresent(giftCertificateId);
        validateFields(giftCertificateDto);
        setUpdatedFields(sourceCertificate, giftCertificateDto);
        if (giftCertificateDto.getCertificateTags() != null) {
            Set<TagDto> tags = giftCertificateDto.getCertificateTags();
            sourceCertificate.setTags(saveTags(tags));
        }
        GiftCertificate giftCertificate = giftCertificateRepository.update(sourceCertificate);
        return giftCertificateDtoConverter.convertToDto(giftCertificate);
    }

    @Override
    public List<GiftCertificateDto> getAll(Integer page, Integer size) {
        Pageable pageable;
        try {
            pageable = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        List<GiftCertificate> giftCertificateList = giftCertificateRepository.getAll(pageable);
        return giftCertificateList.stream()
                .map(giftCertificateDtoConverter::convertToDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<GiftCertificateDto> getAllWithSortingAndFiltering(List<String> sortColumns,
                                                                  List<String> orderType,
                                                                  List<String> filterBy) {
        if (sortColumns == null) {
            sortColumns = new ArrayList<>();
        }
        if (orderType == null) {
            orderType = new ArrayList<>();
        }
        if (filterBy == null) {
            filterBy = new ArrayList<>();
        }
        List<GiftCertificate> giftCertificateList = giftCertificateRepository.getAllWithSortingAndFiltering(sortColumns, orderType, filterBy);
        return giftCertificateList.stream()
                .map(giftCertificateDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDto> getAllByTagName(List<String> tagName) {
        List<GiftCertificate> giftCertificateList = giftCertificateRepository.getAllByTagNames(tagName);
        return giftCertificateList.stream()
                .map(giftCertificateDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    private GiftCertificate isPresent(Long id) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.getByField("id", id);
        if (!giftCertificateOptional.isPresent()) {
            throw new NoSuchEntityException("message.certificate.missing");
        }
        return giftCertificateOptional.get();
    }

    private void setUpdatedFields(GiftCertificate sourceCertificate,
                                  GiftCertificateDto giftCertificateDto) {
        String name = giftCertificateDto.getName();
        if (name != null && !sourceCertificate.getName().equals(name)) {
            sourceCertificate.setName(name);
        }
        String description = giftCertificateDto.getDescription();
        if (description != null && !sourceCertificate.getDescription().equals(description)) {
            sourceCertificate.setDescription(description);
        }
        BigDecimal price = giftCertificateDto.getPrice();
        if (price != null && sourceCertificate.getPrice().compareTo(price) != 0) {
            sourceCertificate.setPrice(price);
        }
        int duration = giftCertificateDto.getDuration();
        if (duration != 0 && sourceCertificate.getDuration() != duration) {
            sourceCertificate.setDuration(duration);
        }
    }

    private Set<Tag> saveTags(Set<TagDto> tags) {
        Set<Tag> savedTags = new HashSet<>();
        for (TagDto tagDto : tags) {
            Tag tag = tagDtoConverter.convertToEntity(tagDto);
            Optional<Tag> optionalTag = tagRepository.getByName(tag.getName());
            Tag savedTag = optionalTag.orElseGet(() -> tagRepository.create(tag));
            savedTags.add(savedTag);
        }
        return savedTags;
    }

    private void validateFields(GiftCertificateDto giftCertificateDto) {
        customValidator.validateName(giftCertificateDto);
        customValidator.validateDescription(giftCertificateDto);
        customValidator.validatePrice(giftCertificateDto);
        customValidator.validateDuration(giftCertificateDto);
        customValidator.validateTags(giftCertificateDto);
    }
}