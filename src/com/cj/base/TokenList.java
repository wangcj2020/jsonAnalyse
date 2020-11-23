package com.cj.base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TokenList {
    private List<Token> tokens;
    private int index = 0; //下一个token的下标

    public TokenList(){
        this.tokens = new ArrayList<>();
    }

    public void add(Token token) {
        this.tokens.add(token);
    }

    public void show(){
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    public boolean hasMore() {
        return index < tokens.size();
    }

    /**
     * 返回并移除首个Token
     * @return
     */
    public Token next_token() {
        Token token = tokens.get(index);
        index++;
        return token;
    }

    public Token preToken() {
        if (index > 0){
            return tokens.get(index - 1);
        }
        return null;
    }
}
