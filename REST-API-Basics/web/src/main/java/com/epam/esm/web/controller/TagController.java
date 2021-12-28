package com.epam.esm.web.controller;

import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;
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
import java.util.stream.Collectors;

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

    @GetMapping(value = {"/{tag-id}", ""})
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> getAll(
            @PathVariable(name = "tag-id", required = false) Long tagId,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "gift-certificate-id", required = false) Long giftCertificateId
    ) {
        List<TagDto> tagList = tagService.getRoute(tagId, giftCertificateId, page, size);
        return tagList.stream()
                .peek(tagLinkAdder::addLinks)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/highest-cost")
    public BestTag getTag() {
        return tagService.getHighestCostTag();
    }

    @DeleteMapping(value = "/{tag-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(name = "tag-id") Long tagId) {
        tagService.deleteById(tagId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody Tag tag) {
        TagDto tagDto = tagService.create(tag);
        tagLinkAdder.addLinks(tagDto);
        return tagDto;
    }

    @PatchMapping(value = "/{tag-id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDto updateById(@PathVariable("tag-id") Long id,
                             @RequestBody Tag tag) {
        TagDto tagDto = tagService.updateNameById(id, tag);
        tagLinkAdder.addLinks(tagDto);
        return tagDto;
    }
}
