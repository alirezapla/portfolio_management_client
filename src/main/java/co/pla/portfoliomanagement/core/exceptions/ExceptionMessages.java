package co.pla.portfoliomanagement.common.exceptions;

public enum ExceptionMessages {
    EXCEPTION(0, "Exception!"),
    BAD_REQUEST(1, "Bad request!"),
    USER_CREDENTIAL_NOT_FOUND(2, "UserCredential for requested username not found."),
    PASSWORD_IS_NOT_VALID(3, "Entered password is not valid."),
    EMPTY_FILE(4, "Empty file!"),
    RECORD_NOT_FOUND(5, "Record not found."),
    TRANSACTION_NOT_FOUND(6, "Transaction not found."),
    ILLEGAL_CONTENT_EXCEPTION(7, "Illegal content!"),
    CURRENT_PASSWORD_IS_EQUAL_TO_NEW_PASSWORD(8, "Current password is equal to new password!"),
    REGISTRY_OFFICE_API_EXCEPTION(9, "Registry office api threw an exception! "),
    ILLEGAL_STATE(10, "Illegal state!"),
    SERVICE_UNAVAILABLE(11, "Service Unavailable"),
    USER_NOT_FOUND(12, "Record not found.");


    private int index;
    private String title;

    ExceptionMessages(int index, String title) {
        this.index = index;
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "ExceptionMessages{" +
                "index=" + index +
                ", title='" + title + '\'' +
                '}';
    }
}