package com.paul90317.mcsync.util;

import java.io.*;
import java.util.HashMap;

import com.google.gson.Gson;

public class JsonMap extends HashMap {
    public static Gson gson = new Gson();
    public JsonMap(){
        super();
    }
    public JsonMap(HashMap hashMap){
        super();
        hashMap.forEach((key,value)->{
            put(key,value);
        });
    }
    public void loads(String s){
        var temp = gson.fromJson(s, HashMap.class);
        clear();
        temp.forEach((key,value)->{
            put(key,value);
        });
    }
    public void load(String filename) throws FileNotFoundException, IOException
    {
        var file = new File(filename);
        byte[] data = new FileInputStream(file).readAllBytes();
        loads(new String(data, "utf8"));
    }
    public String dumps(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public void dump(String filename) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        var fs = new FileOutputStream(filename);
        byte[] data = dumps().getBytes("utf8");
        fs.write(data);
    }
}
