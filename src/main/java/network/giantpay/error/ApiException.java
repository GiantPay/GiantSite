package network.giantpay.error;

public class ApiException extends Exception {

    private int code;
    private String field;

    public ApiException(int code, String message) {
        super(message);

        this.code = code;
    }

    public ApiException(int code, String field, String message) {
        super(message);

        this.code = code;
        this.field = field;
    }

    public int getCode() {
        return code;
    }

    public String getField() {
        return field;
    }
}
