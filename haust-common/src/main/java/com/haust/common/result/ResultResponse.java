package com.haust.common.result;

public class ResultResponse<T> {
    private int code; // 状态码
    private String message; // 返回信息
    private T data; // 返回的数据

    // 构造函数
    public ResultResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功方法
    public static <T> ResultResponse<T> success(T data) {
        return new ResultResponse<>(200, "Success", data);
    }

    // 失败方法
    public static <T> ResultResponse<T> error(int code, String message) {
        return new ResultResponse<>(code, message, null);
    }

    // Getter 和 Setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
}