package com.igar15.training_management.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.igar15.training_management.constants.SecurityConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("#{jwt.secretKey}")
    private String secretKey;

    public String generateEmailVerificationToken(String email) {
        return JWT.create()
                .withIssuedAt(new Date())
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EMAIL_VERIFICATION_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public boolean isTokenExpired(String token) {
        JWTVerifier verifier = getJWTVerifier();
        Date expirationDate = verifier.verify(token).getExpiresAt();
        return expirationDate.before(new Date());
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier = null;
        try {
            verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }


}
