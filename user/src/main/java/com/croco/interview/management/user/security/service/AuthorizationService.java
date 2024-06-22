package com.croco.interview.management.user.security.service;

import com.croco.interview.core.security.util.JWTUtils;
import com.croco.interview.management.user.security.model.request.AuthorizationRequest;
import com.croco.interview.management.user.security.model.response.AuthorizationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private static String generateJWT(UserDetails userDetails) {
        return JWTUtils.generate(
                userDetails.getUsername(),
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
    }

    public AuthorizationResponse authorize(AuthorizationRequest request) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.identifier());

        if (!passwordEncoder.matches(request.password(), userDetails.getPassword())) {
            throw new IllegalStateException("Cannot authorize invalid username or password!");
        }

        return new AuthorizationResponse(
                generateJWT(userDetails),
                JWTUtils.MILLISECONDS_IN_HOUR
        );
    }
}
