package ygz.httputils.core.io;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ygz.httputils.buidler.RequestParams;

/**
 * Created by YGZ on 2017/4/25.
 */

/*Form表单的形式*/
public class FormContent extends HttpContent {

    public FormContent(RequestParams mParams, String encode) {
        super(mParams, encode);
    }

    @Override
    protected void doOutput() throws IOException {
        StringBuffer buffer = new StringBuffer();
        intoString(buffer);
        mOutputStream.write(buffer.substring(0,buffer.length()-1).getBytes(mEncode));


    }

    @Override
    public String intoString() {
        if (mParams.getTextParams()==null||mParams.getTextParams().size()==0)
        return "";
        StringBuffer buffer=new StringBuffer();
        intoString(buffer);
        return buffer.substring(0,buffer.length()-1);
    }

    private void intoString(StringBuffer buffer) {
        Set<String> set = mParams.getTextParams().keySet();
        Map<String, String> texts = mParams.getTextParams();
        for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value=texts.get(key);
            buffer.append(key+"="+value+"&");

        }

    }
}
