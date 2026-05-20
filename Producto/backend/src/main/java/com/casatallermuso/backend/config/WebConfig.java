package com.casatallermuso.backend.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.casatallermuso.backend.security.RequiereAuthResolver;
import com.casatallermuso.backend.security.RequiereRolResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final RequiereAuthResolver requiereAuthResolver;
    private final RequiereRolResolver requiereRolResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(requiereAuthResolver);
        resolvers.add(requiereRolResolver);
    }

}
