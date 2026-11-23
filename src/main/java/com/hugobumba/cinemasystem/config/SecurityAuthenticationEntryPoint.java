package com.hugobumba.cinemasystem.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException authException
    ) throws IOException {
        String message = (String) request.getAttribute("security.error.message");
        if (message == null || message.isBlank()) {
            message = "Authentication is required";
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"timestamp\":\"" + Instant.now() + "\","
                        + "\"status\":401,"
                        + "\"error\":\"Unauthorized\","
                        + "\"message\":\"" + escape(message) + "\","
                        + "\"path\":\"" + escape(request.getRequestURI()) + "\","
                        + "\"errors\":null}"
        );
    }

    private String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
