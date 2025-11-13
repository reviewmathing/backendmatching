package com.hunko.missionmatching.core.presentation.security;

import com.hunko.missionmatching.core.Authorities;
import java.util.Collection;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserRoleArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserRole.class)
                && parameter.getParameterType().equals(Authorities.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> credentials = auth.getAuthorities();
        Optional<? extends GrantedAuthority> first = credentials.stream().findFirst();
        if (first.isEmpty()) {
            return null;
        }
        GrantedAuthority grantedAuthority = first.get();
        String authority = grantedAuthority.getAuthority();
        return Authorities.findByName(authority);
    }
}
