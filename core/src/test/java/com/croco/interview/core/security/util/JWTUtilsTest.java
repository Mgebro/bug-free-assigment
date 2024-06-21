package com.croco.interview.core.security.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class JWTUtilsTest {

    @Test
    void generate() {
        Assertions.assertNotNull(JWTUtils.generate("test", Collections.singletonList("CLIENT")));
    }

    @Test
    void verify() {
        String generatedToken = JWTUtils.generate("test", Collections.singletonList("ADMIN"));

        Assertions.assertNotNull(generatedToken);

        var decodedJWT = JWTUtils.verify(generatedToken);

        Assertions.assertNotNull(decodedJWT);
        Assertions.assertNotNull(decodedJWT.getClaims());
        Assertions.assertEquals("test", decodedJWT.getClaim("sub").asString());
        Assertions.assertEquals("ADMIN", decodedJWT.getClaim("roles").asArray(String.class)[0]);
    }
}
