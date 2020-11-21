package com.cj.core;

import com.cj.base.CharReader;
import com.cj.base.Token;
import com.cj.base.TokenList;
import com.cj.base.TokenType;
import com.cj.exception.JsonParseException;

import java.io.IOException;

/*
词法分析器：读取文本并将其装化为Token标记
 */
public class Tokenizer {
    private CharReader charReader;//读取文本
    private TokenList tokenList;//存储Token标记

    public TokenList tokenize(CharReader charReader) throws IOException {
        this.charReader = charReader;
        this.tokenList = new TokenList();
        tokenize();
        return tokenList;
    }

    public void tokenize() throws IOException {
        Token token;
        do{
            //开始读取文本,直至末尾
            token = start();
            tokenList.add(token);
        }while (token.getTokenType() != TokenType.END_DOCUMENT);
    }

    public Token start() throws IOException {
        char ch;
        do {
            if (!this.charReader.hasMore()) {
                //到达文本末尾
                return new Token(TokenType.END_DOCUMENT, null);
            }

            ch = charReader.next_char();
        } while (isWhiteSpace(ch));//忽略有效内容前的所有空格、回车及换行

        switch (ch) {
            case '{':
                return new Token(TokenType.BEGIN_OBJECT,String.valueOf(ch));
            case '}':
                return new Token(TokenType.END_OBJECT,String.valueOf(ch));
            case '[':
                return new Token(TokenType.BEGIN_ARRAY,String.valueOf(ch));
            case ']':
                return new Token(TokenType.END_ARRAY, String.valueOf(ch));
            case ',':
                return new Token(TokenType.SEP_COMMA,String.valueOf(ch));
            case ':':
                return new Token(TokenType.SEP_COLON,String.valueOf(ch));
            case 'n': //返回null Token
                return readNull();
            case '"': //返回String Token
                return readString();
            //返回true、false Token
            case 't':
                return readBoolean("rue");
            case 'f':
                return readBoolean("alse");
            case '-': //负号，返回Number Token
                return readNumber();
        }
        if(isDigit(ch)){
            return readNumber(false,ch);
        }
        throw new JsonParseException("非法字符");
    }

    /**
     * 判断ch是否是空格符
     * @param ch
     * @return
     */
    private boolean isWhiteSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    /**
     * 返回ch是否是数字
     * @param ch
     * @return
     */
    private boolean isDigit(char ch){
        return ch >= '0' && ch <= '9';
    }

    private Token readNull() throws IOException {
        if(!(this.charReader.next_char() == 'u' &&
                this.charReader.next_char() == 'l' &&
                this.charReader.next_char() == 'l')){//后三个字符不是ull
            throw new JsonParseException("无效的Json字符串");
        }
        return new Token(TokenType.NULL,"null");
    }

    private Token readString() throws IOException {
        StringBuilder sb = new StringBuilder();
        while(true){
            char ch = charReader.next_char();
            if(ch == '\\'){//转移字符
                if(!isEscape()){//检查‘\\’后的字符否符合上述转移字符
                    throw new JsonParseException("无效的转义字符：" + ch,this.charReader.getHas_read());
                }
                sb.append('\\');
                char ech = this.charReader.next_char();
                sb.append(ech);
                // 处理 Unicode 编码，形如 \u4e2d。且只支持 \u0000 ~ \uFFFF 范围内的编码
                if (ech == 'u'){
                    for (int i = 0; i < 4; i++) {
                        char uch = this.charReader.next_char();
                        if(isHex(uch)){
                            sb.append(uch);
                        }else {
                            throw new JsonParseException("无效字符："+ uch,this.charReader.getHas_read());
                        }
                    }
                }
            }else if(ch == '"'){//字符串结束
                return new Token(TokenType.STRING,sb.toString());
            }else if(ch == '\r' || ch == '\n'){//传入的Json字符串不允许换行
                throw new JsonParseException("传入的Json字符串不允许换行");
            }else{
                sb.append(ch);
            }
        }
    }

    private boolean isEscape() throws IOException {
        char ch = charReader.peek();
        return (ch == '"' || ch == '\\' || ch == '/' || ch == 'r'
                || ch == 'n' || ch == 'b' || ch == 't' || ch == 'f');
    }

    private boolean isHex(char ch) {
        return ((ch >= '0' && ch <= '9') || ('a' <= ch && ch <= 'f')
                || ('A' <= ch && ch <= 'F'));
    }

    private Token readBoolean(String expected) throws IOException {
        //比对之后的几个字符串与期望的字符串是否相同
        for (int i = 0; i < expected.length(); i++) {
            char ch = this.charReader.next_char();
            if(ch != expected.charAt(i)){
                throw new JsonParseException("不期望的字符：" + ch, this.charReader.getHas_read());
            }
        }

        String value;
        if ("rue".equals(expected)){
            value = new String("true");
        }else{
            value = new String("false");
        }
        return new Token(TokenType.BOOLEAN,value);
    }

    private Token readNumber(boolean isNegativeNum,char c) throws IOException {
        StringBuilder sb = new StringBuilder();
        if(isNegativeNum){
            sb.append('-');
        }else{
            sb.append(c);
        }

        // 读取之后的数据：暂定必须为整数(无小数点)
        char nch;
        while (true){
            nch = this.charReader.peek();
            if(!isDigit(nch)){
                break;
            }else{
                nch = this.charReader.next_char();
                sb.append(nch);
            }
        }

        String num = sb.toString();
        if (isNegativeNum && "-".equals(num)){//若是负数，可能只有负号，则返回异常
            throw new JsonParseException("不期望的字符：" + nch, this.charReader.getHas_read());
        }
        //若是正数至少是一位正数
        return new Token(TokenType.NUMBER,num);
    }

    private Token readNumber() throws IOException {
        return this.readNumber(true,' ');
    }
}
