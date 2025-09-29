package exam.exceptions;

import exam.api.error.ProblemDetail;
import exam.api.error.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger logger = LoggerFactory.getLogger(ValidationExceptionMapper.class);

        @Context
        private HttpServletRequest request;

        @Context
        private HttpHeaders httpHeaders;


    @Override
    public Response toResponse(ConstraintViolationException exception) {
        logger.warn("Validation error occurred: {}", exception.getMessage());

        List<ValidationError> validationErrors = exception.getConstraintViolations().stream()
                .map(this::createValidationError)
                .collect(Collectors.toList());

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/constraint-violation",
                "Constraint Violation",
                Response.Status.BAD_REQUEST.getStatusCode(),
                "One or more constraints were violated",
                request != null ? request.getRequestURI() : "unknown"
        );
        problemDetail.setValidationErrors(validationErrors);

        return ProblemDetailResponseBuilder.build(request, httpHeaders, Response.Status.BAD_REQUEST, problemDetail);
    }

    private ValidationError createValidationError(ConstraintViolation<?> violation) {
        String fieldName = getFieldName(violation);
        return new ValidationError(
                fieldName,
                violation.getInvalidValue(),
                violation.getMessage()
        );
    }

    private String getFieldName(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        return propertyPath.contains(".") ?
                propertyPath.substring(propertyPath.lastIndexOf('.') + 1) :
                propertyPath;
    }
}