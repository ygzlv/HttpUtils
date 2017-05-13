package ygz.httputils.buidler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YGZ on 2017/4/20.
 */

public class RequestParams {
    private Map<String,String> textParams;
    private Map<String,File> multiParams;

    public RequestParams(Map<String, String> textParams) {
        textParams =new HashMap<>();
    }
    public RequestParams put(String name,String value){
        textParams.put(name,value);
        return this;
    }
    public RequestParams put(String name,int value){
        return put(name,String.valueOf(value));
    }
    public RequestParams put(String name,long value){
        return put(name,String.valueOf(value));
    }
    public RequestParams put(String name,double value){
        return put(name,String.valueOf(value));
    }
    public RequestParams put(String name,float value){
        return put(name,String.valueOf(value));
    }
    public RequestParams put(String name,byte value){
        return put(name,String.valueOf(value));
    }
    public RequestParams put(String name,boolean value){
        return put(name,String.valueOf(value));
    }
    public RequestParams putFile(String name,File file){
        if (multiParams==null) multiParams=new HashMap<>();
        if (!file.exists())
            return this;
        multiParams.put(name,file);
        return this;

    }
    public RequestParams putFile(String name,String fileName){
        return putFile(name,new File(fileName));
    }
    public Map<String ,String> getTextParams(){
        return textParams;
    }
    public Map<String,File> getMultiParams(){
        return multiParams;
    }
}
