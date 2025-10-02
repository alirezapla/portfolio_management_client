package co.pla.portfoliomanagement.gateway.infrastructure.util.response;

public class SuccessfulResponseEntity<T> extends BaseHttpResponse<T> {
    public SuccessfulResponseEntity(T data) {
        super(data, System.currentTimeMillis(), "successfully done");
    }

}
