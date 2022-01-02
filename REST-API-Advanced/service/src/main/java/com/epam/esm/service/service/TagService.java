package com.epam.esm.service.service;


import com.epam.esm.model.entity.BestTag;
import com.epam.esm.service.dto.TagDto;

import java.util.List;


public interface TagService {

    TagDto create(TagDto tagDto);

    void deleteById(Long id);

    TagDto update(TagDto tagDto);

    List<TagDto> getById(Long id);

    List<TagDto> getAll(Integer page, Integer size);

    List<TagDto> getRoute(Long tagId, Integer page, Integer size);

    BestTag getHighestCostTag(Long userId);
}
