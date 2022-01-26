package com.epam.esm;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.TagRepository;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.GiftCertificateDto;
import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.util.validator.GiftCertificateValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GiftCertificateServiceImpl.class})
public class GiftCertificateServiceImplTest {

    private Long id;
    private String name;
    private GiftCertificate giftCertificate;
    private GiftCertificateDto giftCertificateDto;
    private Integer defaultPage;
    private Integer defaultPageSize;
    private Pageable pageable;
    private Sort sort;

    @MockBean
    private GiftCertificateRepository giftCertificateRepository;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private DtoConverter<GiftCertificate, GiftCertificateDto> giftCertificateDtoConverter;
    @MockBean
    private DtoConverter<Tag, TagDto> tagDtoConverter;
    @MockBean
    private GiftCertificateValidator giftCertificateValidator;
    @Autowired
    private GiftCertificateServiceImpl giftCertificateService;

    @Before
    public void setUp() {
        id = 1L;
        name = "name";
        defaultPage = 0;
        defaultPageSize = 10;
        sort = Sort.unsorted();
        giftCertificate = GiftCertificate.builder()
                .id(id)
                .name(name)
                .description(name)
                .price(BigDecimal.TEN)
                .duration(defaultPage)
                .createDate(ZonedDateTime.now())
                .lastUpdateDate(ZonedDateTime.now())
                .build();
        giftCertificateDto = new GiftCertificateDto(id, name, name, BigDecimal.TEN, defaultPageSize, true, ZonedDateTime.now(), ZonedDateTime.now(), new HashSet<>());
        pageable = PageRequest.of(defaultPage, defaultPageSize);
    }

    @Test
    public void testCreateShouldCreateWhenNotExist() {
        when(giftCertificateDtoConverter.convertToEntity(giftCertificateDto)).thenReturn(giftCertificate);
        when(giftCertificateRepository.save(giftCertificate)).thenReturn(giftCertificate);
        giftCertificateService.create(giftCertificateDto);
        verify(giftCertificateRepository).save(giftCertificate);
    }

    @Test(expected = InvalidParametersException.class)
    public void testGetAllShouldThrowInvalidParametersExceptionWhenParametersInvalid() {
        giftCertificateService.getAll(-10, -10);
    }

    @Test
    public void testGetAllShouldGetWhenFound() {
        giftCertificateService.getAll(defaultPage, defaultPageSize);
        verify(giftCertificateRepository).findAll();
    }

    @Test
    public void testGetAllWIthSortingAndFiltering() {
        when(giftCertificateDtoConverter.convertToDto(giftCertificate)).thenReturn(giftCertificateDto);
        giftCertificateService.getAllWithSortingAndFiltering(null, null, null, defaultPage, defaultPageSize);
        verify(giftCertificateRepository).findAllByNameContainingOrDescriptionContaining("", "", pageable);
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateDtoConverter.convertToDto(giftCertificate)).thenReturn(giftCertificateDto);
        giftCertificateService.getById(id);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetByIdShouldThrowNoSuchEntityExceptionWhenNotFound() {
        when(giftCertificateDtoConverter.convertToDto(giftCertificate)).thenReturn(giftCertificateDto);
        giftCertificateService.getById(id);
    }

    @Test
    public void getAllByTagNameShouldGetWhenFound() {
        when(giftCertificateDtoConverter.convertToDto(giftCertificate)).thenReturn(giftCertificateDto);
        giftCertificateService.getAllByTagName(new ArrayList<>(), defaultPage, defaultPageSize);
        verify(giftCertificateRepository).findAllByTagsIn(new ArrayList<>(), pageable);
    }

    @Test
    public void updateByIdShouldUpdateWhenFound() {
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateDtoConverter.convertToDto(giftCertificate)).thenReturn(giftCertificateDto);
        giftCertificateService.updateById(id, giftCertificateDto);
        verify(giftCertificateRepository).save(giftCertificate);
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() {
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        giftCertificateService.deleteById(id);
        verify(giftCertificateRepository).delete(giftCertificate);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testDeleteByIdShouldThrowNoSuchEntityExceptionWhenNotFound() {
        giftCertificateService.deleteById(id);
        verify(giftCertificateRepository).delete(giftCertificate);
    }

    @After
    public void tierDown() {
        id = null;
        name = null;
        defaultPage = null;
        defaultPageSize = null;
        giftCertificate = null;
        giftCertificateDto = null;
    }
}
