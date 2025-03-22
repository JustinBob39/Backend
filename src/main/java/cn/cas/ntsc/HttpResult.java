package cn.cas.ntsc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpResult {
    private static final int SUCCESS_CODE = 200;
    private static final int FAILURE_CODE = 400;

    private int code;
    private String message;
    private Object data;

    public static HttpResult success(String message, Object data) {
        HttpResult result = new HttpResult();
        result.setCode(SUCCESS_CODE);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static HttpResult success(String message) {
        HttpResult result = new HttpResult();
        result.setCode(SUCCESS_CODE);
        result.setMessage(message);
        return result;
    }

    public static HttpResult failure(String message, Object data) {
        HttpResult result = new HttpResult();
        result.setCode(FAILURE_CODE);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static HttpResult failure(String message) {
        HttpResult result = new HttpResult();
        result.setCode(FAILURE_CODE);
        result.setMessage(message);
        return result;
    }
}
