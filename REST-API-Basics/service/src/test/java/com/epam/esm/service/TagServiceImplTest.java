package com.epam.esm.service;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.impl.TagServiceImpl;
import com.epam.esm.service.validator.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TagServiceImplTest {
    private Long id;
    private Tag tag;
    private TagDao tagDao;
    private Validator<Tag> tagValidator;
    private TagServiceImpl tagService;

//    @BeforeAll
//    public void setUp() {
//        id = 1l;
//        tag = new Tag(id, "tag");
//        tagDao = Mockito.mock(TagDaoImpl.class);
//        tagValidator = Mockito.mock(TagValidatorImpl.class);
//        tagService = new TagServiceImpl(tagDao, tagValidator);
//    }

    @Test
    public void testCreateShouldCreateWhenValidAndNotExist() {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagDao.getByName(anyString())).thenReturn(Optional.empty());
        tagService.create(tag);
        verify(tagDao).create(tag);
    }

    @Test
    public void testCreateShouldThrowInvalidEntityExceptionWhenInvalid() {
        when(tagValidator.isValid((any()))).thenReturn(false);
        assertThrows(InvalidEntityException.class, () -> tagService.create(tag));
    }

//    @Test
//    public void testGetAllShouldGetAll() {
//        tagService.getAll();
//        verify(tagDao).getAll();
//    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(tagDao.getById(anyLong())).thenReturn(Optional.of(tag));
        tagService.getById(id);
        verify(tagDao).getById(id);
    }

    @Test
    public void testCreateShouldThrowDuplicateEntityExceptionWhenExists() {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagDao.getByName(anyString())).thenReturn(Optional.of(tag));
        assertThrows(DuplicateEntityException.class, () -> tagService.create(tag));
    }

    @Test
    public void testGetByIdShouldThrowServiceExceptionWhenNotFound() {
        when(tagDao.getById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> tagService.getById(id));
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() {
        when(tagDao.getById(anyLong())).thenReturn(Optional.of(tag));
        tagService.deleteById(id);
        verify(tagDao).deleteById(id);
    }

    @Test
    public void testDeleteByIdShouldThrowsServiceExceptionWhenDoesntExist() {
        when(tagDao.getById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> tagService.deleteById(id));
    }

    @AfterAll
    public void tierDown() {
        id = null;
        tag = null;
        tagDao = null;
        tagValidator = null;
        tagService = null;
    }

}
