package com.cj.exception;

/**
 * Json异常基类
 */
public class JsonException extends RuntimeException{
    /**
     * 默认构造函数
     */
    public JsonException(){ }

    /**
     * 以异常信息为参数的有参构造
     * @param message
     */
    public JsonException(String message){
        super(message);
    }

    /**
     * 以异常原因为参数的有参构造
     * @param cause
     */
    public JsonException(Throwable cause){
        super(cause);
    }

    /**
     * 以异常信息和异常原因为参数的有参构造
     * @param message
     * @param cause
     */
    public JsonException(String message,Throwable cause){
        super(message,cause);
    }
}
