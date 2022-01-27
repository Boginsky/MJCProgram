package com.epam.esm.service.jwt;

import com.epam.esm.model.entity.Role;
import com.epam.esm.service.exception.InvalidJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final String SIGN_ALGO_NAME = "RSA";
    private static final String AUTHORIZATION_TYPE_STR = "Bearer";

    private final UserDetailsService userDetailsService;
    private KeyPair keys;
    @Value("${jwt.duration}")
    private long durationTime;
    @Value("${jwt.refresh.duration}")
    private long durationRefreshTime;
    @Value("${jwt.refresh.header}")
    private String authHeader;


    @Autowired
    public JwtTokenProvider(@Qualifier("securityUserService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() throws NoSuchAlgorithmException {
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(SIGN_ALGO_NAME);
            keyGenerator.initialize(2048);
            keys = keyGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException ignore) {
            throw new NoSuchAlgorithmException();
        }
    }

    public String createToken(String username, Role role) {
        Claims claims = Jwts.claims();
        claims.setSubject(username);
        claims.put("role", role);
        Date currentTime = new Date();
        Date expirationTime = new Date(currentTime.getTime() + durationTime);
        return AUTHORIZATION_TYPE_STR + " " + Jwts.builder() // FIXME: 27.01.2022
                .setClaims(claims)
                .setIssuedAt(currentTime)
                .setExpiration(expirationTime)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.RS512, keys.getPrivate())
                .compact();
    }

    public String createRefreshToken(String username, Role role) {
        Claims claims = Jwts.claims();
        claims.setSubject(username);
        claims.put("role", role);
        Date currentTime = new Date();
        Date expirationTime = new Date(currentTime.getTime() + durationRefreshTime);
        return AUTHORIZATION_TYPE_STR+ " " + Jwts.builder() // FIXME: 27.01.2022
                .setClaims(claims)
                .setIssuedAt(currentTime)
                .setExpiration(expirationTime)
                .setHeaderParam("typ", "JWTRefresh")
                .signWith(SignatureAlgorithm.RS512, keys.getPrivate())
                .compact();
    }

    public Map<String, Object> refreshToken(HttpServletRequest httpServletRequest) {
        String token = resolveToken(httpServletRequest);
        Map<String, Object> map = new HashMap<>();
        String username = extractUsernameFromJwt(token);
        Role role = Role.valueOf((String) Jwts.parser().setSigningKey(keys.getPublic())
                .parseClaimsJws(token)
                .getBody()
                .get("role"));
        String jwt = createToken(username, role);
        String jwtRefresh = createRefreshToken(username, role);
        map.put("jwt", jwt);
        map.put("jwtRefresh", jwtRefresh);
        return map;
    }

    public void validateToken(String token) {
        PublicKey publicKey = keys.getPublic();
        try {
            Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getHeader();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtException("message.jwt.invalid");
        }
    }

    public Authentication getAuthentication(String token) {
        String username = extractUsernameFromJwt(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String extractUsernameFromJwt(String token) {
        return Jwts.parser().setSigningKey(keys.getPublic())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean extractHeaderFromJwt(String token) {
        String jwtHeader = (String) Jwts.parser().setSigningKey(keys.getPublic())
                .parseClaimsJws(token)
                .getHeader()
                .get("typ");
        return jwtHeader.equals("JWTRefresh");
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        String authToken = httpServletRequest.getHeader(authHeader);
        if (authToken != null && authToken.startsWith(AUTHORIZATION_TYPE_STR)) {
            return authToken.substring(AUTHORIZATION_TYPE_STR.length() + 1);
        }
        return null;
    }
}
