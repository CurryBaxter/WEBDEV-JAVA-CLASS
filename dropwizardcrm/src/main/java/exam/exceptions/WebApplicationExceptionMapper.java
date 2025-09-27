package exam.exceptions;

import exam.api.error.ProblemDetail;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    private static final Logger logger = LoggerFactory.getLogger(WebApplicationExceptionMapper.class);

    @Context
    private ContainerRequestContext requestContext;

    @Override
    public Response toResponse(WebApplicationException exception) {
        Response response = exception.getResponse();
        int status = response.getStatus();

        // Only log errors (5xx), not client errors (4xx)
        if (status >= 500) {
            logger.error("Web application error occurred: {}", exception.getMessage(), exception);
        } else if (status >= 400) {
            logger.warn("Client error occurred: {}", exception.getMessage());
        }

        String type = getTypeForStatus(status);
        String title = getTitleForStatus(status);
        String detail = exception.getMessage() != null ? exception.getMessage() : getDefaultDetailForStatus(status);

        ProblemDetail problemDetail = new ProblemDetail(
                type,
                title,
                status,
                detail,
                getRequestPath()
        );

        return Response.status(status)
                .entity(problemDetail)
                .build();
    }

    private String getTypeForStatus(int status) {
        switch (status) {
            case 400: return "https://problems.customer-crm.com/bad-request";
            case 401: return "https://problems.customer-crm.com/unauthorized";
            case 403: return "https://problems.customer-crm.com/forbidden";
            case 404: return "https://problems.customer-crm.com/not-found";
            case 405: return "https://problems.customer-crm.com/method-not-allowed";
            case 406: return "https://problems.customer-crm.com/not-acceptable";
            case 409: return "https://problems.customer-crm.com/conflict";
            case 415: return "https://problems.customer-crm.com/unsupported-media-type";
            case 500: return "https://problems.customer-crm.com/internal-error";
            default: return "https://problems.customer-crm.com/http-error";
        }
    }

    private String getTitleForStatus(int status) {
        switch (status) {
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 405: return "Method Not Allowed";
            case 406: return "Not Acceptable";
            case 409: return "Conflict";
            case 415: return "Unsupported Media Type";
            case 500: return "Internal Server Error";
            default: return "HTTP Error";
        }
    }

    private String getDefaultDetailForStatus(int status) {
        switch (status) {
            case 400: return "The request was malformed or invalid";
            case 401: return "Authentication is required to access this resource";
            case 403: return "Access to this resource is forbidden";
            case 404: return "The requested resource was not found";
            case 405: return "The HTTP method is not allowed for this resource";
            case 406: return "The requested media type is not acceptable";
            case 409: return "The request conflicts with the current state of the resource";
            case 415: return "The media type is not supported";
            case 500: return "An internal server error occurred";
            default: return "An HTTP error occurred";
        }
    }

    private String getRequestPath() {
        if (requestContext != null && requestContext.getUriInfo() != null) {
            return requestContext.getUriInfo().getPath();
        }
        return "unknown";
    }
}