package com.epam.esm.service;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.impl.TagDaoImpl;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.service.impl.TagServiceImpl;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.TagValidatorImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TagServiceImplTest {
    private Long id;
    private Tag tag;
    private TagDao tagDao;
    private Validator<Tag> tagValidator;
    private TagServiceImpl tagService;

    @BeforeAll
    public void setUp(){
        id = 1l;
        tag = new Tag(id,"tag");
        tagDao = Mockito.mock(TagDaoImpl.class);
        tagValidator = Mockito.mock(TagValidatorImpl.class);
        tagService = new TagServiceImpl(tagDao,tagValidator);
    }

    @Test
    public void testCreateShouldCreateWhenValidAndNotExist() throws ServiceException {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagDao.findByName(anyString())).thenReturn(Optional.empty());
        tagService.create(tag);
        verify(tagDao).create(tag);
    }

    @Test
    public void testCreateShouldThrowServiceExceptionWhenInvalid(){
        when(tagValidator.isValid((any()))).thenReturn(false);
        assertThrows(ServiceException.class, () -> tagService.create(tag));
    }

    @Test
    public void testGetAllShouldGetAll(){
        tagService.getAll();
        verify(tagDao).getAll();
    }

    @Test
    public void testGetByIdShouldGetWhenFound() throws ServiceException {
        when(tagDao.findById(anyLong())).thenReturn(Optional.of(tag));
        tagService.findById(id);
        verify(tagDao).findById(id);
    }

    @Test
    public void testCreateShouldThrowServiceExceptionWhenExists(){
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagDao.findByName(anyString())).thenReturn(Optional.of(tag));
        assertThrows(ServiceException.class, () -> tagService.create(tag));
    }

    @Test
    public void testGetByIdShouldThrowServiceExceptionWhenNotFound(){
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> tagService.findById(id));
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() throws ServiceException {
        when(tagDao.findById(anyLong())).thenReturn(Optional.of(tag));
        tagService.deleteById(id);
        verify(tagDao).deleteById(id);
    }

    @Test
    public void testDeleteByIdShouldThrowsServiceExceptionWhenDoesntExist(){
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> tagService.deleteById(id));
    }

    @AfterAll
    public void tierDown(){
        id = null;
        tag = null;
        tagDao = null;
        tagValidator = null;
        tagService = null;
    }

}
