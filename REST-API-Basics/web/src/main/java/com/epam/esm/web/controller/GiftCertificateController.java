package com.epam.esm.web.controller;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.service.GiftCertificateService;
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
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final LinkAdder<GiftCertificateDto> giftCertificateDtoLinkAdder;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, LinkAdder<GiftCertificateDto> giftCertificateDtoLinkAdder) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateDtoLinkAdder = giftCertificateDtoLinkAdder;
    }

    @GetMapping(value = {"/{gift-certificate-id}", ""})
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> getAllWithTags(
            @PathVariable(name = "gift-certificate-id", required = false) Long giftCertificateId,
            @RequestParam(name = "all-with-tag", required = false) String allWithTag,
            @RequestParam(name = "tag-name", required = false) List<String> tagName,
            @RequestParam(name = "sort", required = false) List<String> sortColumns,
            @RequestParam(name = "order", required = false) List<String> orderType,
            @RequestParam(name = "filter", required = false) List<String> filterBy,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size
    ) {
        List<GiftCertificateDto> giftCertificateDtoList = giftCertificateService.getRoute(tagName, sortColumns,
                orderType, filterBy,
                giftCertificateId, allWithTag,
                page, size);
        return giftCertificateDtoList.stream()
                .peek(giftCertificateDtoLinkAdder::addLinks)
                .collect(Collectors.toList());
    }

    @PatchMapping(value = "/{gift-certificate-id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateById(@PathVariable("gift-certificate-id") Long id,
                                         @RequestBody GiftCertificateDto requestDto) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.updateById(id, requestDto);
        giftCertificateDtoLinkAdder.addLinks(giftCertificateDto);
        return giftCertificateDto;
    }

    @DeleteMapping(value = "/{gift-certificate-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("gift-certificate-id") Long id) {
        giftCertificateService.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody GiftCertificateDto requestDto) {
        GiftCertificateDto giftCertificateDto = giftCertificateService.create(requestDto);
        giftCertificateDtoLinkAdder.addLinks(giftCertificateDto);
        return giftCertificateDto;
    }
}
