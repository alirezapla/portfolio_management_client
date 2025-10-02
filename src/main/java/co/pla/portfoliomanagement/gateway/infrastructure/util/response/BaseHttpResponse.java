package co.pla.portfoliomanagement.gateway.infrastructure.util.response;

public class BaseHttpResponse<T> {
    private T data;
    private long timestamp;
    private String message;

    public BaseHttpResponse() {
    }

    public BaseHttpResponse(T data, long timestamp, String message) {
        this.data = data;
        this.timestamp = timestamp;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
