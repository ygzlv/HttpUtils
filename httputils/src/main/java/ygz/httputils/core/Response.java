package ygz.httputils.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by YGZ on 2017/4/29.
 */
/*the request response*/
public class Response {
    private Map<String, List<String>> mHeaders;
    private int mCode;
    private String mBody;
    private InputStream mInputStream;
    private String mEncode;
    private int mContentLength;

    public Response(int code, InputStream is, Map<String, List<String>> headers, String encode, int contentLength) {
        this.mHeaders = headers;
        this.mCode = code;
        this.mInputStream = is;
        this.mEncode = encode;
        this.mContentLength = contentLength;
    }
    public int getCode(){
        return mCode;
    }
    public String getBody(){
        if (mBody==null){
            try{
                ByteArrayOutputStream os=new ByteArrayOutputStream();
                int b;
                while ((b=mInputStream.read())!=-1){
                    os.write(b);
                }
                mBody=new String(os.toByteArray(),mEncode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mBody;
    }
}
