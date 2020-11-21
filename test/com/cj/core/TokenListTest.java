package com.cj.core;

import com.cj.base.CharReader;
import com.cj.base.TokenList;
import org.junit.Test;

import java.io.*;

public class TokenListTest {
    String file_path = System.getProperty("user.dir") + "\\test\\com\\cj\\core\\json";
    Reader fileReader = getReader(file_path);

    private Reader getReader(String file_path){
        try {
            return new FileReader(new File(file_path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    /**
     * 输出结果
     * {"name":"wang","age":24,"sex":"boy","hobby":["game","movie","music"]}
    */
    public void test_read_file() throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String content = null;
        while (true){
            try {
                if ((content = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(content);
        }
    }

    @Test
    /**
     * 输出结果
     * Token{tokenType=TokenType{code=1, symbol='{'}, value='{'}
     * Token{tokenType=TokenType{code=64, symbol='string'}, value='name'}
     * Token{tokenType=TokenType{code=256, symbol=':'}, value=':'}
     * Token{tokenType=TokenType{code=64, symbol='string'}, value='wang'}
     * Token{tokenType=TokenType{code=512, symbol=','}, value=','}
     * Token{tokenType=TokenType{code=64, symbol='string'}, value='age'}
     * Token{tokenType=TokenType{code=256, symbol=':'}, value=':'}
     * Token{tokenType=TokenType{code=32, symbol='number'}, value='24'}
     * Token{tokenType=TokenType{code=512, symbol=','}, value=','}
     * Token{tokenType=TokenType{code=64, symbol='string'}, value='sex'}
     * Token{tokenType=TokenType{code=256, symbol=':'}, value=':'}
     * Token{tokenType=TokenType{code=64, symbol='string'}, value='boy'}
     * Token{tokenType=TokenType{code=512, symbol=','}, value=','}
     * Token{tokenType=TokenType{code=64, symbol='string'}, value='hobby'}
     * Token{tokenType=TokenType{code=256, symbol=':'}, value=':'}
     * Token{tokenType=TokenType{code=4, symbol='['}, value='['}
     * Token{tokenType=TokenType{code=64, symbol='string'}, value='game'}
     * Token{tokenType=TokenType{code=512, symbol=','}, value=','}
     * Token{tokenType=TokenType{code=64, symbol='string'}, value='movie'}
     * Token{tokenType=TokenType{code=512, symbol=','}, value=','}
     * Token{tokenType=TokenType{code=64, symbol='string'}, value='music'}
     * Token{tokenType=TokenType{code=8, symbol=']'}, value=']'}
     * Token{tokenType=TokenType{code=2, symbol='}'}, value='}'}
     * Token{tokenType=TokenType{code=1024, symbol=';'}, value='null'}
     *
     * Process finished with exit code 0
     */
    public void test_tokenizer() throws IOException {
        Tokenizer tokenizer = new Tokenizer();
        CharReader charReader = new CharReader(fileReader);
        TokenList list = tokenizer.tokenize(charReader);
        list.show();
    }
}
