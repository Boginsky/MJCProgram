package com.epam.esm.service;

import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GiftCertificateTest {
//
//    private long id;
//    private GiftCertificateValidatorImpl giftCertificateValidator;
//    private Validator<Tag> tagValidator;
//    private GiftCertificateDao giftCertificateDao;
//    private TagDao tagDao;
//    private GiftCertificateService giftCertificateService;
//    private GiftCertificate giftCertificate;
//    private GiftCertificateDto giftCertificateDto;
//
//    @BeforeAll
//    public void setUp() {
//        id = 1l;
//        giftCertificateValidator = Mockito.mock(GiftCertificateValidatorImpl.class);
////        tagValidator = Mockito.mock(TagValidatorImpl.class);
//        giftCertificateDao = Mockito.mock(GiftCertificateDaoImpl.class);
//        tagDao = Mockito.mock(TagDaoImpl.class);
////        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao
////                , giftCertificateValidator, tagValidator);
//        giftCertificate = new GiftCertificate(id, "name",
//                "description", BigDecimal.TEN, 5, ZonedDateTime.now(), ZonedDateTime.now());
////        giftCertificateDto = new GiftCertificateDto(giftCertificate);
//    }
//
//    @Test
//    public void testCreateShouldThrowsExceptionWhenNotValid() {
//        when(giftCertificateValidator.isValid(any())).thenReturn(false);
//        assertThrows(InvalidEntityException.class, () -> giftCertificateService.create(giftCertificateDto));
//    }
//
//    @Test
//    public void testCreateShouldThrowsExceptionWhenExist() {
//        when(giftCertificateValidator.isValid(any())).thenReturn(true);
//        when(giftCertificateDao.getByName(anyString())).thenReturn(Optional.of(giftCertificate));
//        assertThrows(DuplicateEntityException.class, () -> giftCertificateService.create(giftCertificateDto));
//    }
//
////    @Test
////    public void testGetAllShouldGetAll() {
////        giftCertificateService.getAll();
////        verify(giftCertificateDao, atLeast(2)).getAll();
////    }
//
//    @Test
//    public void testGetByIdShouldGetWhenFound() {
//        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.of(giftCertificate));
//        giftCertificateService.getById(id);
//        verify(giftCertificateDao, atLeast(2)).getById(id);
//    }
//
////    @Test
////    public void getAllWithTagsShouldGetAllWithTags() {
////        giftCertificateService.getAllWithTags();
////        verify(giftCertificateDao).getAll();
////    }
//
//    @Test
//    void testUpdateByIdShouldUpdateWhenFound() {
//        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.of(giftCertificate));
//        when((giftCertificateValidator.isNameValid(anyString()))).thenReturn(true);
//        when((giftCertificateValidator.isDescriptionValid(anyString()))).thenReturn(true);
//        when((giftCertificateValidator.isDurationValid(anyInt()))).thenReturn(true);
//        when((giftCertificateValidator.isPriceValid(any()))).thenReturn(true);
//        giftCertificateService.updateById(id, giftCertificateDto);
//        verify(giftCertificateDao).updateById(anyLong(), any());
//    }
//
//    @Test
//    void testUpdateByIdShouldThrowsExceptionWhenNotFound() {
//        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.empty());
//        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.updateById(id, giftCertificateDto));
//    }
//
//    @Test
//    void testDeleteByIdShouldDeleteWhenFound() {
//        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.of(giftCertificate));
//        giftCertificateService.deleteById(id);
//        verify(giftCertificateDao).deleteById(id);
//    }
//
//    @Test
//    void testDeleteByIdShouldThrowsExceptionWhenNotFound() {
//        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.empty());
//        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.deleteById(id));
//    }
//
//    @AfterAll
//    public void tierDown() {
//        giftCertificateValidator = null;
//        tagValidator = null;
//        giftCertificateDao = null;
//        tagDao = null;
//        giftCertificateService = null;
//        giftCertificate = null;
//        giftCertificateDto = null;
//    }
}
