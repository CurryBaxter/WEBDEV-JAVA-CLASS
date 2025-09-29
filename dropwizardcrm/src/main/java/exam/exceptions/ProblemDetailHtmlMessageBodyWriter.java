package exam.exceptions;

import exam.api.error.ProblemDetail;
import exam.api.error.ValidationError;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Provider
@Produces(MediaType.TEXT_HTML)
public class ProblemDetailHtmlMessageBodyWriter implements MessageBodyWriter<ProblemDetail> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return ProblemDetail.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(ProblemDetail problemDetail,
                        Class<?> type,
                        Type genericType,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException {

        String html = buildHtml(problemDetail);
        entityStream.write(html.getBytes(StandardCharsets.UTF_8));
    }

    private String buildHtml(ProblemDetail problemDetail) {
        StringBuilder sb = new StringBuilder(2048);
        sb.append("<!DOCTYPE html>\n")
          .append("<html lang=\"de\">\n")
          .append("<head>\n")
          .append("    <meta charset=\"UTF-8\">\n")
          .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
          .append("    <title>").append(escape(problemDetail.getTitle())).append("</title>\n")
          .append("    <script src=\"https://cdn.tailwindcss.com\"></script>\n")
          .append("</head>\n")
          .append("<body class=\"bg-gray-50 min-h-screen\">\n")
          .append("    <div class=\"max-w-4xl mx-auto px-6 py-10\">\n")
          .append("        <div class=\"bg-white border border-red-200 rounded-xl shadow-lg p-8\">\n")
          .append("            <h1 class=\"text-2xl font-bold text-red-600 mb-4\">")
          .append(escape(problemDetail.getTitle()))
          .append("</h1>\n")
          .append("            <p class=\"text-gray-700 mb-2\"><strong>Status:</strong> ").append(problemDetail.getStatus()).append("</p>\n")
          .append("            <p class=\"text-gray-700 mb-2\"><strong>Detail:</strong> ")
          .append(escape(problemDetail.getDetail()))
          .append("</p>\n")
          .append("            <p class=\"text-gray-500 text-sm\"><strong>Instance:</strong> ")
          .append(escape(problemDetail.getInstance()))
          .append("</p>\n");

        if (problemDetail.getTimestamp() != null) {
            sb.append("            <p class=\"text-gray-500 text-sm\"><strong>Zeitpunkt:</strong> ")
              .append(DATE_TIME_FORMATTER.format(problemDetail.getTimestamp()))
              .append("</p>\n");
        }

        if (problemDetail.getValidationErrors() != null && !problemDetail.getValidationErrors().isEmpty()) {
            sb.append("            <div class=\"mt-6\">\n")
              .append("                <h2 class=\"text-lg font-semibold text-yellow-700 mb-2\">Validierungsfehler</h2>\n")
              .append("                <ul class=\"list-disc list-inside space-y-2 text-gray-700\">\n");
            for (ValidationError error : problemDetail.getValidationErrors()) {
                sb.append("                    <li><strong>")
                  .append(escape(error.getField()))
                  .append(":</strong> ")
                  .append(escape(error.getMessage()))
                  .append(optionalRejectedValue(error.getRejectedValue()))
                  .append("</li>\n");
            }
            sb.append("                </ul>\n")
              .append("            </div>\n");
        }

        if (problemDetail.getExtensions() != null && !problemDetail.getExtensions().isEmpty()) {
            sb.append("            <div class=\"mt-6\">\n")
              .append("                <h2 class=\"text-lg font-semibold text-gray-800 mb-2\">Zusätzliche Informationen</h2>\n")
              .append("                <div class=\"bg-gray-100 rounded-lg p-4 text-sm text-gray-700 space-y-2\">\n");
            for (Map.Entry<String, Object> entry : problemDetail.getExtensions().entrySet()) {
                sb.append("                    <div><strong>")
                  .append(escape(entry.getKey()))
                  .append(":</strong> ")
                  .append(escape(String.valueOf(entry.getValue())))
                  .append("</div>\n");
            }
            sb.append("                </div>\n")
              .append("            </div>\n");
        }

        sb.append("            <div class=\"mt-6 flex space-x-3\">\n")
          .append("                <a href=\"/ui\" class=\"bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg\">Zur Startseite</a>\n")
          .append("                <a href=\"javascript:history.back()\" class=\"bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-lg\">Zurück</a>\n")
          .append("            </div>\n")
          .append("        </div>\n")
          .append("    </div>\n")
          .append("</body>\n")
          .append("</html>\n");

        return sb.toString();
    }

    private String optionalRejectedValue(Object value) {
        if (value == null) {
            return "";
        }
        return " <span class=\"text-gray-500\">(Wert: " + escape(String.valueOf(value)) + ")</span>";
    }

    private String escape(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}