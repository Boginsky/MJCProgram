package com.epam.esm.service;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.model.dao.impl.TagDaoImpl;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.GiftCertificateValidatorImpl;
import com.epam.esm.service.validator.impl.TagValidatorImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GiftCertificateTest {

    private long id;
    private GiftCertificateValidatorImpl giftCertificateValidator;
    private Validator<Tag> tagValidator;
    private GiftCertificateDao giftCertificateDao;
    private TagDao tagDao;
    private GiftCertificateService giftCertificateService;
    private GiftCertificate giftCertificate;
    private GiftCertificateDto giftCertificateDto;

    @BeforeAll
    public void setUp() {
        id = 1l;
        giftCertificateValidator = Mockito.mock(GiftCertificateValidatorImpl.class);
        tagValidator = Mockito.mock(TagValidatorImpl.class);
        giftCertificateDao = Mockito.mock(GiftCertificateDaoImpl.class);
        tagDao = Mockito.mock(TagDaoImpl.class);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao
                , giftCertificateValidator, tagValidator);
        giftCertificate = new GiftCertificate(id, "name",
                "description", BigDecimal.TEN, 5, ZonedDateTime.now(), ZonedDateTime.now());
        giftCertificateDto = new GiftCertificateDto(giftCertificate);
    }

    @Test
    public void testCreateShouldThrowsExceptionWhenNotValid() {
        when(giftCertificateValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidEntityException.class, () -> giftCertificateService.create(giftCertificateDto));
    }

    @Test
    public void testCreateShouldThrowsExceptionWhenExist() {
        when(giftCertificateValidator.isValid(any())).thenReturn(true);
        when(giftCertificateDao.getByName(anyString())).thenReturn(Optional.of(giftCertificate));
        assertThrows(DuplicateEntityException.class, () -> giftCertificateService.create(giftCertificateDto));
    }

    @Test
    public void testGetAllShouldGetAll() {
        giftCertificateService.getAll();
        verify(giftCertificateDao, atLeast(2)).getAll();
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.of(giftCertificate));
        giftCertificateService.getById(id);
        verify(giftCertificateDao, atLeast(2)).getById(id);
    }

    @Test
    public void getAllWithTagsShouldGetAllWithTags() {
        giftCertificateService.getAllWithTags();
        verify(giftCertificateDao).getAll();
    }

    @Test
    public void testUpdateByIdShouldUpdateWhenFound() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.of(giftCertificate));
        when((giftCertificateValidator.isNameValid(anyString()))).thenReturn(true);
        when((giftCertificateValidator.isDescriptionValid(anyString()))).thenReturn(true);
        when((giftCertificateValidator.isDurationValid(anyInt()))).thenReturn(true);
        when((giftCertificateValidator.isPriceValid(any()))).thenReturn(true);
        giftCertificateService.updateById(id, giftCertificateDto);
        verify(giftCertificateDao).updateById(anyLong(), any());
    }

    @Test
    public void testUpdateByIdShouldThrowsExceptionWhenNotFound() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.updateById(id, giftCertificateDto));
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.of(giftCertificate));
        giftCertificateService.deleteById(id);
        verify(giftCertificateDao).deleteById(id);
    }

    @Test
    public void testDeleteByIdShouldThrowsExceptionWhenNotFound() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.deleteById(id));
    }

    @AfterAll
    public void tierDown() {
        giftCertificateValidator = null;
        tagValidator = null;
        giftCertificateDao = null;
        tagDao = null;
        giftCertificateService = null;
        giftCertificate = null;
        giftCertificateDto = null;
    }
}
