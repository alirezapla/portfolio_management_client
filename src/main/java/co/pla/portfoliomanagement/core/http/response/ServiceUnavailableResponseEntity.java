package co.pla.portfoliomanagement.core.http.response;


import co.pla.portfoliomanagement.core.exceptions.ExceptionMessages;

public class ServiceUnavailableResponseEntity<T> extends BaseHttpResponse<T> {
    public ServiceUnavailableResponseEntity(T data) {
        super(data, System.currentTimeMillis(), ExceptionMessages.EXCEPTION.getTitle());
    }
}
