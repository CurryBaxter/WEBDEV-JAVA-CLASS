package com.customer.controller.advice;

import com.customer.dto.error.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice(basePackages = "com.customer.controller")
public class WebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebExceptionHandler.class);
    
    @Value("${app.debug.enabled:false}")
    private boolean debugEnabled;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationException(MethodArgumentNotValidException ex, 
                                                 HttpServletRequest request) {
        logger.warn("Web validation error occurred: {}", ex.getMessage());

        List<ValidationError> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());

        // Add global errors
        ex.getBindingResult().getGlobalErrors().forEach(error ->
                validationErrors.add(new ValidationError(
                        error.getObjectName(),
                        null,
                        error.getDefaultMessage())));

        ModelAndView mav = createErrorModelAndView("Validation Error", 
                "Input validation failed for one or more fields", 
                HttpStatus.BAD_REQUEST, request);
        
        mav.addObject("validationErrors", validationErrors);
        
        if (debugEnabled) {
            addDebugInfo(mav, ex, request);
        }

        return mav;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraintViolationException(ConstraintViolationException ex,
                                                          HttpServletRequest request) {
        logger.warn("Web constraint violation occurred: {}", ex.getMessage());

        List<ValidationError> validationErrors = ex.getConstraintViolations().stream()
                .map(violation -> new ValidationError(
                        getFieldName(violation),
                        violation.getInvalidValue(),
                        violation.getMessage()))
                .collect(Collectors.toList());

        ModelAndView mav = createErrorModelAndView("Constraint Violation", 
                "One or more constraints were violated", 
                HttpStatus.BAD_REQUEST, request);
        
        mav.addObject("validationErrors", validationErrors);
        
        if (debugEnabled) {
            addDebugInfo(mav, ex, request);
        }

        return mav;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrityViolationException(DataIntegrityViolationException ex,
                                                             HttpServletRequest request) {
        logger.error("Web data integrity violation: {}", ex.getMessage());

        ModelAndView mav = createErrorModelAndView("Data Integrity Violation", 
                "A database constraint was violated", 
                HttpStatus.CONFLICT, request);
        
        if (debugEnabled) {
            addDebugInfo(mav, ex, request);
        }

        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex,
                                                      HttpServletRequest request) {
        logger.warn("Web illegal argument: {}", ex.getMessage());

        ModelAndView mav = createErrorModelAndView("Invalid Request", 
                ex.getMessage(), 
                HttpStatus.BAD_REQUEST, request);
        
        if (debugEnabled) {
            addDebugInfo(mav, ex, request);
        }

        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected web error occurred", ex);

        ModelAndView mav = createErrorModelAndView("Internal Server Error", 
                "An unexpected error occurred", 
                HttpStatus.INTERNAL_SERVER_ERROR, request);
        
        if (debugEnabled) {
            addDebugInfo(mav, ex, request);
        }

        return mav;
    }

    private ModelAndView createErrorModelAndView(String title, String message, 
                                               HttpStatus status, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorTitle", title);
        mav.addObject("errorMessage", message);
        mav.addObject("errorStatus", status.value());
        mav.addObject("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        mav.addObject("path", request.getRequestURI());
        mav.addObject("debugEnabled", debugEnabled);
        return mav;
    }

    private void addDebugInfo(ModelAndView mav, Exception ex, HttpServletRequest request) {
        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("exceptionClass", ex.getClass().getSimpleName());
        debugInfo.put("exceptionMessage", ex.getMessage());
        debugInfo.put("requestMethod", request.getMethod());
        debugInfo.put("requestURL", request.getRequestURL().toString());
        debugInfo.put("userAgent", request.getHeader("User-Agent"));
        debugInfo.put("remoteAddress", request.getRemoteAddr());
        
        // Add stack trace (first 10 elements for brevity)
        StackTraceElement[] stackTrace = ex.getStackTrace();
        List<String> stackTraceLines = new ArrayList<>();
        for (int i = 0; i < Math.min(10, stackTrace.length); i++) {
            stackTraceLines.add(stackTrace[i].toString());
        }
        debugInfo.put("stackTrace", stackTraceLines);
        
        // Add request parameters
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.isEmpty()) {
            debugInfo.put("requestParameters", parameterMap);
        }
        
        mav.addObject("debugInfo", debugInfo);
    }

    private String getFieldName(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        return propertyPath.contains(".") ? 
               propertyPath.substring(propertyPath.lastIndexOf('.') + 1) : 
               propertyPath;
    }
}