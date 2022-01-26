package com.epam.esm;

import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.impl.UserServiceImpl;
import com.epam.esm.service.util.validator.UserValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserServiceImpl.class})
public class UserServiceImplTest {

    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private UserDto userDto;
    private User user;
    private Integer defaultPage;
    private Integer defaultPageSize;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DtoConverter<User, UserDto> userDtoConverter;

    @MockBean
    private UserValidator userValidator;

    @Autowired
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        id = 1L;
        name = "userName";
        password = "password";
        username = "username";
        email = "email";
        userDto = new UserDto(id, name, name, username, email, password, true, Role.USER);
        user = User.builder()
                .id(id)
                .firstName(name)
                .lastName(name)
                .build();
        defaultPage = 0;
        defaultPageSize = 10;
    }

    @Test
    public void testCreateShouldCreate() {
        userService.create(userDto);
        User user = userDtoConverter.convertToEntity(userDto);
        verify(userRepository).save(user);
    }

    @Test
    public void testGetAllShouldGetAll() {
        userService.getAll(defaultPage, defaultPageSize);
        verify(userRepository).findAll();
    }

    @Test(expected = InvalidParametersException.class)
    public void testGetAllShouldThrowInvalidParameterExceptionWhenParametersInvalid() {
        userService.getAll(-10, -10);
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        userService.getById(id);
        verify(userRepository).findById(id);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetByIdShouldThrowsNoSuchEntityExceptionWhenNotFound() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        userService.getById(id);
    }

    @After
    public void tierDown() {
        id = null;
        name = null;
        userDto = null;
        user = null;
        defaultPage = null;
        defaultPageSize = null;
    }
}
