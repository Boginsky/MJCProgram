package com.epam.esm.web.controller;

import com.epam.esm.model.constant.SortParamsContext;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


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
    public List<GiftCertificate> getAll() {
        return giftCertificateService.getAll();
    }

    @GetMapping("/with_tags")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificateDto> getAllWithTags() {
        return giftCertificateService.getAllWithTags();
    }

    @GetMapping("/{giftCertificateId}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificate getById(@PathVariable("giftCertificateId") Long id) throws ServiceException {
        return giftCertificateService.findById(id);
    }

    @PutMapping("/{giftCertificateId}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateById(@PathVariable("giftCertificateId") Long id,
                                         @RequestBody GiftCertificateDto giftCertificateDto) throws ServiceException {
        return giftCertificateService.updateById(id, giftCertificateDto);

    }

    @DeleteMapping("{giftCertificateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("giftCertificateId") Long id) throws ServiceException {
        giftCertificateService.deleteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody GiftCertificateDto giftCertificateDto,
                       HttpServletResponse httpServletResponse) throws ServiceException {
        giftCertificateService.create(giftCertificateDto);
        httpServletResponse.addHeader("Gift certificate with name ", giftCertificateDto
                .getGiftCertificate().getName() + " created");
    }

    @GetMapping("/sort/")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificate> getWithSorting(@RequestBody Map<String, String> sortParamsContext) {
        return giftCertificateService.getAllWithSorting(new SortParamsContext(sortParamsContext));
    }

    @GetMapping("/filter/")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificate> getWithFiltering(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description,", required = false) String description
    ) {
        return giftCertificateService.findWithFiltering(name, description);
    }

    @GetMapping("/with_tag")
    @ResponseStatus(HttpStatus.OK)
    public List<GiftCertificate> getGiftCertificateByTagName(
            @RequestParam(name = "tagName") String tagName) {
        return giftCertificateService.getAllByTagName(tagName);
    }
}
