package exam.exceptions;

import exam.api.error.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

public final class ProblemDetailResponseBuilder {

    private static final MediaType DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;

    private ProblemDetailResponseBuilder() {}

    public static Response build(HttpServletRequest request,
                                 HttpHeaders headers,
                                 Response.StatusType status,
                                 ProblemDetail problemDetail) {
        MediaType mediaType = negotiate(request, headers);
        return Response.status(status)
                .type(mediaType)
                .entity(problemDetail)
                .build();
    }

    private static MediaType negotiate(HttpServletRequest request, HttpHeaders headers) {
        if (isUiRequest(request)) {
            return MediaType.TEXT_HTML_TYPE;
        }

        if (headers != null) {
            List<MediaType> acceptableMediaTypes = headers.getAcceptableMediaTypes();
            for (MediaType mediaType : acceptableMediaTypes) {
                if (mediaType.isCompatible(MediaType.TEXT_HTML_TYPE) || containsHtml(mediaType)) {
                    return MediaType.TEXT_HTML_TYPE;
                }
                if (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
                    return MediaType.APPLICATION_JSON_TYPE;
                }
            }
        }

        return DEFAULT_MEDIA_TYPE;
    }

    private static boolean containsHtml(MediaType mediaType) {
        return mediaType.toString().toLowerCase().contains("html");
    }

    private static boolean isUiRequest(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        String uri = request.getRequestURI();
        return uri != null && uri.startsWith("/ui");
    }
}
