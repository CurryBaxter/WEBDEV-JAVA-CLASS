package com.customer.controller.advice;

import com.customer.dto.error.ProblemDetail;
import com.customer.dto.error.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Validation error occurred: {}", ex.getMessage());

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

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/validation-error",
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                "Input validation failed for one or more fields",
                request.getDescription(false).replace("uri=", "")
        );
        problemDetail.setValidationErrors(validationErrors);

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        logger.warn("Constraint violation occurred: {}", ex.getMessage());

        List<ValidationError> validationErrors = ex.getConstraintViolations().stream()
                .map(violation -> new ValidationError(
                        getFieldName(violation),
                        violation.getInvalidValue(),
                        violation.getMessage()))
                .collect(Collectors.toList());

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/constraint-violation",
                "Constraint Violation",
                HttpStatus.BAD_REQUEST.value(),
                "One or more constraints were violated",
                request.getDescription(false).replace("uri=", "")
        );
        problemDetail.setValidationErrors(validationErrors);

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException ex, WebRequest request) {
        logger.warn("Binding error occurred: {}", ex.getMessage());

        List<ValidationError> validationErrors = ex.getFieldErrors().stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/binding-error",
                "Binding Error",
                HttpStatus.BAD_REQUEST.value(),
                "Data binding failed",
                request.getDescription(false).replace("uri=", "")
        );
        problemDetail.setValidationErrors(validationErrors);

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        logger.warn("Message not readable: {}", ex.getMessage());

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/malformed-request",
                "Malformed JSON Request",
                HttpStatus.BAD_REQUEST.value(),
                "Request body could not be read or parsed",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.warn("Method argument type mismatch: {}", ex.getMessage());

        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError(
                ex.getName(),
                ex.getValue(),
                String.format("Parameter '%s' should be of type %s", ex.getName(), ex.getRequiredType().getSimpleName())
        ));

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/type-mismatch",
                "Type Mismatch",
                HttpStatus.BAD_REQUEST.value(),
                "Method argument type mismatch",
                request.getDescription(false).replace("uri=", "")
        );
        problemDetail.setValidationErrors(validationErrors);

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        logger.warn("Entity not found: {}", ex.getMessage());

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/entity-not-found",
                "Entity Not Found",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("Data integrity violation: {}", ex.getMessage());

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/data-integrity-violation",
                "Data Integrity Violation",
                HttpStatus.CONFLICT.value(),
                "A database constraint was violated",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Illegal argument: {}", ex.getMessage());

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/illegal-argument",
                "Illegal Argument",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred", ex);

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/internal-error",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    private String getFieldName(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        return propertyPath.contains(".") ? 
               propertyPath.substring(propertyPath.lastIndexOf('.') + 1) : 
               propertyPath;
    }
}