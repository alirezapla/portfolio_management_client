package co.pla.portfoliomanagement.core.http.response;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusCode {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    FOUND(302),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    NOT_ACCEPTABLE(406),
    INTERNAL_SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503);

    StatusCode(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return this.code;
    }

    public boolean isOk() {
        return this == StatusCode.OK;
    }

    public boolean isCreated() {
        return this == StatusCode.CREATED;
    }

    public boolean isNoContent() {
        return this == StatusCode.NO_CONTENT;
    }

    public boolean isFound() {
        return this == StatusCode.FOUND;
    }

    public boolean isBadRequest() {
        return this == StatusCode.BAD_REQUEST;
    }

    public boolean isUnauthorized() {
        return this == StatusCode.UNAUTHORIZED;
    }

    public boolean isForbidden() {
        return this == StatusCode.FORBIDDEN;
    }

    public boolean isNotFound() {
        return this == StatusCode.NOT_FOUND;
    }

    public boolean isNotAcceptable() {
        return this == StatusCode.NOT_ACCEPTABLE;
    }

    public boolean isInternalServerError() {
        return this == StatusCode.INTERNAL_SERVER_ERROR;
    }

    public boolean isServiceUnavailable() {
        return this == StatusCode.SERVICE_UNAVAILABLE;
    }

    public boolean isSuccessful() {
        return this.code < 400;
    }

    public boolean isError() {
        return this.code >= 400;
    }

    @JsonCreator
    public static StatusCode valueOf(int code) {
        for (StatusCode statusCode : StatusCode.values()) {
            if (statusCode.code == code) return statusCode;
        }
        throw new IllegalArgumentException("StatusCode not found.");
    }
}
