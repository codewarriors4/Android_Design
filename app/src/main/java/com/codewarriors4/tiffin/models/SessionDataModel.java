package com.codewarriors4.tiffin.models;

import java.util.HashMap;

public class SessionDataModel
{
    private HashMap<String, Object> params;

    public SessionDataModel(){
        params = new HashMap<>();
    }

    public Object getValue(String key){
        return params.get(key);
    }

    public void setValue(String key, Object value){
        this.params.put(key, value);
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }
}
