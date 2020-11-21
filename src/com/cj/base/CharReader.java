package com.cj.base;

import java.io.IOException;
import java.io.Reader;

public class CharReader {
    private static final int BUFFER_SIZE = 1024;
    private final Reader reader;
    private final char[] buffer;

    private int has_read = 0; //total has read char number
    private int pos; //buffer position
    private int size; //buffer ends

    public CharReader(Reader reader){
        this.reader = reader;
        buffer = new char[BUFFER_SIZE];
    }

    public int getHas_read() {
        return has_read;
    }

    public void setHas_read(int has_read) {
        this.has_read = has_read;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    private void fillBuffer() throws IOException {
        int n = this.reader.read(buffer);
        if(n == -1){//未读到内容
            return;
        }
        this.pos = 0;
        this.size = n;
        this.has_read += n;
    }
    public boolean hasMore() throws IOException {
        if(pos < size){
            return true;
        }
        // fill buffer
        fillBuffer();
        return pos < size;
    }
    /**
     * 读取下一个字符，但不移动位置
     * @return
     */
    public char peek() throws IOException {
        if(this.pos  == this.size){
            //fill buffer
            fillBuffer();
        }
        assert(this.pos < this.size);
        return this.buffer[this.pos];
    }

    /**
     * 读取下一个字符，并移动位置
     * @return
     */
    public char next_char() throws IOException {
        if(this.pos  == this.size){
            //fill buffer
            fillBuffer();
        }
        char ch = this.buffer[this.pos];
        this.pos++;
        return ch;
    }

    /**
     * 读取指定长度的字符串
     */
    public String next_str(int size) throws IOException {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(next_char());
        }
        return sb.toString();
    }
}
