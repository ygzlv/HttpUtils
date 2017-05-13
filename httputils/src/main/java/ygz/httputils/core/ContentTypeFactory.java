package ygz.httputils.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by YGZ on 2017/4/25.
 */
/*报文数据中的Content-Type格式，支持所有文件*/

public class ContentTypeFactory {
    private Map<String, String> mTypeMap;
    private static ContentTypeFactory mInstance = new ContentTypeFactory();

    private ContentTypeFactory() {
        InputStream is = getClass().getResourceAsStream("/assets/content_type.json");
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bos.write(ch);
            }
            byte data[] = bos.toByteArray();
            bos.close();
            String json = new String(data);

            mTypeMap = new Gson().fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getContentType(String fileName) {
        String end = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")).toLowerCase() : ".*";
        return mTypeMap.containsKey(end) ? mTypeMap.get(end) : mTypeMap.get(".*");
    }
    public static ContentTypeFactory getInstance(){
        return mInstance;
    }
}
