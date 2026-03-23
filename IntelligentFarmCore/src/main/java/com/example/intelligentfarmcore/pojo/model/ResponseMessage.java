package com.example.intelligentfarmcore.pojo.model;

import org.springframework.http.HttpStatus;

public class ResponseMessage<T> {
    private Integer code;
    private String message;
    private T data;

    public ResponseMessage(Integer code, String message, T data) {
        if(data == null){
            data = (T) new Object();
        }
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 接口请求成功
    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage<>(HttpStatus.OK.value(), "success", data);
    }
    public static <T> ResponseMessage<T> success(T data, String message) {
        return new ResponseMessage<>(HttpStatus.OK.value(), message, data);
    }

    // 接口请求失败
    public static <T> ResponseMessage<T> error(String message) {
        ResponseMessage<Object> responseMessage = new ResponseMessage<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, new Object());
        return (ResponseMessage<T>) responseMessage;
    }
    public static <T> ResponseMessage<T> error(Integer code, String message) {
        ResponseMessage<Object> responseMessage = new ResponseMessage<>(code, message, new Object());
        return (ResponseMessage<T>) responseMessage;
    }
}
