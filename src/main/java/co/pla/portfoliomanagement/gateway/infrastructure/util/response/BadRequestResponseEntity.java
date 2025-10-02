package co.pla.portfoliomanagement.gateway.infrastructure.util.response;


import co.pla.portfoliomanagement.common.exceptions.ExceptionMessages;

public class BadRequestResponseEntity<T> extends BaseHttpResponse<T> {
    public BadRequestResponseEntity(T data) {
        super(data, System.currentTimeMillis(), ExceptionMessages.BAD_REQUEST.getTitle());
    }
}
