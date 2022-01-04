package com.epam.esm;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.model.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.impl.GiftCertificateServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GiftCertificateServiceImpl.class})
public class GiftCertificateServiceImplTest {

    private Long id;
    private String name;
    private GiftCertificate giftCertificate;
    private GiftCertificateDto giftCertificateDto;
    private Tag tag;
    private TagDto tagDto;
    private Integer defaultPage;
    private Integer defaultPageSize;
    private List<String> listOfStrings;

    @MockBean
    private GiftCertificateRepositoryImpl giftCertificateRepository;
    @MockBean
    private TagRepositoryImpl tagRepository;
    @MockBean
    private DtoConverter<Tag, TagDto> tagDtoConverter;
    @MockBean
    private DtoConverter<GiftCertificate, GiftCertificateDto> giftCertificateDtoConverter;
    @Autowired
    private GiftCertificateServiceImpl giftCertificateService;

    @Before
    public void setUp() {
        id = 1L;
        name = "name";
        defaultPage = 0;
        defaultPageSize = 10;
        giftCertificate = new GiftCertificate(id, name, name, BigDecimal.TEN, defaultPageSize, ZonedDateTime.now(), ZonedDateTime.now());
        giftCertificateDto = new GiftCertificateDto(id, name, name, BigDecimal.TEN, defaultPageSize, ZonedDateTime.now(), ZonedDateTime.now(), new HashSet<>());
        tag = new Tag(id, name);
        tagDto = new TagDto(id, name);
        listOfStrings = new ArrayList<>();
    }

    @Test
    public void testCreateShouldCreateWhenNotExist() {
        when(giftCertificateDtoConverter.convertToEntity(giftCertificateDto)).thenReturn(giftCertificate);
        when(giftCertificateRepository.create(giftCertificate)).thenReturn(giftCertificate);
        giftCertificateService.create(giftCertificateDto);
        verify(giftCertificateRepository).create(giftCertificate);
    }

    @Test(expected = InvalidParametersException.class)
    public void testGetAllShouldThrowInvalidParametersExceptionWhenParametersInvalid() {
        giftCertificateService.getAll(-10, -10);
    }

    @Test
    public void testGetAllShouldGetWhenFound(){
        giftCertificateService.getAll(defaultPage,defaultPageSize);
        verify(giftCertificateRepository).getAll(any());
    }

    @Test
    public void testGetAllWIthSortingAndFiltering() {
        when(giftCertificateDtoConverter.convertToDto(giftCertificate)).thenReturn(giftCertificateDto);
        giftCertificateService.getAllWithSortingAndFiltering(null, null, null);
        verify(giftCertificateRepository).getAllWithSortingAndFiltering(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(giftCertificateRepository.getByField("id", id)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateDtoConverter.convertToDto(giftCertificate)).thenReturn(giftCertificateDto);
        giftCertificateService.getById(id);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetByIdShouldThrowNoSuchEntityExceptionWhenNotFound(){
        when(giftCertificateDtoConverter.convertToDto(giftCertificate)).thenReturn(giftCertificateDto);
        giftCertificateService.getById(id);
    }

    @Test
    public void getAllByTagNameShouldGetWhenFound(){
        when(giftCertificateDtoConverter.convertToDto(giftCertificate)).thenReturn(giftCertificateDto);
        giftCertificateService.getAllByTagName(listOfStrings);
        verify(giftCertificateRepository).getAllByTagNames(listOfStrings);
    }

    @Test
    public void updateByIdShouldUpdateWhenFound(){
        when(giftCertificateRepository.getByField("id",id)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateDtoConverter.convertToDto(giftCertificate)).thenReturn(giftCertificateDto);
        giftCertificateService.updateById(id,giftCertificateDto);
        verify(giftCertificateRepository).update(giftCertificate);
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound(){
        when(giftCertificateRepository.getByField("id",id)).thenReturn(Optional.of(giftCertificate));
        giftCertificateService.deleteById(id);
        verify(giftCertificateRepository).delete(giftCertificate);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testDeleteByIdShouldThrowNoSuchEntityExceptionWhenNotFound(){
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
        tag = null;
    }
}
