package com.cj.base;

public enum TokenType {
    BEGIN_OBJECT(1,"{"),   //{
    END_OBJECT(2,"}"),     //}
    BEGIN_ARRAY(4,"["),    //[
    END_ARRAY(8,"]"),      //]
    NULL(16,"null"),       //null
    NUMBER(32,"number"),   //number:数字
    STRING(64,"string"),   //string:字符串
    BOOLEAN(128,"boolean"),//boolean:true/false
    SEP_COLON(256,":"),    //:
    SEP_COMMA(512,","),    //,
    END_DOCUMENT(1024,";");//; end of file

    private int code;
    private String symbol;

    TokenType(int code,String symbol){
        this.code = code;
        this.symbol = symbol;
    }

    public int getCode(){
        return code;
    }

    public String getSymbol(){
        return symbol;
    }

    @Override
    public String toString() {
        return "TokenType{" +
                "code=" + code +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
