package com.epam.esm;

import com.epam.esm.model.entity.BestTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.model.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.impl.TagServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TagServiceImpl.class})
public class TagServiceImplTest {

    private Long id;
    private String name;
    private Tag tag;
    private BestTag bestTag;
    private BigDecimal totalPrice;
    private TagDto tagDto;
    private User user;
    private Integer defaultPage;
    private Integer defaultPageSize;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TagRepositoryImpl tagRepository;

    @MockBean
    private DtoConverter<Tag, TagDto> tagDtoConverter;

    @Autowired
    private TagServiceImpl tagService;

    @Before
    public void setUp() {
        id = 1L;
        name = "tagName";
        totalPrice = BigDecimal.TEN;
        tag = new Tag(id, name);
        user = new User(id, name, name);
        tagDto = new TagDto(id, name);
        bestTag = new BestTag(id, name, totalPrice);
        defaultPage = 0;
        defaultPageSize = 10;
    }

    @Test
    public void testCreateShouldCreateWhenNotExist() {
        when(tagRepository.getByName(name)).thenReturn(Optional.empty());
        when(tagDtoConverter.convertToEntity(any())).thenReturn(tag);
        tagService.create(tagDto);
        verify(tagRepository).create(tag);
    }

    @Test(expected = DuplicateEntityException.class)
    public void testCreateShouldThrowDuplicateEntityExceptionWhenExist() {
        when(tagRepository.getByName(name)).thenReturn(Optional.of(tag));
        when(tagDtoConverter.convertToEntity(any())).thenReturn(tag);
        tagService.create(tagDto);
    }

    @Test
    public void testGetAllShouldGetAll() {
        tagService.getAll(defaultPage, defaultPageSize);
        verify(tagRepository).getAll(any());
    }

    @Test(expected = InvalidParametersException.class)
    public void testGetAllShouldThrowsInvalidParametersExceptionWhenParamsInvalid() {
        tagService.getAll(-10, -10);
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(tagRepository.getByField("id", id)).thenReturn(Optional.of(tag));
        tagService.getById(id);
        verify(tagRepository).getByField("id", id);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetByIdShouldThrowNoSuchEntityExceptionWhenNoFound() {
        when(tagRepository.getByField("id", id)).thenReturn(Optional.empty());
        tagService.getById(id);
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() {
        when(tagRepository.getByField("id", id)).thenReturn(Optional.of(tag));
        tagService.deleteById(id);
        verify(tagRepository).delete(tag);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testDeleteByIdShouldThrowsNoSuchEntityExceptionWhenNotFound() {
        when(tagRepository.getByField("id", id)).thenReturn(Optional.empty());
        tagService.deleteById(id);
    }

    @Test
    public void testGetHighestCostTagShouldGetWhenFound() {
        when(userRepository.getByField("id", id)).thenReturn(Optional.of(user));
        when(tagRepository.getHighestCostTag(id)).thenReturn(Optional.of(bestTag));
        tagService.getHighestCostTag(id);
        verify(tagRepository).getHighestCostTag(eq(id));
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetHighestCostTagShouldThrowNoSuchEntityExceptionWhenNotFound() {
        when(userRepository.getByField("id", id)).thenReturn(Optional.of(user));
        tagService.getHighestCostTag(id);
        verify(tagRepository).getHighestCostTag(id);
    }

    @After
    public void tierDown() {
        id = null;
        name = null;
        tag = null;
        tagDto = null;
        defaultPage = null;
        ;
        defaultPageSize = null;
    }

}
