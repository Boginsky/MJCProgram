package com.epam.esm.web.filter;

import com.epam.esm.service.exception.InvalidJwtException;
import com.epam.esm.service.jwt.JwtTokenProvider;
import com.epam.esm.web.exception.ExceptionControllerAdviser;
import com.epam.esm.web.exception.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_TYPE_STR = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;
    private final ExceptionControllerAdviser exceptionControllerAdviser;
    private final ServletJsonResponseSender jsonResponseSender;
    @Value("${jwt.header}")
    private String authHeader;
    @Value("${jwt.refresh.header}")
    private String refreshAuthHeader;

    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, ExceptionControllerAdviser exceptionControllerAdviser,
                          ServletJsonResponseSender jsonResponseSender) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.exceptionControllerAdviser = exceptionControllerAdviser;
        this.jsonResponseSender = jsonResponseSender;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURL = ((HttpServletRequest) request).getRequestURI();
        String token = resolveToken((HttpServletRequest) request);
        String refreshToken = resolveRefreshToken((HttpServletRequest) request);
        try {
            if (token != null && refreshToken != null) {
                jwtTokenProvider.validateToken(token);
                if (!requestURL.contains("refresh-token")) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    if (authentication != null && !jwtTokenProvider.isRefreshToken(token)) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        throw new InvalidJwtException("message.jwt.invalid");
                    }
                } else {
                    throw new AccessDeniedException("message.forbidden");
                }
            }
            chain.doFilter(request, response);
        } catch (InvalidJwtException e) {
            Locale locale = request.getLocale();
            ExceptionResponse responseObject = exceptionControllerAdviser.handleJwtInvalidException(e, locale).getBody();
            jsonResponseSender.send((HttpServletResponse) response, responseObject);
        } catch (ExpiredJwtException e) {
            if (requestURL.contains("refresh-token")) {
                jwtTokenProvider.validateToken(refreshToken);
                if (jwtTokenProvider.isRefreshToken(refreshToken)) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, Collections.singleton(new SimpleGrantedAuthority("refresh:token")));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(request, response);
                }
            } else {
                Locale locale = request.getLocale();
                ExceptionResponse responseObject = exceptionControllerAdviser.handleExpiredJwtException(e, locale).getBody();
                jsonResponseSender.send((HttpServletResponse) response, responseObject);
            }
        } catch (AccessDeniedException e) {
            Locale locale = request.getLocale();
            ExceptionResponse responseObject = exceptionControllerAdviser.handleAccessDeniedException(e, locale).getBody();
            jsonResponseSender.send((HttpServletResponse) response, responseObject);
        }
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        String authToken = httpServletRequest.getHeader(authHeader);
        if (authToken != null && authToken.startsWith(AUTHORIZATION_TYPE_STR)) {
            return authToken.substring(AUTHORIZATION_TYPE_STR.length() + 1);
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest httpServletRequest) {
        String authToken = httpServletRequest.getHeader(refreshAuthHeader);
        if (authToken != null && authToken.startsWith(AUTHORIZATION_TYPE_STR)) {
            return authToken.substring(AUTHORIZATION_TYPE_STR.length() + 1);
        }
        return null;
    }
}
