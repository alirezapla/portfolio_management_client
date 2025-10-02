package co.pla.portfoliomanagement.gateway.infrastructure.util.response;


import co.pla.portfoliomanagement.common.exceptions.ExceptionMessages;

public class ServiceUnavailableResponseEntity<T> extends BaseHttpResponse<T> {
    public ServiceUnavailableResponseEntity(T data) {
        super(data, System.currentTimeMillis(), ExceptionMessages.EXCEPTION.getTitle());
    }
}
