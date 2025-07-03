package com.blink.backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class EndpointLoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTRIBUTE = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTRIBUTE, startTime);
        
        log.info("Request started - Method: {}, Path: {}, Start Time: {}", 
                request.getMethod(), 
                request.getRequestURI(), 
                startTime);
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        if (startTime != null) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            log.info("Request completed - Method: {}, Path: {}, Status: {}, End Time: {}, Duration: {}ms", 
                    request.getMethod(), 
                    request.getRequestURI(), 
                    response.getStatus(),
                    endTime, 
                    duration);
        }
    }
}