package com.AAA.e_commerce.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class EntryPointExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String jsonResponse =
                """
    {
        "status": 401,
        "error": "Unauthorized",
        "message": "Authentication failed: Token is missing, invalid, or expired",
        "path": "%s"
    }
    """
                        .formatted(request.getServletPath());
        response.getWriter().write(jsonResponse);
    }
}
