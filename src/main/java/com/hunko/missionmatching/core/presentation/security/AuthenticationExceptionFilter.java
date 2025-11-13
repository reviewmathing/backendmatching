package com.hunko.missionmatching.core.presentation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationExceptionFilter extends OncePerRequestFilter {

    private final Filter authenticationFilter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthenticationExceptionFilter(Filter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            authenticationFilter.doFilter(request, response, filterChain);
        } catch (AuthenticationException e) {
            ResponseBodyDto responseBodyDto = new ResponseBodyDto(e.getMessage());
            objectMapper.writeValue(response.getOutputStream(), responseBodyDto);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


    @Getter
    @NoArgsConstructor
    private static class ResponseBodyDto {
        private String error;

        public ResponseBodyDto(String error) {
            this.error = error;
        }
    }
}
