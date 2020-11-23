package com.cj.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonArray {
    private List list = new ArrayList();

    public List getList() {
        return list;
    }

    public void add(Object obj){
        list.add(obj);
    }

    public Object get(int index){
        return list.get(index);
    }

    public Iterator iterator(){
        return list.iterator();
    }
}
