package com.codewarriors4.tiffin.utils;

import android.content.SharedPreferences;

import com.codewarriors4.tiffin.models.SessionDataModel;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Set;

public class SessionUtli
{

    private static SessionUtli instance;
    private SharedPreferences sharedPreferences;
    //private SessionDataModel sessionDataModel;
    private SharedPreferences.Editor edit;
    public SessionUtli(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.edit = sharedPreferences.edit();

        //initModel();
    }

//    private void initModel()
//    {
//        if(sharedPreferences.contains("Token"))
//        {
//            String session = sharedPreferences.getString("Token", "");
//            HashMap hashMap = new Gson().fromJson(session, HashMap.class);
//            for (Object key : hashMap.keySet())
//            {
//                String str = (String) key;
//                (str, hashMap.get(key));
//            }
//
//        }
//    }


    public static SessionUtli getSession(SharedPreferences sharedPreferences)
    {
        if(instance == null)
        {
            return new SessionUtli(sharedPreferences);

        }
        else
        {
            return instance;
        }
    }

    public String getValue(String str)
    {
        return sharedPreferences.getString(str, "");
    }

    public void setValue(String str, Object value)
    {
        edit.putString(str, (String)value);
        edit.apply();
    }

    public void setValues(String values ){
        HashMap hashMap = new Gson().fromJson(values, HashMap.class);
        for (Object key : hashMap.keySet())
        {
            String str = (String) key;
            edit.putString(str, hashMap.get(key) + "");
        }
        edit.apply();
    }

    public void clearAll(){
        sharedPreferences.edit().clear().apply();
    }






}
