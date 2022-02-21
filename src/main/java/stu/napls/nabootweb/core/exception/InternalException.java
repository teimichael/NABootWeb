package stu.napls.nabootweb.core.exception;

import stu.napls.nabootweb.core.dictionary.ResponseCode;

public class InternalException extends BaseException {

    private int code = ResponseCode.FAILURE;

    public InternalException(String message) {
        super(message);
    }

    public InternalException(int code, String message) {
        super(message);
        this.code = code;
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
