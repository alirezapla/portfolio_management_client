package co.pla.portfoliomanagement.common.http.response;

public class SuccessfulRequestResponseEntity<T> extends BaseHttpResponse<T> {
    public SuccessfulRequestResponseEntity(T data) {
        super(data, System.currentTimeMillis(), "successfully done");
    }

}
