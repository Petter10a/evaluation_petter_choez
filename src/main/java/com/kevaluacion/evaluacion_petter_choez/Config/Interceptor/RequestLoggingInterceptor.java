package com.kevaluacion.evaluacion_petter_choez.Config.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String remoteAddr = getClientIpAddress(request);

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("INICIA - ");
        logMessage.append("Método: ").append(method);
        logMessage.append(" | URI: ").append(uri);

        if (queryString != null && !queryString.isEmpty()) {
            logMessage.append("?").append(queryString);
        }

        logMessage.append(" | IP: ").append(remoteAddr);

        log.info(logMessage.toString());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        Long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();
        String remoteAddr = getClientIpAddress(request);

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("FINALIZA - ");
        logMessage.append("Método: ").append(method);
        logMessage.append(" | URI: ").append(uri);
        logMessage.append(" | Status: ").append(status);
        logMessage.append(" | Duración: ").append(duration).append("ms");
        logMessage.append(" | IP: ").append(remoteAddr);

        if (ex != null) {
            logMessage.append(" | Error: ").append(ex.getMessage());
            log.error(logMessage.toString(), ex);
        } else {
            log.info(logMessage.toString());
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        String xRealIP = request.getHeader("X-Real-IP");
        String xClientIP = request.getHeader("X-Client-IP");

        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        if (xRealIP != null && !xRealIP.isEmpty() && !"unknown".equalsIgnoreCase(xRealIP)) {
            return xRealIP;
        }

        if (xClientIP != null && !xClientIP.isEmpty() && !"unknown".equalsIgnoreCase(xClientIP)) {
            return xClientIP;
        }

        return request.getRemoteAddr();
    }
}
