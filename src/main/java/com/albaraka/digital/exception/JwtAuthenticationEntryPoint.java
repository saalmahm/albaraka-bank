package com.albaraka.digital.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        String body = """
            {
              "status": %d,
              "error": "%s",
              "message": "Authentification requise ou identifiants invalides",
              "path": "%s"
            }
            """.formatted(
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI()
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(body);
    }
}