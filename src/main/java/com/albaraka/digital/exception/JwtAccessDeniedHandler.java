package com.albaraka.digital.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        HttpStatus status = HttpStatus.FORBIDDEN;

        String body = """
            {
              "status": %d,
              "error": "%s",
              "message": "Accès refusé pour ce rôle",
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