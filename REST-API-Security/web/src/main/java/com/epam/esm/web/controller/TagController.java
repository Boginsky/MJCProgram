package com.epam.esm.web.controller;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.service.TagService;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public CustomPage<TagDto> getAll(
            @PathVariable(name = "id", required = false) Long id,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size
    ) {
        CustomPage<TagDto> tagDtoList = tagService.getRoute(id, page, size);
        tagDtoList.getContent().forEach(tagLinkAdder::addLinks);
        return tagDtoList;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('tags:delete')")
    public void deleteById(@PathVariable(name = "id") Long id) {
        tagService.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('tags:create')")
    public TagDto create(@RequestBody @Valid TagDto tagDto) {
        tagDto = tagService.create(tagDto);
        tagLinkAdder.addLinks(tagDto);
        return tagDto;
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('tags:update')")
    public TagDto updateById(@PathVariable("id") Long id,
                             @RequestBody @Valid TagDto tagDto) {
        tagDto.setId(id);
        tagDto = tagService.update(tagDto);
        tagLinkAdder.addLinks(tagDto);
        return tagDto;
    }
}
