package com.epam.esm.web.security;

import com.epam.esm.web.exception.ExceptionControllerAdviser;
import com.epam.esm.web.exception.ExceptionResponse;
import com.epam.esm.web.filter.JwtTokenFilter;
import com.epam.esm.web.filter.ServletJsonResponseSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter jwtTokenFilter;
    private final ServletJsonResponseSender jsonResponseSender;
    private final ExceptionControllerAdviser exceptionControllerAdviser;
    private final OAuthAuthSuccessHandler oAuthAuthSuccessHandler;

    @Autowired
    public SecurityConfig(JwtTokenFilter jwtTokenFilter, ServletJsonResponseSender jsonResponseSender,
                          ExceptionControllerAdviser exceptionControllerAdviser, OAuthAuthSuccessHandler oAuthAuthSuccessHandler) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.jsonResponseSender = jsonResponseSender;
        this.exceptionControllerAdviser = exceptionControllerAdviser;
        this.oAuthAuthSuccessHandler = oAuthAuthSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users/**", "/tags/**", "/certificates/**").permitAll()
                .antMatchers("/login", "/sign-up", "/oauth/sign-up", "/jwt-public-key").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> handleNoJwt(request, response)
                )
                .and()
                .oauth2Login()
                .successHandler(oAuthAuthSuccessHandler::handle)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private void handleNoJwt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        Locale locale = httpServletRequest.getLocale();
        ExceptionResponse responseObject = exceptionControllerAdviser.buildNoJwtResponseObject(locale);
        jsonResponseSender.send(httpServletResponse, responseObject);
    }
}
