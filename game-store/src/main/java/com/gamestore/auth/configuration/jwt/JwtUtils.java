package com.gamestore.auth.configuration.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    @Value("${jwt.secret.user.key}")
    private String SECRET_USER_KEY;

    @Value("${jwt.expiration.time}")
    private long EXPIRATION_TIME;

    public String generateJwtToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(this.SECRET_KEY);

        String username = authentication.getName();

        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        try {
            return JWT.create()
                    .withIssuer(this.SECRET_USER_KEY)
                    .withSubject(username)
                    .withClaim("authorities", authorities)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .withJWTId(UUID.randomUUID().toString())
                    .withNotBefore(new Date(System.currentTimeMillis()))
                    .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.SECRET_USER_KEY)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token Invalid, not Authorized");
        }
    }

    public String extractUsername(DecodedJWT token) {
        return token.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT token, String claimName) {
        return token.getClaim(claimName);
    }

    public Map<String, Claim> extractAllClaims(DecodedJWT token) {
        return token.getClaims();
    }

    public Date extractExpiration(DecodedJWT token) {
        return token.getExpiresAt();
    }
}