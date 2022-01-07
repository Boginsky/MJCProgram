package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.UserService;
import com.epam.esm.service.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class UserServiceImpl implements UserService {

    @Qualifier("giftCertificateConverter")
    private final DtoConverter<User, UserDto> userDtoConverter;
    private final UserRepository userRepository;
    @Qualifier("userValidator")
    private final UserValidator userValidator;

    @Autowired
    public UserServiceImpl(DtoConverter<User, UserDto> userDtoConverter,
                           UserRepository userRepository, UserValidator userValidator) {
        this.userDtoConverter = userDtoConverter;
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public CustomPage<UserDto> getRoute(Long userId, Integer page, Integer size) {
        if (userId != null) {
            return getById(userId);
        } else {
            return getAll(page,size);
        }
    }

    @Override
    public UserDto create(UserDto userDto) {
        validateFields(userDto);
        User user = userDtoConverter.convertToEntity(userDto);
        user = userRepository.create(user);
        return userDtoConverter.convertToDto(user);
    }

    @Override
    public CustomPage<UserDto> getById(Long userId) {
        Optional<User> userOptional = userRepository.getByField("id", userId);
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
        Pageable pageable;
        try {
            pageable = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        CustomPage<User> userCustomPage = userRepository.getAll(pageable);
        return userDtoConverter.convertContentToDto(userCustomPage);
    }

    private void validateFields(UserDto userDto) {
        userValidator.validateFirstName(userDto);
        userValidator.validateLastName(userDto);
    }
}

