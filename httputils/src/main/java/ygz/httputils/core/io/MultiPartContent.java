package ygz.httputils.core.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ygz.httputils.core.ContentTypeFactory;
import ygz.httputils.buidler.RequestParams;
import ygz.httputils.core.ProgressListener;

/**
 * Created by YGZ on 2017/4/25.
 */
/*HTTP MultiPart RequestBody,请求体包括文本、文件流等*/

public class MultiPartContent extends HttpContent {
    public MultiPartContent(RequestParams params, String encode) {
        super(params, encode);
    }

    @Override
    protected void doOutput() throws IOException {
        outputText();
        outputFileFormData();
        outputEnd();

    }

    private void outputText() throws IOException {
        StringBuffer buffer = new StringBuffer();
        Set<String> set = mParams.getTextParams().keySet();
        Map<String, String> texts = mParams.getTextParams();
        for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
            String key=iterator.next();
            String value=texts.get(key);
            buffer.append(END+DATA_TAG+BOUNDARY+END);
            buffer.append("Content-Disposition:form-data;name=\""+key+"\"");
            buffer.append(END+END);
            buffer.append(value);

        }
        mOutputStream.write(buffer.toString().getBytes(mEncode));
    }
    private void outputFileFormData()throws IOException{
        Set<String> set=mParams.getMultiParams().keySet();
        Map<String,File> fileMap=mParams.getMultiParams();
        for (Iterator<String> iterator=set.iterator();iterator.hasNext();){
            StringBuffer buffer=new StringBuffer();
            String key=iterator.next();
            File file=fileMap.get(key);
            String fileName=file.getName();
            buffer.append(END+DATA_TAG+BOUNDARY+END);
            buffer.append("Content-Disposition:form-data;name=\""+key+"\";fileName=\""+fileName+"\"");
            buffer.append(END);
            buffer.append("Content-Type:"+ ContentTypeFactory.getInstance().getContentType(fileName));
            buffer.append(END+END);
            mOutputStream.writeBytes(buffer.toString());
            outputFile(file);
        }
    }

    private void outputFile(File file) throws IOException{
        DataInputStream in=new DataInputStream(new FileInputStream(file));
        long p=0;
        long length=file.length();
        ProgressListener listener=null;
        int bytes=0;
        onProgress(listener,p,length);
        byte[] bufferOut=new byte[1024*10];
        while ((bytes=in.read(bufferOut))!=-1){
            mOutputStream.write(bufferOut,0,bytes);
            p+=bytes;
            onProgress(listener,p,length);
        }
        in.close();

    }
    private void onProgress(ProgressListener listener,long currentLength,long allLength){
        if (listener!=null)
            listener.onProgress(currentLength,allLength);

    }
    private void intoString(StringBuffer buffer){
        Set<String> set=mParams.getTextParams().keySet();
        Map<String,String> texts=mParams.getTextParams();
        for (Iterator<String> iterator=set.iterator();iterator.hasNext();){
            String key=iterator.next();
            String value=texts.get(key);
            buffer.append(key+"="+value+"&");
        }

    }

    @Override
    public String intoString() {
        StringBuffer buffer=new StringBuffer();
        intoString(buffer);
        return buffer.substring(0,buffer.length());
    }

    @Override
    public long getContentLength() {
        long length=0;
        Map<String,String> text=mParams.getTextParams();
        Map<String ,File> multi=mParams.getMultiParams();
        if (text!=null){
            Set<String> set=text.keySet();
            for (Iterator<String> iterator=set.iterator();iterator.hasNext();){
                length += (END + DATA_TAG + BOUNDARY + END + "Content-Disposition: form-data; name=\"" + iterator.next() + "\"" + END + END).length();
            }
        }
        if (multi!=null){
            Set<String> set=mParams.getMultiParams().keySet();
            for (Iterator<String> iterator=set.iterator();iterator.hasNext();){
                String key=iterator.next();
                File file=multi.get(key);
                String fileName=file.getName();
                length+=("Content-Disposition: form-data; name=\""+key+"\";"+
                        "filename=\""+END+
                "Content-Type:"+ContentTypeFactory.getInstance().getContentType(fileName)+
                END+END).length()+file.length();
            }
        }

        return length;
    }
}
