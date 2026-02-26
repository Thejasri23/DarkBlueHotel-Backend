//package com.darkblue.DarkBlueHotel.security;
//
//
//import com.darkblue.DarkBlueHotel.service.CustomUserDetailsService;
//import com.darkblue.DarkBlueHotel.utils.JWTUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.jspecify.annotations.NonNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.CachingUserDetailsService;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//
//@Component
//public class JWTAuthFilter extends OncePerRequestFilter {
//
//
//    @Autowired
//    private JWTUtils jwtUtils;
//
//    private CustomUserDetailsService customUserDetailsService;
//
//
//
//    @Override
//    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("authorization");
//        final String jwtToken;
//        final String userEmail;
//        if(authHeader==null || authHeader.isBlank()) {
//            filterChain.doFilter(request,response);
//            return;
//        }
//        jwtToken = authHeader.substring(7);
//        userEmail = jwtUtils.extractUsername(jwtToken);
//
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
//            if (jwtUtils.isValidToken(jwtToken, userDetails)) {
//                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                securityContext.setAuthentication(token);
//                SecurityContextHolder.setContext(securityContext);
//            }
//        }
//        filterChain.doFilter(request, response);
//
//
//    }
//}


package com.darkblue.DarkBlueHotel.security;

import com.darkblue.DarkBlueHotel.service.CustomUserDetailsService;
import com.darkblue.DarkBlueHotel.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // ✅ If no header or wrong format → continue
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);
        final String userEmail = jwtUtils.extractUsername(jwtToken);

        if (userEmail != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(userEmail);

            if (jwtUtils.isValidToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
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
