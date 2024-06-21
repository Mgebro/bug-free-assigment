package com.croco.interview.core.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.List;

@Slf4j
public final class JWTUtils {

    public static final long MILLISECONDS_IN_HOUR = 3_600_000;
    private static final Algorithm ALGORITHM;
    private static final JWTVerifier VERIFIER;
    private static final String ISSUER = "croco.com";
    private static final String RSA = "RSA";

    static {
        final ClassLoader classLoader = JWTUtils.class.getClassLoader();
        try (
                final InputStream publicKeyAsStream = classLoader.getResourceAsStream("keys/public_key.der");
                final InputStream privateKeyAsStream = classLoader.getResourceAsStream("keys/private_key.der")
        ) {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            RSAPublicKey publicKey;
            RSAPrivateKey privateKey;
            if (publicKeyAsStream != null && privateKeyAsStream != null) {
                publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyAsStream.readAllBytes()));
                privateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyAsStream.readAllBytes()));
            } else {
                throw new IllegalStateException("Couldn't read keys!");
            }

            ALGORITHM = Algorithm.RSA256(publicKey, privateKey);
            VERIFIER = JWT.require(ALGORITHM).withIssuer(ISSUER).build();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException("Couldn't execute algorithm initialisation for JWT generation!", e);
        }
    }

    public static String generate(String username, List<String> roles) {
        var issuedAt = Instant.now();
        var builder = JWT.create()
                .withIssuer(ISSUER)
                .withSubject(username)
                .withIssuedAt(issuedAt)
                .withClaim("roles", roles)
                .withExpiresAt(issuedAt.plusMillis(MILLISECONDS_IN_HOUR));

        return builder.sign(ALGORITHM);
    }

    @Nullable
    public static DecodedJWT verify(String token) {
        DecodedJWT result = null;
        try {
            result = VERIFIER.verify(token);
        } catch (JWTVerificationException e) {
            log.warn("Failed verification for provided JWT token, with error message: '%s'!".formatted(e.getLocalizedMessage()));
        }
        return result;
    }
}
