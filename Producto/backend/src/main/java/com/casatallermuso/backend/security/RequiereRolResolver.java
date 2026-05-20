package com.casatallermuso.backend.security;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.casatallermuso.backend.annotations.RequiereRol;
import com.casatallermuso.backend.enums.TipoRolUsuario;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequiereRolResolver implements HandlerMethodArgumentResolver {

    private final JwtAuthHelper jwtAuthHelper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequiereRol.class) &&
            parameter.getParameterType().equals(Claims.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        @Nullable ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        RequiereRol annotation = parameter.getParameterAnnotation(RequiereRol.class);
        TipoRolUsuario requiredRole = annotation.value();

        Claims claims = jwtAuthHelper.authenticate(request);
        jwtAuthHelper.authorizeRole(claims, requiredRole);
        return claims;
    }

    
}
