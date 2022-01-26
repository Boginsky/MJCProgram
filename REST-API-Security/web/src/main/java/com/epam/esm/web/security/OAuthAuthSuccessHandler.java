package com.epam.esm.web.security;

import com.epam.esm.model.entity.User;
import com.epam.esm.service.dto.GeneratedJwtDto;
import com.epam.esm.service.dto.converter.impl.UserDtoConverter;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.jwt.JwtTokenProvider;
import com.epam.esm.service.service.UserService;
import com.epam.esm.web.exception.ExceptionControllerAdviser;
import com.epam.esm.web.exception.ExceptionResponse;
import com.epam.esm.web.filter.ServletJsonResponseSender;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuthAuthSuccessHandler {

    private final UserService userService;
    private final UserDtoConverter userDtoConverter;
    private final ServletJsonResponseSender jsonResponseSender;
    private final ExceptionControllerAdviser exceptionControllerAdviser;
    private final LinkAdder<UserDto> userDtoLinkAdder;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public OAuthAuthSuccessHandler(UserService userService, UserDtoConverter userDtoConverter,
                                   ServletJsonResponseSender jsonResponseSender, ExceptionControllerAdviser exceptionControllerAdviser,
                                   LinkAdder<UserDto> userDtoLinkAdder, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.userDtoConverter = userDtoConverter;
        this.jsonResponseSender = jsonResponseSender;
        this.exceptionControllerAdviser = exceptionControllerAdviser;
        this.userDtoLinkAdder = userDtoLinkAdder;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    public void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser principalUser = (OidcUser) authentication.getPrincipal();
            User user = mapOidUserToUser(principalUser);
            Optional<User> optionalUser = userService.getByEmail(user.getEmail());
            if (optionalUser.isPresent()) {
                loginOauth(response, optionalUser);
            } else {
                signUpOauth(request, response, user);
            }
        }
    }

    private void signUpOauth(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            String decodePassword = user.getPassword();
            UserDto userDto = userDtoConverter.convertToDtoForOauth2(user);
            userDto = userService.create(userDto);
            userDtoLinkAdder.addLinks(userDto);
            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("userInfo", userDto);
            responseObject.put("generatedPassword", decodePassword);
            jsonResponseSender.send(response, responseObject);
        } catch (DuplicateEntityException e) {
            ExceptionResponse responseObject = exceptionControllerAdviser.handleServiceException(e, request.getLocale()).getBody();
            jsonResponseSender.send(response, responseObject);
        } catch (IOException e) {
            ExceptionResponse responseObject = exceptionControllerAdviser.handleGlobalException(e, request.getLocale()).getBody();
            jsonResponseSender.send(response, responseObject);
        }
    }

    private void loginOauth(HttpServletResponse response, Optional<User> optionalUser) throws IOException {
        UserDto userDto = userDtoConverter.convertToDto(optionalUser.get());
        String jwt = jwtTokenProvider.createToken(userDto.getUsername(), userDto.getRole());
        String jwtRefresh = jwtTokenProvider.createRefreshToken(userDto.getUsername(), userDto.getRole());
        GeneratedJwtDto generatedJwtDto = GeneratedJwtDto.builder()
                .username(userDto.getUsername())
                .role(userDto.getRole())
                .jwt(jwt)
                .jwtRefresh(jwtRefresh)
                .build();
        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("userInfo", generatedJwtDto);
        jsonResponseSender.send(response, responseObject);
    }

    private User mapOidUserToUser(OidcUser oidcUser) {
        User user = new User();
        user.setUsername(oidcUser.getEmail());
        user.setFirstName(oidcUser.getGivenName());
        user.setLastName(oidcUser.getFamilyName());
        user.setEmail(oidcUser.getEmail());
        user.setPassword((String.valueOf(user.getEmail().hashCode() & 0x7fffffff)));
        return user;
    }
}
