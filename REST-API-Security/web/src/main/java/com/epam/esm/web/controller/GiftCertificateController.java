package com.epam.esm.web.controller;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.service.dto.entity.GiftCertificateDto;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final LinkAdder<GiftCertificateDto> giftCertificateDtoLinkAdder;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, LinkAdder<GiftCertificateDto> giftCertificateDtoLinkAdder) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateDtoLinkAdder = giftCertificateDtoLinkAdder;
    }

    @GetMapping(value = {"/{id}", ""})
    @ResponseStatus(HttpStatus.OK)
    public CustomPage<GiftCertificateDto> getAllWithTags(
            @PathVariable(name = "id", required = false) Long id,
            @RequestParam(name = "tag-name", defaultValue = "", required = false) List<String> tagName,
            @RequestParam(name = "sort", defaultValue = "", required = false) List<String> sortColumns,
            @RequestParam(name = "order", defaultValue = "", required = false) List<String> orderType,
            @RequestParam(name = "filter", defaultValue = "", required = false) List<String> filterBy,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size
    ) {
        CustomPage<GiftCertificateDto> giftCertificateDtoList = giftCertificateService.getRoute(tagName, sortColumns,
                orderType, filterBy, id,
                page, size);
        giftCertificateDtoList.getContent().forEach(giftCertificateDtoLinkAdder::addLinks);
        return giftCertificateDtoList;
    }

    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('giftCertificate:update')")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateById(@PathVariable("id") Long id,
                                         @RequestBody GiftCertificateDto requestDto) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.updateById(id, requestDto);
        giftCertificateDtoLinkAdder.addLinks(giftCertificateDto);
        return giftCertificateDto;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('giftCertificate:delete')")
    public void deleteById(@PathVariable("id") Long id) {
        giftCertificateService.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('giftCertificate:create')")
    public GiftCertificateDto create(@RequestBody @Valid GiftCertificateDto requestDto) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.create(requestDto);
        giftCertificateDtoLinkAdder.addLinks(giftCertificateDto);
        return giftCertificateDto;
    }
}
