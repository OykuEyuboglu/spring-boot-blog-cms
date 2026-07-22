package com.oyku.blog.Security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
    	
    	LOGGER.debug("JWT filter triggered for URI: {}", request.getRequestURI());
    	
    	final String authHeader = request.getHeader("Authorization");

    	LOGGER.debug("Authorization header: {}", authHeader);
    	
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        LOGGER.debug("TOKEN: {}", jwt);
        
        final String userEmail =
                jwtService.extractUsername(jwt);
        
        LOGGER.debug("EMAIL FROM TOKEN: {}", userEmail);
        
        if (userEmail != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
        	
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {

                LOGGER.info("TOKEN VALID");

                System.out.println("AUTHORITIES : " + userDetails.getAuthorities());
                LOGGER.debug("AUTHORITIES:" + userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);

            }

        }
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication);
        filterChain.doFilter(request, response);

    }

}