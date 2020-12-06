package com.igar15.training_management.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.igar15.training_management.constants.SecurityConstant;
import com.igar15.training_management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Autowired
    private Environment environment;

    public String generateEmailVerificationToken(String email) {
        return JWT.create()
                .withIssuer(SecurityConstant.TRAINING_MANAGEMENT_LLC)
                .withAudience(SecurityConstant.TRAINING_MANAGEMENT_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EMAIL_VERIFICATION_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(environment.getProperty("jwt.secretKey")));
    }

    public String generatePasswordResetToken(String email) {
        return JWT.create()
                .withIssuer(SecurityConstant.TRAINING_MANAGEMENT_LLC)
                .withAudience(SecurityConstant.TRAINING_MANAGEMENT_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.PASSWORD_RESET_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(environment.getProperty("jwt.secretKey")));
    }

    public String generateAuthorizationToken(UserPrincipal userPrincipal) {
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create()
                .withIssuer(SecurityConstant.TRAINING_MANAGEMENT_LLC)
                .withAudience(SecurityConstant.TRAINING_MANAGEMENT_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(SecurityConstant.ROLES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.AUTHORIZATION_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(environment.getProperty("jwt.secretKey")));
    }

    public boolean isTokenExpired(String token) {
        JWTVerifier verifier = getJWTVerifier();
        Date expirationDate = verifier.verify(token).getExpiresAt();
        return expirationDate.before(new Date());
    }

    public String getSubject(String token) {
        JWTVerifier jwtVerifier = getJWTVerifier();
        return jwtVerifier.verify(token).getSubject();
    }

    public boolean isTokenValid(String userEmail, String token) {
        JWTVerifier jwtVerifier = getJWTVerifier();
        return !userEmail.isEmpty() && !isTokenExpired(token);
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        List<String> authorities = userPrincipal.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
        return authorities.toArray(new String[0]);
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

}
