package com.cj.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonObject {
    private Map<String,Object> map = new HashMap<>();

    public Map<String, Object> getMap() {
        return map;
    }

    public void put(String key,Object value){
        map.put(key,value);
    }

    public Object get(String key){
        return map.get(key);
    }

    public List<Map.Entry<String,Object>> getEntry(){
        return new ArrayList<>(this.map.entrySet());
    }
}
