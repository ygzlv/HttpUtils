package ygz.httputils.core.io;

import java.io.IOException;

/**
 * Created by YGZ on 2017/4/25.
 */
/*Json POST请求*/
public class JsonContent extends HttpContent {
    private String mJson;

    public JsonContent(String json ) {
        super(null, "UTF-8");
        this.mJson=json;
    }
    public JsonContent(String json,String encode){
        super(null,encode);
        this.mJson=json;
    }
    public void setJson(String mJson){
        this.mJson=mJson;
    }
    public String getJson(){
        return mJson;
    }

    @Override
    protected void doOutput() throws IOException {
        mOutputStream.write(mJson.getBytes(mEncode));
    }

    @Override
    public String intoString() {
        return mJson;
    }

    @Override
    public long getContentLength() {
        return mJson.length();
    }
}
