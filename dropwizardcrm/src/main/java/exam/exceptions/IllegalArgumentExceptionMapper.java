package exam.exceptions;

import exam.api.error.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger logger = LoggerFactory.getLogger(IllegalArgumentExceptionMapper.class);

    @Context
    private HttpServletRequest request;

    @Context
    private HttpHeaders httpHeaders;

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        logger.warn("Illegal argument error occurred: {}", exception.getMessage());

    String instance = request != null ? request.getRequestURI() : "unknown";

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/illegal-argument",
                "Illegal Argument",
                Response.Status.BAD_REQUEST.getStatusCode(),
                exception.getMessage(),
        instance
        );

    return ProblemDetailResponseBuilder.build(request, httpHeaders, Response.Status.BAD_REQUEST, problemDetail);
    }
}