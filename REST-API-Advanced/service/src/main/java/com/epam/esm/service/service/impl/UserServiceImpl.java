package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class UserServiceImpl implements UserService {

    @Qualifier("giftCertificateConverter")
    private final DtoConverter<User, UserDto> userDtoConverter;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(DtoConverter<User, UserDto> userDtoConverter,
                           UserRepository userRepository) {
        this.userDtoConverter = userDtoConverter;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getRoute(Long userId, Integer page, Integer size) {
        if (userId != null) {
            return getById(userId);
        } else {
            return getAll(page, size);
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
    public List<UserDto> getById(Long userId) {
        Optional<User> userOptional = userRepository.getByField("id", userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("message.user.missing");
        }
        List<UserDto> userDtoList = new ArrayList<>();
        UserDto userDto = userDtoConverter.convertToDto(userOptional.get());
        userDtoList.add(userDto);
        return userDtoList;
    }

    @Override
    public List<UserDto> getAll(Integer page, Integer size) {
        Pageable pageable;
        try {
            pageable = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        List<User> userList = userRepository.getAll(pageable);
        return userList.stream()
                .map(userDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    private void validateFields(UserDto userDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        validateFirstName(userDto, validator);
        validateLastName(userDto, validator);
    }

    private void validateLastName(UserDto userDto, Validator validator) {
        String lastName = userDto.getLastName();
        if (lastName != null) {
            validateField(validator, "lastName", lastName);
        }
    }

    private void validateFirstName(UserDto userDto, Validator validator) {
        String firstName = userDto.getFirstName();
        if (firstName != null) {
            validateField(validator, "firstName", firstName);
        }
    }

    private void validateField(Validator validator, String propertyName, Object value) {
        Set<ConstraintViolation<UserDto>> violations = validator.validateValue(
                UserDto.class, propertyName, value);
        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            throw new InvalidEntityException(message);
        }
    }
}

