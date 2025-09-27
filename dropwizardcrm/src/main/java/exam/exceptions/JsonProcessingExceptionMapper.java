package exam.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import exam.api.error.ProblemDetail;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    private static final Logger logger = LoggerFactory.getLogger(JsonProcessingExceptionMapper.class);

    @Context
    private ContainerRequestContext requestContext;

    @Override
    public Response toResponse(JsonProcessingException exception) {
        logger.warn("JSON processing error occurred: {}", exception.getMessage());

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/malformed-json",
                "Malformed JSON",
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Request body contains malformed JSON: " + exception.getOriginalMessage(),
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