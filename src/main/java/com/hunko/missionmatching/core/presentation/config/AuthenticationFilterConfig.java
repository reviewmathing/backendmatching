package com.hunko.missionmatching.core.presentation.config;

import com.hunko.missionmatching.core.presentation.security.XHeaderAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class AuthenticationFilterConfig {

    @Bean
    public OncePerRequestFilter authenticationFilter() {
        return new XHeaderAuthenticationFilter();
    }
}
