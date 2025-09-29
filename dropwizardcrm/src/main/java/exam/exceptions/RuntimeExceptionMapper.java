package exam.exceptions;

import exam.api.error.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private static final Logger logger = LoggerFactory.getLogger(RuntimeExceptionMapper.class);

    private static final int STACK_TRACE_PREVIEW_LIMIT = 10;

    @Context
    private HttpServletRequest request;

    @Context
    private HttpHeaders httpHeaders;

    @Override
    public Response toResponse(RuntimeException exception) {
        logger.error("Unexpected runtime error occurred", exception);

        String instance = request != null ? request.getRequestURI() : "unknown";
        String detail = exception.getMessage() != null ? exception.getMessage() : "An unexpected error occurred";

        ProblemDetail problemDetail = new ProblemDetail(
                "https://problems.customer-crm.com/internal-error",
                "Internal Server Error",
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                detail,
                instance
        );
        problemDetail.setExtensions(buildExtensions(exception));

        return ProblemDetailResponseBuilder.build(request, httpHeaders, Response.Status.INTERNAL_SERVER_ERROR, problemDetail);
    }

    private Map<String, Object> buildExtensions(RuntimeException exception) {
        Map<String, Object> extensions = new LinkedHashMap<>();
        extensions.put("exceptionClass", exception.getClass().getName());
        extensions.put("rootCause", getRootCauseDescription(exception));
        extensions.put("stackTrace", formatStackTrace(exception));
        return extensions;
    }

    private String getRootCauseDescription(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        String message = root.getMessage();
        return message != null ? root.getClass().getName() + ": " + message : root.getClass().getName();
    }

    private String formatStackTrace(Throwable throwable) {
        return Arrays.stream(throwable.getStackTrace())
                .limit(STACK_TRACE_PREVIEW_LIMIT)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }
}