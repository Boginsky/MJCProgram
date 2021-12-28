package com.epam.esm.service.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.GiftCertificateValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final TagDao tagDao;
    private final Validator<Tag> tagValidator;
    private final GiftCertificateDao giftCertificateDao;
    private final DtoConverter<Tag, TagDto> tagDtoConverter;
    private final GiftCertificateValidatorImpl giftCertificateValidator;
    private final DtoConverter<GiftCertificate, GiftCertificateDto> giftCertificateDtoConverter;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      GiftCertificateValidatorImpl giftCertificateValidator,
                                      Validator<Tag> tagValidator, DtoConverter<GiftCertificate,
            GiftCertificateDto> giftCertificateDtoConverter,
                                      DtoConverter<Tag, TagDto> tagDtoConverter) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
        this.giftCertificateDtoConverter = giftCertificateDtoConverter;
        this.tagDtoConverter = tagDtoConverter;

    }

    @Override
    public List<GiftCertificateDto> getRoute(List<String> tagName, List<String> sortColumns,
                                             List<String> orderType, List<String> filterBy,
                                             Long giftCertificateId, String allWithTags,
                                             Integer page, Integer size) {
        if (giftCertificateId != null) {
            return getById(giftCertificateId);
        } else if (sortColumns != null || filterBy != null) {
            return getAllWithSortingAndFiltering(sortColumns, orderType, filterBy);
        } else if (tagName != null) {
            return getAllByTagName(tagName);
        } else if (allWithTags != null) {
            return getAllWithTags(page, size);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    public List<GiftCertificateDto> getAll(Integer page, Integer size) {
        Pageable pageable;
        try {
            pageable = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getAll(pageable).getContent();
        return giftCertificateList.stream()
                .map(giftCertificateDtoConverter::convertToDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<GiftCertificateDto> getById(Long id) {
        GiftCertificate giftCertificate = isPresentGiftCertificate(id);
        List<GiftCertificateDto> listOfGiftCertificate = new ArrayList<>();
        listOfGiftCertificate.add(giftCertificateDtoConverter.convertToDto(giftCertificate));
        return listOfGiftCertificate;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateDtoConverter.convertToEntity(giftCertificateDto);
        validateGiftCertificate(giftCertificate);
        validateTags(giftCertificateDto.getCertificateTags());
        String giftCertificateName = getGiftCertificateName(giftCertificate);
        giftCertificate = createGiftCertificate(giftCertificate);
        Long giftCertificateId = giftCertificateDao.getByName(giftCertificateName)
                .map(GiftCertificate::getId).orElseThrow(() -> new NoSuchEntityException("message.notFound"));
        createGiftCertificateTagReference(giftCertificateDto.getCertificateTags(), giftCertificateId);
        return giftCertificateDtoConverter.convertToDto(giftCertificate);
    }

    @Override
    public List<GiftCertificateDto> getAllWithTags(Integer page, Integer size) {
        Pageable pageable;
        try {
            pageable = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        List<GiftCertificateDto> listOfDto = new ArrayList<>();
        List<GiftCertificate> listOfCertificates = giftCertificateDao.getAll(pageable).getContent();
        for (GiftCertificate giftCertificate : listOfCertificates) {
            List<Tag> listOfTags = tagDao.getTagsByGiftCertificateId(giftCertificate.getId());
            GiftCertificateDto giftCertificateDto = giftCertificateDtoConverter.convertToDto(giftCertificate);
            giftCertificateDto.setCertificateTags(listOfTags.stream()
                    .map(tagDtoConverter::convertToDto)
                    .collect(Collectors.toList()));
            listOfDto.add(giftCertificateDto);
        }
        return listOfDto;
    }


    @Transactional
    @Override
    public void deleteById(Long id) {
        isPresentGiftCertificate(id);
        giftCertificateDao.deleteById(id);
    }

    @Override
    public List<GiftCertificateDto> getAllByTagName(List<String> tagName) {
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getGiftCertificateByTagName(tagName);
        for (GiftCertificate giftCertificate : giftCertificateList) {
            giftCertificateDtoList.add(buildGiftCertificateDto(giftCertificate));
        }
        return giftCertificateDtoList;
    }

    @Override
    public List<GiftCertificateDto> getAllWithSortingAndFiltering(List<String> sortColumns,
                                                                  List<String> orderType,
                                                                  List<String> filterBy) {
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        if (sortColumns == null) {
            sortColumns = new ArrayList<>();
        }
        if (orderType == null) {
            orderType = new ArrayList<>();
        }
        if (filterBy == null) {
            filterBy = new ArrayList<>();
        }
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getAllWithSortingAndFiltering(sortColumns, orderType, filterBy);
        for (GiftCertificate giftCertificate : giftCertificateList) {
            giftCertificateDtoList.add(buildGiftCertificateDto(giftCertificate));
        }
        return giftCertificateDtoList;
    }

    @Transactional
    @Override
    public GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateDtoConverter.convertToEntity(giftCertificateDto);
        List<TagDto> tagList = giftCertificateDto.getCertificateTags();
        if (tagList == null || giftCertificate == null) {
            throw new InvalidEntityException("message.cantUpdateGiftCertificateById");
        }
        isPresentGiftCertificate(giftCertificateId);
        giftCertificateDao.updateById(giftCertificateId, updateInfo(giftCertificate));
        updateCertificateTags(tagList, giftCertificateId);
        return buildGiftCertificateDto(isPresentGiftCertificate(giftCertificateId));
    }

    private GiftCertificate createGiftCertificate(GiftCertificate giftCertificate) {
        Long id = giftCertificateDao.create(giftCertificate);
        return isPresentGiftCertificate(id);
    }

    private Map<String, Object> updateInfo(GiftCertificate giftCertificate) {
        Map<String, Object> updateInfo = new HashMap<>();
        isValidNameForUpdate(giftCertificate, updateInfo, giftCertificateValidator);
        isValidDescriptionForUpdate(giftCertificate, updateInfo, giftCertificateValidator);
        isPriceValidForUpdate(giftCertificate, updateInfo, giftCertificateValidator);
        isDurationValidForUpdate(giftCertificate, updateInfo, giftCertificateValidator);
        return updateInfo;
    }

    private String getGiftCertificateName(GiftCertificate giftCertificate) {
        String giftCertificateName = giftCertificate.getName();
        boolean isCertificateExist = giftCertificateDao.getByName(giftCertificateName).isPresent();
        if (isCertificateExist) {
            throw new DuplicateEntityException("message.giftCertificateExists");
        }
        return giftCertificateName;
    }

    private void isDurationValidForUpdate(GiftCertificate giftCertificate,
                                          Map<String, Object> updateInfo,
                                          GiftCertificateValidatorImpl giftCertificateValidator) {
        Integer duration = giftCertificate.getDuration();
        if (duration != null) {
            if (!giftCertificateValidator.isDurationValid(duration)) {
                throw new InvalidEntityException("message.cantUpdateInvalidDuration");
            }
            updateInfo.put("duration", duration);
        }
    }

    private void isPriceValidForUpdate(GiftCertificate giftCertificate,
                                       Map<String, Object> updateInfo,
                                       GiftCertificateValidatorImpl giftCertificateValidator) {
        BigDecimal price = giftCertificate.getPrice();
        if (price != null) {
            if (!giftCertificateValidator.isPriceValid(price)) {
                throw new InvalidEntityException("message.cantUpdateInvalidPrice");
            }
            updateInfo.put("price", price);
        }
    }

    private void isValidDescriptionForUpdate(GiftCertificate giftCertificate,
                                             Map<String, Object> updateInfo,
                                             GiftCertificateValidatorImpl giftCertificateValidator) {
        String description = giftCertificate.getDescription();
        if (description != null) {
            if (!giftCertificateValidator.isDescriptionValid(description)) {
                throw new InvalidEntityException("message.cantUpdateInvalidDescription");
            }
            updateInfo.put("description", description);
        }
    }

    private void isValidNameForUpdate(GiftCertificate giftCertificate,
                                      Map<String, Object> updateInfo,
                                      GiftCertificateValidatorImpl giftCertificateValidator) {
        String name = giftCertificate.getName();
        if (name != null) {
            if (!giftCertificateValidator.isNameValid(name)) {
                throw new InvalidEntityException("message.cantUpdateInvalidName");
            }
            updateInfo.put("name", name);
        }
    }

    private GiftCertificate isPresentGiftCertificate(Long id) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.getById(id);
        if (!giftCertificateOptional.isPresent()) {
            throw new NoSuchEntityException("message.notFound");
        } else {
            return giftCertificateOptional.get();
        }
    }

    private void updateCertificateTags(List<TagDto> tagList, Long giftCertificateId) {
        for (TagDto tag : tagList) {
            String tagName = tag.getName();
            if (tagName != null) {
                Optional<Tag> tagOptional = tagDao.getByName(tagName);
                if (!tagOptional.isPresent()) {
                    createGiftCertificateTag(tagDtoConverter.convertToEntity(tag));
                    Long tagId = tagDao.getByName(tagName).get().getId();
                    if (!giftCertificateDao.getTagIdsByGiftCertificateId(giftCertificateId).contains(tagId)) {
                        giftCertificateDao.createGiftCertificateTagReference(giftCertificateId, tagId);
                    }
                }
            }
        }
    }

    private GiftCertificateDto buildGiftCertificateDto(GiftCertificate giftCertificate) {
        GiftCertificateDto giftCertificateDto = giftCertificateDtoConverter.convertToDto(giftCertificate);
        List<Optional<Tag>> optionalTagList = new ArrayList<>();
        List<Long> tagIds = giftCertificateDao.getTagIdsByGiftCertificateId(giftCertificate.getId());
        tagIds.forEach(id -> optionalTagList.add(tagDao.getById(id)));
        optionalTagList.stream()
                .filter(Optional::isPresent)
                .forEach(tag -> giftCertificateDto.addTag(tagDtoConverter.convertToDto(tag.get())))
        ;
        return giftCertificateDto;
    }


    private void createGiftCertificateTagReference(List<TagDto> tags, Long giftCertificateId) {
        if (tags != null) {
            for (TagDto tagDto : tags) {
                String tagName = tagDto.getName();
                Optional<Tag> tagOptional = tagDao.getByName(tagName);
                if (!tagOptional.isPresent()) {
                    Tag tag = tagDtoConverter.convertToEntity(tagDto);
                    createGiftCertificateTag(tag);
                    tag = tagDao.getByName(tagName).get();
                    giftCertificateDao.createGiftCertificateTagReference(giftCertificateId, tag.getId());
                }
            }
        }
    }

    private void createGiftCertificateTag(Tag tag) {
        tagDao.create(tag);
    }

    private void validateGiftCertificate(GiftCertificate giftCertificate) {
        if (!giftCertificateValidator.isValid(giftCertificate)) {
            throw new InvalidEntityException("message.giftCertificateInvalid");
        }
    }

    private void validateTags(List<TagDto> tags) {
        boolean isCorrectTags = tags.stream()
                .map(tagDtoConverter::convertToEntity)
                .allMatch(tagValidator::isValid);
        if (!isCorrectTags) {
            throw new InvalidEntityException("message.tagInvalid");
        }
    }
}
