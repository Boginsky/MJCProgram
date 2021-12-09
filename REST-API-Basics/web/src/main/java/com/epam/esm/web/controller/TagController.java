package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @GetMapping(value = "/id/{tagId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Tag getById(@PathVariable("tagId") Long id) throws ServiceException {
        return tagService.findById(id);
    }

    @GetMapping(value = "/name/{tagName}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Tag getByName(@PathVariable("tagName") String name) throws ServiceException {
        return tagService.findByName(name);
    }

    @GetMapping(value = "/gift_certificate/{giftCertificateId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getTagsByGiftCertificateId(@PathVariable("giftCertificateId") Long id) {
        return tagService.getTagsByGiftCertificateId(id);
    }


    @DeleteMapping("/name/{tagName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByName(@PathVariable("tagName") String name) throws ServiceException {
        tagService.deleteByName(name);

    }

    @DeleteMapping("/id/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("tagId") Long id) throws ServiceException {
        tagService.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Tag tag,
                       HttpServletResponse httpServletResponse) throws ServiceException {
        tagService.create(tag);
        httpServletResponse.addHeader("Tag with name ", tag.getName() + " created");
    }

    @PutMapping("/{tagId}/{tagNameForUpdate}")
    @ResponseStatus(HttpStatus.OK)
    public void updateById(@PathVariable("tagId") String id,
                           @PathVariable("tagNameForUpdate") String name) throws ServiceException {
        tagService.updateNameById(Long.parseLong(id), name);
    }
}
