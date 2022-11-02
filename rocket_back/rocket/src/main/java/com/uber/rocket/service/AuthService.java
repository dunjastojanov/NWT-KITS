package com.uber.rocket.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class AuthService {
    @Value("${secret}")
    private String secret;
    private final static int FIFTEEN_DAYS = 15 * 24 * 60 * 60 * 1000;

    public String makeAccessToken(UserDetails user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + FIFTEEN_DAYS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public DecodedJWT getDecodedJWT(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }

    public String getUsernameFromJWT(String authorizationHeader) {
        return getDecodedJWT(authorizationHeader).getSubject();
    }

    public Collection<SimpleGrantedAuthority> getAuthorityClaimsFromJWT(String authorizationHeader) {
        DecodedJWT decodedJWT = getDecodedJWT(authorizationHeader);
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }
}
