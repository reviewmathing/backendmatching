package com.hunko.missionmatching.core.presentation.security;

import com.hunko.missionmatching.core.Authorities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class XHeaderAuthenticationFilter extends OncePerRequestFilter {


    private static final String X_USER_ROLE = "X-User-ROLE";
    private static final String X_USER_ID = "X-User-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String idValue = request.getHeader(X_USER_ID);
        String roles = request.getHeader(X_USER_ROLE);

        if (idValue == null || idValue.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String[] split = roles.split(",");
        try {
            Authentication xHeaderAuthenticationToken = new XHeaderAuthenticationToken(idValue,
                    getAuthorities(split));
            SecurityContextHolder.getContext().setAuthentication(xHeaderAuthenticationToken);
            filterChain.doFilter(request, response);
        } catch (BadCredentialsException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid role\"}");
        }


    }

    private Collection<? extends GrantedAuthority> getAuthorities(String[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            try {
                authorities.add(new SimpleGrantedAuthority(Authorities.findByName(role).toSpringAuth()));
            } catch (IllegalArgumentException e) {
                throw new BadCredentialsException("Invalid role");
            }

        }
        return authorities;
    }
}
