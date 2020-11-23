package com.cj.core;

import com.cj.base.Token;
import com.cj.base.TokenList;
import com.cj.base.TokenType;
import com.cj.exception.JsonParseException;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    private TokenList tokenList = null;
    private List<JsonObject> json = new ArrayList<>();

    public JsonParser(TokenList tokenList){
        this.tokenList = tokenList;
    }

    /**
     * 解析到'{'时，说明后面可能是一个Json对象，调用该函数解析该Json对象
     * @return
     */
    private JsonObject parseJsonObject(){
        JsonObject jsonObject = new JsonObject();
        //调用该函数时必然是遇到了'{',因此起初期待一个key值(必须为字符串)，或'}'
        int exceptTokeTypeCode = TokenType.STRING.getCode() | TokenType.END_OBJECT.getCode();

        String key = null;
        Object value = null;

        while(tokenList.hasMore()){
            // 1、读取一个Token
            Token token = tokenList.next_token();
            TokenType tokenType = token.getTokenType();
            String tokenValue = token.getValue();
            // 2、判断该Token是否是期望的Token,若是更改exceptTokeTypeCode，否则抛出异常，直至最终遇到'}'后返回该Json对象
            /*
            {：期待一个key值，必须为字符串或‘}‘；递归解析JsonObject
            }：完成解析返回jsonObject
            [：期待一个Array元素值，或’]‘
            ]：期待一个分隔符‘,’
            :：期待一个value值，可以是null、boolean、string、number、Array；
            ,：期待一个key值，必须为字符串
            boolean：只能为value值，因此期待一个分隔符','
            number：只能为value值，因此期待一个分隔符',
            null：只能为value值，因此期待一个分隔符',
            string：可以是key值，此时期待一个分隔符‘:’;也可以是value值，此时期待一个分隔符‘,’
                    此处需要先判断该string是key还是value
            ;：文件结束
            * */
            checkExceptToken(tokenType, exceptTokeTypeCode);
            switch (tokenType){
                case BEGIN_OBJECT: //key(字符串)、'}'
                    jsonObject.put(key,parseJsonObject());  //递归解析Json对象
                    exceptTokeTypeCode  = TokenType.STRING.getCode() | TokenType.END_OBJECT.getCode();
                    break;
                case END_OBJECT:
                    return jsonObject;
                case BEGIN_ARRAY: //value(字符串)、']'
                    jsonObject.put(key,parseJsonArray()); //递归解析Array
                    exceptTokeTypeCode = TokenType.STRING.getCode() | TokenType.END_ARRAY.getCode();
                    break;
                case END_ARRAY: //','、'}'
                    exceptTokeTypeCode = TokenType.SEP_COMMA.getCode() | TokenType.END_OBJECT.getCode();
                    break;
                case NULL: // ','
                case NUMBER: // ','
                case BOOLEAN: // ','
                    exceptTokeTypeCode = TokenType.SEP_COMMA.getCode();
                    break;
                case STRING: //只有string才能做key值
                    // 1、获取前一个Token
                    Token preToken = tokenList.preToken();
                    // 2、前一个Token是':'，说明该String是value值，否则为key值
                    if (preToken.getTokenType().getCode() == TokenType.SEP_COLON.getCode()){
                        value = token.getValue();
                        jsonObject.put(key,value);
                        exceptTokeTypeCode = TokenType.SEP_COMMA.getCode() | TokenType.END_OBJECT.getCode();
                    }else{
                        key = token.getValue();
                        exceptTokeTypeCode = TokenType.SEP_COLON.getCode();
                    }
                    break;
                case SEP_COLON: // null、boolean、string、number、'{'、'['
                    exceptTokeTypeCode = TokenType.NULL.getCode() | TokenType.BOOLEAN.getCode()
                            | TokenType.STRING.getCode() | TokenType.NUMBER.getCode()
                            | TokenType.BEGIN_OBJECT.getCode() | TokenType.BEGIN_ARRAY.getCode();
                    break;
                case SEP_COMMA: // key值，或'{'
                    exceptTokeTypeCode = TokenType.STRING.getCode()/* | TokenType.BEGIN_OBJECT.getCode()*/;
                    break;
                case END_DOCUMENT:
                    System.out.println("已解析到文件末尾!");
                    break;
                default:
                    throw new JsonParseException("无效的Token");
            }
        }
        throw new JsonParseException("解析错误，无效的Token");
    }

    private void checkExceptToken(TokenType tokenType,int exceptTokeTypeCode){
        if((tokenType.getCode() & exceptTokeTypeCode) == 0){
            throw new JsonParseException("解析错误，不期望的Token!");
        }
    }

    private JsonArray parseJsonArray(){
        JsonArray jsonArray = new JsonArray();
        throw new JsonParseException("解析错误，无效的Token");
    }

//    定义JsonObject、JsonArray，完成语法分析JsonParser,即Token流转为Json对象并完成测试
}
