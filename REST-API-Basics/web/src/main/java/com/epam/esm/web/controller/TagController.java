package com.epam.esm.web.controller;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.service.TagService;
import com.epam.esm.web.exception.ControllerException;
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
    public List<Tag> getAll(){
        return tagService.getAll();
    }

    @GetMapping(value = "/{tagId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Tag getById(@PathVariable("tagId") long id) throws ControllerException {
        try {
            return tagService.findById(id);
        } catch (ServiceException e) {
            throw new ControllerException("Exception in tag controller: can't find tag by id",e);
        }
    }

    @GetMapping(value = "/{tagName}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Tag getByName(@PathVariable("tagName") String name) throws ControllerException {
        try {
            return tagService.findByName(name);
        } catch (ServiceException e) {
            throw new ControllerException("Exception in tag controller: can't find tag by name",e);
        }
    }

    @GetMapping(value = "/{giftCertificateId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getTagsByGiftCertificateId(@PathVariable("giftCertificateId") long id){
        return tagService.getTagsByGiftCertificateId(id);
    }


    @DeleteMapping("/{tagName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByName(@PathVariable("tagName") String name) throws ControllerException {
        try {
            tagService.deleteByName(name);
        } catch (ServiceException e) {
            throw new ControllerException("Exception in controller: can't delete tag by name",e);
        }
    }

    @DeleteMapping("/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("tagId") long id) throws ControllerException {
        try {
            tagService.deleteById(id);
        } catch (ServiceException e) {
            throw new ControllerException("Exception in controller: can't delete tag by id",e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Tag tag,
                       HttpServletResponse httpServletResponse) throws ControllerException {
        try {
            tagService.create(tag);
            httpServletResponse.addHeader("Tag with name ",tag.getName() + " created");
        } catch (ServiceException e) {
            throw new ControllerException("Exception in controller: can't create tag with name" + tag.getName(),e);
        }
    }

    @PutMapping("/{tagId}/{tagNameForUpdate}")
    @ResponseStatus(HttpStatus.OK)
    public void updateById(@PathVariable("tagId") String id,
                           @PathVariable("tagNameForUpdate") String name) throws ControllerException {
        try {
            tagService.updateNameById(Long.parseLong(id),name);
        } catch (ServiceException e) {
            throw new ControllerException("Exception in controller: can't update tag's name by id");
        }
    }
}
