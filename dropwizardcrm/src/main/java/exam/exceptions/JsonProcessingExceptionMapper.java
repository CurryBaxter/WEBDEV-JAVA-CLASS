package exam.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import exam.api.error.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

    private static final Logger logger = LoggerFactory.getLogger(JsonProcessingExceptionMapper.class);

    @Context
    private HttpServletRequest request;

    @Context
    private HttpHeaders httpHeaders;


    @Override
    public Response toResponse(JsonProcessingException exception) {
        logger.warn("JSON processing error occurred: {}", exception.getMessage());

    String instance = request != null ? request.getRequestURI() : "unknown";

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/malformed-json",
                "Malformed JSON",
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Request body contains malformed JSON: " + exception.getOriginalMessage(),
        instance
        );

    return ProblemDetailResponseBuilder.build(request, httpHeaders, Response.Status.BAD_REQUEST, problemDetail);
    }
}