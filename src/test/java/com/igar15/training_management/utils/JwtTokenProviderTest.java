package com.igar15.training_management.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.igar15.training_management.constants.SecurityConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private Environment environment;

    private String testEmail = "test@test.com";

    @Test
    void generateEmailVerificationToken() {
        String token = jwtTokenProvider.generateEmailVerificationToken(testEmail);
        checkToken(testEmail, token);
    }

    @Test
    void isTokenExpiredWhereTokenNotExpired() {
        String token = JWT.create()
                .withSubject(testEmail)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.PASSWORD_RESET_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(environment.getProperty("jwt.secretKey")));
        Assertions.assertFalse(jwtTokenProvider.isTokenExpired(token));
    }

    @Test
    void isTokenExpiredWhereTokenExpired() {
        String token = JWT.create()
                .withSubject(testEmail)
                .withExpiresAt(new Date(System.currentTimeMillis() - SecurityConstant.PASSWORD_RESET_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(environment.getProperty("jwt.secretKey")));
        Assertions.assertThrows(TokenExpiredException.class, () -> jwtTokenProvider.isTokenExpired(token));
    }

    @Test
    void generatePasswordResetToken() {
        String token = jwtTokenProvider.generatePasswordResetToken(testEmail);
        checkToken(testEmail, token);
    }



    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier = null;
        try {
            verifier = JWT.require(Algorithm.HMAC512(environment.getProperty("jwt.secretKey"))).build();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }

    private void checkToken(String testEmail, String token) {
        JWTVerifier jwtVerifier = getJWTVerifier();
        String email = jwtVerifier.verify(token).getSubject();
        Date expiresDate = jwtVerifier.verify(token).getExpiresAt();
        Assertions.assertEquals(testEmail, email);
        Assertions.assertTrue(expiresDate.after(new Date(System.currentTimeMillis())));
    }
}