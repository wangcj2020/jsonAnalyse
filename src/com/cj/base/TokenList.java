package com.cj.base;

import java.util.ArrayList;

public class TokenList {
    private ArrayList<Token> tokens;

    public TokenList(){
        this.tokens = new ArrayList<Token>();
    }

    public void add(Token token) {
        this.tokens.add(token);
    }

    public void show(){
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(this.tokens.get(i));
        }
    }
}
