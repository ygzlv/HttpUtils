package ygz.httputils.buidler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YGZ on 2017/4/19.
 */

public class Headers {
    private Map<String ,List<String>> mHeaders;
    private Headers(Builder builer){
        this.mHeaders=builer.mHeaders;
    }
    public Map<String, List<String>> getHeaders(){
        return mHeaders;
    }
    public void setmHeaders(Map<String,List<String>> mHeaders){
        this.mHeaders=mHeaders;
    }

    public static final class Builder{
        private Map<String,List<String>> mHeaders;

        public Builder() {
            mHeaders=new HashMap<>();
        }
        public Builder addHeader(String name,String value){
            checkNotNull(name,value);
            if (mHeaders.containsKey(name)){
                if (mHeaders.get(name)==null) mHeaders.put(value,new ArrayList<String>());
                mHeaders.get(name).add(value);
            }else {
                List<String> h=new ArrayList<>();
                h.add(value);
                mHeaders.put(name,h);
            }
            return this;
        }
        public Builder setHeader(String name,String value){
            if (mHeaders.containsKey(name)){
                mHeaders.remove(name);

            }
            List<String> h=new ArrayList<>();
            h.add(value);
            mHeaders.put(name,h);
            return this;
        }
        private void checkNotNull(String name,String value){
            if (name==null) throw new NullPointerException("name can not be null");
            if (value==null) throw new NullPointerException("value can not be null");
        }
        public Headers build(){
            return new Headers(this);
        }
    }
}
