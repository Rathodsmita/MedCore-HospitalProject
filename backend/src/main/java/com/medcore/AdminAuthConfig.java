package com.medcore;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminAuthConfig implements WebMvcConfigurer {
    @Value("${medcore.admin-token}") private String adminToken;

    @Value("${medcore.cors-origins}") private String[] corsOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOriginPatterns(corsOrigins)
            .allowedMethods("GET", "POST", "PATCH", "OPTIONS")
            .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor((HandlerInterceptor) (req, res, handler) -> {
            if ("OPTIONS".equalsIgnoreCase(req.getMethod())) return true; // CORS preflight
            String h = req.getHeader("Authorization");
            String given = h != null && h.startsWith("Bearer ") ? h.substring(7) : "";
            boolean ok = MessageDigest.isEqual(given.getBytes(StandardCharsets.UTF_8), adminToken.getBytes(StandardCharsets.UTF_8));
            if (!ok) {
                res.setStatus(401);
                res.setContentType("application/json");
                res.getWriter().write("{\"error\":\"Unauthorized\"}");
            }
            return ok;
        }).addPathPatterns("/api/admin/**");
    }
}
