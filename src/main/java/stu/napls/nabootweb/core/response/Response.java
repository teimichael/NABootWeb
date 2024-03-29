package stu.napls.nabootweb.core.response;

import lombok.Data;
import stu.napls.nabootweb.core.dictionary.ResponseCode;

import java.io.Serializable;

/**
 * @author Tei Michael
 * @date 2/21/2022
 */
@Data
public class Response<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    private Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<T>(ResponseCode.SUCCESS, "ok", data);
    }

    public static <T> Response<T> success(String message) {
        return new Response<T>(ResponseCode.SUCCESS, message, null);
    }

    public static <T> Response<T> success(String message, T data) {
        return new Response<T>(ResponseCode.SUCCESS, message, data);
    }

    public static <T> Response<T> failure(int code, String message) {
        return new Response<T>(code, message, null);
    }

    public static <T> Response<T> failure(String message) {
        return failure(ResponseCode.FAILURE, message);
    }

    public String toString() {
        return "{code:\"" + code + "\", message:\"" + message + "\", data:\"" + (data != null ? data.toString() : null) + "\"}";
    }

}