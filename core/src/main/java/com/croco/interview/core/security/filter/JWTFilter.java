package com.croco.interview.core.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.croco.interview.core.security.util.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JWTFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || header.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String[] headerContent = header.split(" ");
        if (headerContent.length < 2) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = headerContent[1];

        DecodedJWT decodedJWT = JWTUtils.verify(token);
        if (decodedJWT != null) {
            var authentication = UsernamePasswordAuthenticationToken
                    .authenticated(
                            decodedJWT.getClaim("sub").asString(),
                            "",
                            Arrays.stream(decodedJWT.getClaim("roles").asArray(String.class))
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList())
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
