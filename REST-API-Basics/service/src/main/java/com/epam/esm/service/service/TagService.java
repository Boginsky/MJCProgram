package com.epam.esm.service.service;


import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.TagDto;

import java.util.List;


public interface TagService {

    TagDto create(Tag tag);

    TagDto updateNameById(Long id, Tag tag);

    void deleteById(Long id);

    List<TagDto> getTagsByGiftCertificateId(Long giftCertificateId);

    List<TagDto> getById(Long id);

    List<TagDto> getAll(Integer page, Integer size);

    List<TagDto> getRoute(Long tagId, Long giftCertificateId, Integer page, Integer size);

    BestTag getHighestCostTag();
}
