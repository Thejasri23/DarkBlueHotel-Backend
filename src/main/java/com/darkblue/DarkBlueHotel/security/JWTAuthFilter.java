package com.darkblue.DarkBlueHotel.security;

import com.darkblue.DarkBlueHotel.service.CustomUserDetailsService;
import com.darkblue.DarkBlueHotel.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;


    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);
        final String userEmail = jwtUtils.extractUsername(jwtToken);

        if (userEmail != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            var userDetails =
                    customUserDetailsService.loadUserByUsername(userEmail);

            if (jwtUtils.isValidToken(jwtToken, userDetails)) {

                // ✅ Extract role from JWT
                String role = jwtUtils.extractRole(jwtToken);

                var authorities = Collections.singletonList(
                        new SimpleGrantedAuthority(role)
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userEmail,
                                null,
                                authorities
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}