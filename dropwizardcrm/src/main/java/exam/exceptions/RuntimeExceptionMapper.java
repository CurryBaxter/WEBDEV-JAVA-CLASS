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
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

    @Context
    private ContainerRequestContext requestContext;

    @Override
    public Response toResponse(RuntimeException exception) {
        logger.error("Unexpected runtime error occurred", exception);

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/internal-error",
                "Internal Server Error",
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "An unexpected error occurred",
                getRequestPath()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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