package com.epam.esm.web.controller;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.web.exception.ControllerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/gift_certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificate> getAll(){
        return giftCertificateService.getAll();
    }

    @GetMapping("/{giftCertificateId}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificate getById(@PathVariable("giftCertificateId") Long id) throws ControllerException {
        try {
            return giftCertificateService.findById(id);
        } catch (ServiceException e) {
            throw new ControllerException("Exception in gift certificate controller: can't find by id");
        }
    }

    @PutMapping("/{giftCertificateId}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateById(@PathVariable("giftCertificateId") Long id,
                                         @RequestBody GiftCertificateDto giftCertificateDto) throws ControllerException {
        try {
            return giftCertificateService.updateById(id,giftCertificateDto);
        } catch (ServiceException e) {
            throw new ControllerException("Exception in gift certificate controller: can't update by id");
        }
    }

    @DeleteMapping("{giftCertificateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("giftCertificateId") Long id) throws ControllerException {
        try {
            giftCertificateService.deleteById(id);
        } catch (ServiceException e) {
            throw new ControllerException("Exception in gift certificate controller: can't delete by id");
        }
    }


    public List<GiftCertificate> getWithSorting(){
        return null;
    }

    public List<GiftCertificate> getWithFiltering(){
        return null;
    }
}
