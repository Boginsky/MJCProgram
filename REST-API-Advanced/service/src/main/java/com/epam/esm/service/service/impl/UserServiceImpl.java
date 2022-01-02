package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class UserServiceImpl implements UserService {

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
        User user = userDtoConverter.convertToEntity(userDto);
        user = userRepository.create(user);
        return userDtoConverter.convertToDto(user);
    }

    @Override
    public List<UserDto> getById(Long userId) {
        Optional<User> userOptional = userRepository.getByField("id", userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("message.cantFindUser");
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
}

