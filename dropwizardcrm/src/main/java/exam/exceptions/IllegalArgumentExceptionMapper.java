package main.java.exam.exceptions;

import exam.api.error.ProblemDetail;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger logger = LoggerFactory.getLogger(IllegalArgumentExceptionMapper.class);

    @Context
    private ContainerRequestContext requestContext;

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        logger.warn("Illegal argument error occurred: {}", exception.getMessage());

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/illegal-argument",
                "Illegal Argument",
                Response.Status.BAD_REQUEST.getStatusCode(),
                exception.getMessage(),
                getRequestPath()
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(problemDetail)
                .build();
    }

    private String getRequestPath() {
        if (requestContext != null && requestContext.getUriInfo() != null) {
            return requestContext.getUriInfo().getPath();
        }
        return "unknown";
    }
}