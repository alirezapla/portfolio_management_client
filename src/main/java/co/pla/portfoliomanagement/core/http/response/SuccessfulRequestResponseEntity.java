package co.pla.portfoliomanagement.core.http.response;

import co.pla.portfoliomanagement.core.logging.AppLogEvent;

public class SuccessfulRequestResponseEntity<T> extends BaseHttpResponse<T> {
    public SuccessfulRequestResponseEntity(T data) {
        super(data, System.currentTimeMillis(), "successfully done");
    }

}
