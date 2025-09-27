package exam.api.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationError {

    @JsonProperty
    private String field;

    @JsonProperty
    private Object rejectedValue;

    @JsonProperty
    private String message;

    public ValidationError() {}

    public ValidationError(String field, Object rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    // Getters and Setters
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public Object getRejectedValue() { return rejectedValue; }
    public void setRejectedValue(Object rejectedValue) { this.rejectedValue = rejectedValue; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}