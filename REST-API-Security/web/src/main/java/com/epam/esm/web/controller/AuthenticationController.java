package com.epam.esm.web.controller;

import com.epam.esm.service.dto.GeneratedJwtDto;
import com.epam.esm.service.dto.LoginRequestDto;
import com.epam.esm.service.dto.OAuthSignUpDto;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.jwt.JwtTokenProvider;
import com.epam.esm.service.service.UserService;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
public class AuthenticationController {

    private static final String OAUTH_BASE_URL = "/oauth2/authorization/";

    private final UserService userService;
    private final LinkAdder<UserDto> userLinkAdder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public AuthenticationController(UserService userService, LinkAdder<UserDto> userLinkAdder,
                                    AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                                    ClientRegistrationRepository clientRegistrationRepository) {
        this.userService = userService;
        this.userLinkAdder = userLinkAdder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto signUp(@RequestBody @Valid UserDto userDto) {
        userDto = userService.create(userDto);
        userLinkAdder.addLinks(userDto);
        return userDto;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public GeneratedJwtDto login(@RequestBody LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        UserDto userDto = userService.getByUsername(username);
        String jwt = jwtTokenProvider.createToken(userDto.getUsername(), userDto.getRole());
        String refreshJwt = jwtTokenProvider.createRefreshToken(userDto.getUsername(), userDto.getRole());
        return GeneratedJwtDto.builder()
                .username(userDto.getUsername())
                .role(userDto.getRole())
                .jwt(jwt)
                .jwtRefresh(refreshJwt)
                .build();
    }

    @GetMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('refresh:token')")
    public Map<String, Object> refreshJwtToken(HttpServletRequest httpServletRequest) {
        return jwtTokenProvider.refreshToken(httpServletRequest);
    }

    @GetMapping("/oauth2/authorization")
    @ResponseStatus(HttpStatus.OK)
    public OAuthSignUpDto getOAuthRefs() {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        OAuthSignUpDto oauthLogin = new OAuthSignUpDto();
        if (clientRegistrations != null) {
            clientRegistrations.forEach(registration ->
                    oauthLogin.getRefs().put(registration.getClientName(),
                            OAUTH_BASE_URL + registration.getRegistrationId()));
        }
        return oauthLogin;
    }
}
