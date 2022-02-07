package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.TagRepository;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.converter.impl.GiftCertificateConverter;
import com.epam.esm.service.dto.entity.GiftCertificateDto;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.util.CommonUtil;
import com.epam.esm.service.util.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Validated
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final TagRepository tagRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    @Qualifier("tagDtoConverter")
    private final DtoConverter<Tag, TagDto> tagDtoConverter;
    private final GiftCertificateConverter giftCertificateDtoConverter;
    @Qualifier("giftCertificateValidator")
    private final GiftCertificateValidator giftCertificateValidator;
    private final CommonUtil commonUtil;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository,
                                      GiftCertificateConverter giftCertificateDtoConverter, CommonUtil commonUtil,
                                      DtoConverter<Tag, TagDto> tagDtoConverter, GiftCertificateValidator giftCertificateValidator) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.giftCertificateDtoConverter = giftCertificateDtoConverter;
        this.tagDtoConverter = tagDtoConverter;
        this.commonUtil = commonUtil;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    @Override
    public CustomPage<GiftCertificateDto> getRoute(List<String> tagName, List<String> sortColumns,
                                                   List<String> orderType, List<String> filterBy,
                                                   Long giftCertificateId, Integer page, Integer size) {
        if (giftCertificateId != null) {
            return getById(giftCertificateId);
        } else if (!sortColumns.isEmpty() || !filterBy.isEmpty()) {
            return getAllWithSortingAndFiltering(sortColumns, filterBy, orderType, page, size);
        } else if (!tagName.isEmpty()) {
            return getAllByTagName(tagName, page, size);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    public void deleteById(Long id) {
        GiftCertificate giftCertificate = isPresent(id);
        for (Tag tag : giftCertificate.getTags()) {
            tag.getGiftCertificateList().remove(giftCertificate);
        }
        giftCertificateRepository.delete(giftCertificate);
    }

    @Override
    public CustomPage<GiftCertificateDto> getById(Long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoConverter.convertToDto(isPresent(id));
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        giftCertificateDtoList.add(giftCertificateDto);
        return CustomPage.<GiftCertificateDto>builder()
                .content(giftCertificateDtoList)
                .build();
    }

    @Override
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        validateFields(giftCertificateDto);
        isExist(giftCertificateDto.getName());
        Set<TagDto> tagSet = getTagDtoSet(giftCertificateDto);
        giftCertificateDto.setTags(tagSet);
        giftCertificateDto.setStatus(true);
        giftCertificateDto.setCreateDate(ZonedDateTime.now());
        giftCertificateDto.setLastUpdateDate(ZonedDateTime.now());
        GiftCertificate giftCertificate = giftCertificateRepository
                .save(giftCertificateDtoConverter
                        .convertToEntity(giftCertificateDto));
        return giftCertificateDtoConverter.convertToDto(giftCertificate);
    }

    @Override
    public GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto) {
        GiftCertificate sourceCertificate = isPresent(giftCertificateId);
        validateFields(giftCertificateDto);
        setUpdatedFields(sourceCertificate, giftCertificateDto);
        if (giftCertificateDto.getTags() != null) {
            Set<TagDto> tags = giftCertificateDto.getTags();
            sourceCertificate.setTags(saveTags(tags));
        }
        GiftCertificate giftCertificate = giftCertificateRepository.save(sourceCertificate);
        return giftCertificateDtoConverter.convertToDto(giftCertificate);
    }

    @Override
    public CustomPage<GiftCertificateDto> getAll(Integer page, Integer size) {
        Pageable pageable = commonUtil.getPageable(page, size, Sort.unsorted());
        Page<GiftCertificate> giftCertificateList = giftCertificateRepository.findAll(pageable);
        return giftCertificateDtoConverter.convertContentToDto(giftCertificateList);

    }

    @Override
    public CustomPage<GiftCertificateDto> getAllWithSortingAndFiltering(List<String> sortColumns, List<String> filterBy,
                                                                        List<String> orderType, Integer page, Integer size) {
        Sort sort = commonUtil.buildSort(sortColumns, orderType);
        Pageable pageable = commonUtil.getPageable(page, size, sort);
        Page<GiftCertificate> giftCertificateList;
        if (filterBy.size() == 2) {
            giftCertificateList = giftCertificateRepository.
                    findAllByNameContainingAndDescriptionContaining(filterBy.get(0), filterBy.get(1), pageable);
        } else {
            giftCertificateList = giftCertificateRepository.findAll(pageable);
        }
        return giftCertificateDtoConverter.convertContentToDto(giftCertificateList);
    }


    @Override
    public CustomPage<GiftCertificateDto> getAllByTagName(List<String> tagName, Integer page, Integer size) {
        Pageable pageable = commonUtil.getPageable(page, size, Sort.unsorted());
        List<Tag> tagList = getTags(tagName);
        Page<GiftCertificate> giftCertificateList = giftCertificateRepository.findAllByTagsIn(tagList, pageable);
        return giftCertificateDtoConverter.convertContentToDto(giftCertificateList);

    }

    private GiftCertificate isPresent(Long id) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(id);
        if (!giftCertificateOptional.isPresent()) {
            throw new NoSuchEntityException("message.certificate.missing");
        }
        return giftCertificateOptional.get();
    }

    private void setUpdatedFields(GiftCertificate sourceCertificate,
                                  GiftCertificateDto giftCertificateDto) {
        updateName(sourceCertificate, giftCertificateDto);
        updateDescription(sourceCertificate, giftCertificateDto);
        updatePrice(sourceCertificate, giftCertificateDto);
        updateDuration(sourceCertificate, giftCertificateDto);
    }

    private void updateDuration(GiftCertificate sourceCertificate, GiftCertificateDto giftCertificateDto) {
        int duration = giftCertificateDto.getDuration();
        if (duration != 0 && sourceCertificate.getDuration() != duration) {
            sourceCertificate.setDuration(duration);
        }
    }

    private void updatePrice(GiftCertificate sourceCertificate, GiftCertificateDto giftCertificateDto) {
        BigDecimal price = giftCertificateDto.getPrice();
        if (price != null && sourceCertificate.getPrice().compareTo(price) != 0) {
            sourceCertificate.setPrice(price);
        }
    }

    private void updateDescription(GiftCertificate sourceCertificate, GiftCertificateDto giftCertificateDto) {
        String description = giftCertificateDto.getDescription();
        if (description != null && !sourceCertificate.getDescription().equals(description)) {
            sourceCertificate.setDescription(description);
        }
    }

    private void updateName(GiftCertificate sourceCertificate, GiftCertificateDto giftCertificateDto) {
        String name = giftCertificateDto.getName();
        if (name != null && !sourceCertificate.getName().equals(name)) {
            sourceCertificate.setName(name);
        }
    }

    private Set<Tag> saveTags(Set<TagDto> tags) {
        Set<Tag> savedTags = new HashSet<>();
        for (TagDto tagDto : tags) {
            Tag tag = tagDtoConverter.convertToEntity(tagDto);
            Optional<Tag> optionalTag = tagRepository.findByName(tag.getName());
            Tag savedTag = optionalTag.orElseGet(() -> tagRepository.save(tag));
            savedTags.add(savedTag);
        }
        return savedTags;
    }

    private List<Tag> getTags(List<String> tagNames) {
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tagNames) {
            Optional<Tag> optionalTag = tagRepository.findByName(tagName);
            optionalTag.ifPresent(tagList::add);
        }
        return tagList;
    }

    private void validateFields(GiftCertificateDto giftCertificateDto) {
        giftCertificateValidator.validateName(giftCertificateDto);
        giftCertificateValidator.validateDescription(giftCertificateDto);
        giftCertificateValidator.validatePrice(giftCertificateDto);
        giftCertificateValidator.validateDuration(giftCertificateDto);
        giftCertificateValidator.validateTags(giftCertificateDto);
    }

    private Set<TagDto> getTagDtoSet(GiftCertificateDto giftCertificateDto) {
        Set<TagDto> tagSet = new HashSet<>();
        if (giftCertificateDto.getTags() != null) {
            for (TagDto tag : giftCertificateDto.getTags()) {
                Optional<Tag> tagOptional = tagRepository.findByName(tag.getName());
                if (tagOptional.isPresent()) {
                    tagSet.add(tagDtoConverter.convertToDto(tagOptional.get()));
                } else {
                    tagSet.add(tag);
                }
            }
        }
        return tagSet;
    }

    private void isExist(String giftCertificateName) {
        if (giftCertificateRepository.findByName(giftCertificateName).isPresent()) {
            throw new DuplicateEntityException("message.certificate.existent");
        }
    }
}