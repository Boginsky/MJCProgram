package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.service.TagService;
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

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = {"/{tagId}", ""})
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getAll(
            @PathVariable(name = "tagId", required = false) Long tagId,
            @RequestParam(name = "tagName", required = false) String tagName,
            @RequestParam(name = "giftCertificateId", required = false) Long giftCertificateId
    ) {
        return tagService.getRoute(tagId, tagName, giftCertificateId);
    }

    @DeleteMapping(value = "/tagId")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(name = "tagId") Long tagId) {
        tagService.deleteById(tagId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Tag tag) {
        tagService.create(tag);
    }

    @PatchMapping(value = "/tagId")
    @ResponseStatus(HttpStatus.OK)
    public void updateById(@PathVariable("tagId") Long id,
                           @RequestBody Tag tag) {
        tagService.updateNameById(id, tag);
    }
}
