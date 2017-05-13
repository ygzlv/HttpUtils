package ygz.httputils.core.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ygz.httputils.buidler.RequestParams;

/**
 * Created by YGZ on 2017/4/20.
 */
/*定义HttpContent 传输数据，使用策略模式
* FormContent
* JsonContent
* MultiPartContent*/
public abstract class HttpContent {
    public static final String BOUNDARY = "http-utils";
    public static final String DATA_TAG = "--";
    public static final String END = "\r\n";
    protected String mEncode;
    protected RequestParams mParams;
    protected DataOutputStream mOutputStream;

    public HttpContent(RequestParams mParams, String encode) {
        this.mEncode = encode == null ? "UTF-8" : encode;
        this.mParams = mParams;
    }

    public void setOutputStream(DataOutputStream outputStream) throws IOException {
        this.mOutputStream = outputStream;
        doOutput();
    }

    protected String urlEnconde(String value) {
        try {
            return URLEncoder.encode(value, mEncode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected abstract void doOutput() throws IOException;

    public abstract String intoString();

    public void outputEnd()throws IOException{
        mOutputStream.writeBytes(END+DATA_TAG+BOUNDARY+DATA_TAG+END);
        mOutputStream.flush();
        mOutputStream.close();
    }

    public long getContentLength(){
        return 0;
    }
}
