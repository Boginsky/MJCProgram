package com.epam.esm.service.service;


import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.service.dto.entity.TagDto;


public interface TagService {

    TagDto create(TagDto tagDto);

    void deleteById(Long id);

    TagDto update(TagDto tagDto);

    CustomPage<TagDto> getById(Long id);

    CustomPage<TagDto> getAll(Integer page, Integer size);

    CustomPage<TagDto> getRoute(Long tagId, Integer page, Integer size);

    BestTag getHighestCostTag(Long userId);
}
