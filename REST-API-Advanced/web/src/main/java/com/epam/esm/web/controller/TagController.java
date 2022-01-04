package com.epam.esm.web.controller;

import com.epam.esm.model.entity.BestTag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.service.TagService;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final LinkAdder<TagDto> tagLinkAdder;

    @Autowired
    public TagController(TagService tagService, LinkAdder<TagDto> tagLinkAdder) {
        this.tagService = tagService;
        this.tagLinkAdder = tagLinkAdder;
    }

    @GetMapping(value = {"/{id}", ""})
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> getAll(
            @PathVariable(name = "id", required = false) Long tagId,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size
    ) {
        List<TagDto> tagList = tagService.getRoute(tagId, page, size);
        tagList.forEach(tagLinkAdder::addLinks);
        return tagList;
    }

    @GetMapping(value = "/highest-cost")
    public BestTag getTag(
            @RequestParam(name = "user-id") Long userId
    ) {
        return tagService.getHighestCostTag(userId);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(name = "id") Long tagId) {
        tagService.deleteById(tagId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody TagDto tagDto) {
        tagDto = tagService.create(tagDto);
        tagLinkAdder.addLinks(tagDto);
        return tagDto;
    }

    @PatchMapping(value = "/{tag-id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDto updateById(@PathVariable("tag-id") Long id,
                             @RequestBody TagDto tagDto) {
        tagDto.setId(id);
        tagDto = tagService.update(tagDto);
        tagLinkAdder.addLinks(tagDto);
        return tagDto;
    }
}
