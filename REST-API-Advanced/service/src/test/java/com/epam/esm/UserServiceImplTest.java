package com.epam.esm;

import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserServiceImpl.class})
public class UserServiceImplTest {

    private Long id;
    private String firstName;
    private String lastName;
    private UserDto userDto;
    private User user;
    private Integer defaultPage;
    private Integer defaultPageSize;

    @MockBean
    private UserRepositoryImpl userRepository;

    @MockBean
    private DtoConverter<User, UserDto> userDtoConverter;

    @Autowired
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        id = 1L;
        firstName = "userFirstName";
        lastName = "userFirstName";
        userDto = new UserDto(id, firstName, lastName);
        user = new User(id, firstName, lastName);
        defaultPage = 0;
        defaultPageSize = 10;
    }

    @Test
    public void testCreateShouldCreate() {
        userService.create(userDto);
        User user = userDtoConverter.convertToEntity(userDto);
        verify(userRepository).create(user);
    }

    @Test
    public void testGetAllShouldGetAll() {
        userService.getAll(defaultPage, defaultPageSize);
        verify(userRepository).getAll(any());
    }

    @Test(expected = InvalidParametersException.class)
    public void testGetAllShouldThrowInvalidParameterExceptionWhenParametersInvalid() {
        userService.getAll(-10, -10);
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(userRepository.getByField("id", id)).thenReturn(Optional.of(user));
        userService.getById(id);
        verify(userRepository).getByField("id", id);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetByIdShouldThrowsNoSuchEntityExceptionWhenNotFound() {
        when(userRepository.getByField("id", id)).thenReturn(Optional.empty());
        userService.getById(id);
    }

    @After
    public void tierDown() {
        id = null;
        firstName = null;
        lastName = null;
        userDto = null;
        user = null;
        defaultPage = null;
        defaultPageSize = null;
    }
}
