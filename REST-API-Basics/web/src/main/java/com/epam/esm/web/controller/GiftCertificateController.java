package com.epam.esm.web.controller;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificate> getAll(
            @RequestParam(name = "giftCertificateId", required = false) Long giftCertificateId
    ) {
        return giftCertificateService.getRoute(giftCertificateId);
    }

    @GetMapping("/with-tags")
    public List<GiftCertificateDto> getAllWithTags(
            @RequestParam(name = "tagName", required = false) String tagName,
            @RequestParam(name = "sort", required = false) List<String> sortColumns,
            @RequestParam(name = "order", required = false) List<String> orderType,
            @RequestParam(name = "filter", required = false) List<String> filterBy
    ) {
        return giftCertificateService.getRouteWithTags(tagName, sortColumns, orderType, filterBy);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateById(@RequestParam("giftCertificateId") Long id,
                                         @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.updateById(id, giftCertificateDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@RequestParam("giftCertificateId") Long id) {
        giftCertificateService.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody GiftCertificateDto giftCertificateDto) {
        giftCertificateService.create(giftCertificateDto);
    }
}
