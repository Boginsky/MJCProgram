package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.UserService;
import com.epam.esm.service.util.CommonUtil;
import com.epam.esm.service.util.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.model.entity.Role.USER;

@Service
@Validated
@Transactional
public class UserServiceImpl implements UserService {

    @Qualifier("userDtoConverter")
    private final DtoConverter<User, UserDto> userDtoConverter;
    private final UserRepository userRepository;
    @Qualifier("userValidator")
    private final UserValidator userValidator;
    @Qualifier("bcryptPasswordEncoder")
    private final PasswordEncoder passwordEncoder;
    private final CommonUtil commonUtil;


    @Autowired
    public UserServiceImpl(DtoConverter<User, UserDto> userDtoConverter,
                           UserRepository userRepository, UserValidator userValidator,
                           PasswordEncoder passwordEncoder, CommonUtil commonUtil) {
        this.userDtoConverter = userDtoConverter;
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.commonUtil = commonUtil;
    }

    @Override
    public CustomPage<UserDto> getRoute(Long userId, Integer page, Integer size) {
        if (userId != null) {
            return getById(userId);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    public UserDto create(UserDto userDto) {
        validateFields(userDto);
        isExistByEmail(userDto.getEmail());
        isExistByUsername(userDto.getUsername());
        User user = buildUser(userDto);
        user = userRepository.save(user);
        return userDtoConverter.convertToDto(user);
    }

    @Override
    public CustomPage<UserDto> getById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("message.user.missing");
        }
        List<UserDto> userDtoList = new ArrayList<>();
        UserDto userDto = userDtoConverter.convertToDto(userOptional.get());
        userDtoList.add(userDto);
        return CustomPage.<UserDto>builder()
                .content(userDtoList)
                .build();
    }

    @Override
    public CustomPage<UserDto> getAll(Integer page, Integer size) {
        Pageable pageable = commonUtil.getPageable(page, size, Sort.unsorted());
        Page<User> userCustomPage = userRepository.findAll(pageable);
        return userDtoConverter.convertContentToDto(userCustomPage);
    }


    @Override
    public UserDto getByUsername(String username) {
        User user = isPresentByUsername(username);
        return userDtoConverter.convertToDto(user);
    }

    @Override
    public void deleteById(Long id) { // FIXME: 24.01.2022
        User user = isPresentById(id);
        userRepository.delete(user);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return Optional.empty();
        }
        return userOptional;
    }

    private void validateFields(UserDto userDto) {
        userValidator.validateFirstName(userDto);
        userValidator.validateLastName(userDto);
    }

    private void isExistByEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEntityException("message.user.existent");
        }
    }

    private void isExistByUsername(String userName) {
        if (userRepository.findByUsername(userName).isPresent()) {
            throw new NoSuchEntityException("message.user.missing");
        }
    }

    private User isPresentById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("message.user.missing");
        }
        return userOptional.get();
    }

    private User isPresentByUsername(String userName) {
        Optional<User> userOptional = userRepository.findByUsername(userName);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("message.user.missing");
        }
        return userOptional.get();
    }

    private User buildUser(UserDto userDto) {
        User user = userDtoConverter.convertToEntity(userDto);
        user.setRole(USER);
        user.setStatus(true);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return user;
    }
}

