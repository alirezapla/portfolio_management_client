package co.pla.portfoliomanagement.core.http.response;


import co.pla.portfoliomanagement.core.exceptions.ExceptionMessages;

public class BadRequestResponseEntity<T> extends BaseHttpResponse<T> {
    public BadRequestResponseEntity(T data) {
        super(data, System.currentTimeMillis(), ExceptionMessages.BAD_REQUEST.getTitle());
    }
}
