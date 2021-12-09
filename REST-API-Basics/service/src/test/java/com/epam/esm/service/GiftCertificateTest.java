package com.epam.esm.service;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.model.dao.impl.TagDaoImpl;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.GiftCertificateValidatorImpl;
import com.epam.esm.service.validator.impl.TagValidatorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GiftCertificateTest {

    private Validator<GiftCertificate> giftCertificateValidator;
    private Validator<Tag> tagValidator;
    private GiftCertificateDao giftCertificateDao;
    private TagDao tagDao;
    private GiftCertificateService giftCertificateService;
    private GiftCertificate giftCertificate;
    private GiftCertificateDto giftCertificateDto;

    @BeforeAll
    public void setUp(){
        giftCertificateValidator = Mockito.mock(GiftCertificateValidatorImpl.class);
        tagValidator = Mockito.mock(TagValidatorImpl.class);
        giftCertificateDao = Mockito.mock(GiftCertificateDaoImpl.class);
        tagDao = Mockito.mock(TagDaoImpl.class);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao,tagDao
                ,giftCertificateValidator,tagValidator);
        giftCertificate = new GiftCertificate(1l, "name",
                "description", BigDecimal.TEN, 5, ZonedDateTime.now(), ZonedDateTime.now());
        giftCertificateDto = new GiftCertificateDto(giftCertificate);
    }

    @Test
    public void testCreateShouldCreateWhenValidAndDoesntExist() throws ServiceException {
        when(giftCertificateValidator.isValid(any())).thenReturn(true);
        when(tagValidator.isValid(any())).thenReturn(true);
        when(giftCertificateDao.findByName(anyString())).thenReturn(Optional.empty());
        giftCertificateService.create(giftCertificateDto);
        verify(giftCertificateDao).create(giftCertificate);
    }
}
