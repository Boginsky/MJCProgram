package com.epam.esm.service.security;

import com.epam.esm.model.entity.User;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("securityUserService")
public class SecurityUserService implements UserDetailsService {

    private final UserService userService;
    @Qualifier("userDtoConverter")
    private final DtoConverter<User, UserDto> userDtoConverter;

    @Autowired
    public SecurityUserService(UserService userService, DtoConverter<User, UserDto> userDtoConverter) {
        this.userService = userService;
        this.userDtoConverter = userDtoConverter;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDto userDto = userService.getByUsername(userName);
        User user = userDtoConverter.convertToEntity(userDto);
        if (user == null) {
            throw new UsernameNotFoundException("message.user.missing");
        }
        return SecurityUserDetailsFactory.create(user);
    }
}
