package com.cj.exception;

/**
 * Json 解析异常类
 */
public class JsonParseException extends JsonException{
    private final int errorIndex; //出错字符位置

    public JsonParseException(String message,int errorIndex) {
        super(message);
        this.errorIndex = errorIndex;
    }

    public JsonParseException(String message){
        this(message,-1);
    }

    public int getErrorIndex() {
        return errorIndex;
    }
}
