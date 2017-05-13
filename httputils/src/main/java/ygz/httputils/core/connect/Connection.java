package ygz.httputils.core.connect;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ygz.httputils.HttpUtilsClient;
import ygz.httputils.buidler.Headers;
import ygz.httputils.buidler.Request;
import ygz.httputils.buidler.RequestParams;
import ygz.httputils.core.Response;
import ygz.httputils.core.call.CallBack;
import ygz.httputils.core.io.HttpContent;
import ygz.httputils.core.io.IOUtils;

/**
 * Created by YGZ on 2017/5/2.
 */
@SuppressWarnings("unused")
public abstract class Connection {
    protected Request mRequest;
    protected DataOutputStream mOutputStream;
    protected InputStream mInputStream;
    protected URLConnection mUrlConnection;
    protected HttpUtilsClient mClient;

    public Connection(HttpUtilsClient client) {
        this.mClient = client;
    }

    public void connect(Request request, CallBack callBack) {
        this.mRequest = request;
        try {
            String host = mRequest.host();
            String httpUrl = mRequest.url();
            String method = mRequest.method().toUpperCase();
            if ("GET".equals(method) || "DELETE".equals(method)) {
                if (mRequest.params() != null && mRequest.params().getTextParams() != null) {
                    String paramsStr = mRequest.content().intoString();
                    httpUrl = httpUrl + (httpUrl.endsWith("?") ? paramsStr : "?" + paramsStr);
                }
            }
            URL url = new URL(httpUrl);
            if (host == null && mClient.getProxy() == null) {
                mUrlConnection = url.openConnection();
            } else {
                mUrlConnection = url.openConnection(host != null ? new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, mRequest.port())) : mClient.getProxy());

            }
            initConnection();
            initHeader();
            initMethod(method);
            if ("POST".equals(method))
                post(callBack);
            else if ("GET".equals(method))
                get(callBack);
            else if ("PUT".equals(method))
                put(callBack);
            else if ("DELETE".equals(method))
                delete(callBack);
            mInputStream = mUrlConnection.getInputStream();
            callBack.onResponse(new Response(getResponseCode(), mInputStream, mUrlConnection.getHeaderFields(), mRequest.encode(), mUrlConnection.getContentLength()));


        } catch (IOException e) {
            e.printStackTrace();
            callBack.onFailure(e);
        }finally {

        }

    }

    protected abstract int getResponseCode() throws IOException;

    protected abstract void initMethod(String method) throws ProtocolException;

    protected abstract void convertConnect();

    protected void get(CallBack callBack) throws IOException {

    }

    protected void post(CallBack callBack) throws IOException {
        mUrlConnection.setDoOutput(true);
        mUrlConnection.setRequestProperty("Content-Type", getContentType());
        mOutputStream = new DataOutputStream(mUrlConnection.getOutputStream());
        mRequest.content().setOutputStream(mOutputStream);
    }

    protected void put(CallBack callBack) throws IOException {

    }

    protected void delete(CallBack callBack) throws IOException {

    }

    /*初始化基础链接*/
    protected void initConnection() {
        convertConnect();
        mUrlConnection.setUseCaches(true);
        mUrlConnection.setConnectTimeout(mRequest.timeout());
        mUrlConnection.setRequestProperty("Accept-language", "zh-CN");
        mUrlConnection.setRequestProperty("Charset", mRequest.encode());
        mUrlConnection.setRequestProperty("Connection", "keep-Alive");
    }

    /*初始化头部*/
    protected void initHeader() {
        Headers headers = mRequest.headers();
        if (headers != null) {
            Map<String, List<String>> maps = headers.getHeaders();
            if (maps != null) {
                Set<String> sets = maps.keySet();
                for (Iterator<String> iterator = sets.iterator(); iterator.hasNext(); ) {
                    String key = iterator.next();
                    for (String value : maps.get(key)) {
                        mUrlConnection.addRequestProperty(key, value);
                    }
                }
            }
        }
    }

    /*获取Content-Type
    * @return "multipart/form-data||application/json;charset=utf-8||application/x-www-form-urlencoded"*/
    protected String getContentType() {
        RequestParams params = mRequest.params();
        return params != null ? (params.getMultiParams() != null ? "multipart/form-data;boundary=\"" + HttpContent.BOUNDARY + "\"" : "application/x-www-form-urlencode")
                : "application/json;charset=utf-8";
    }

    /*关闭链接和流
    * @param closeables closebles*/
    protected void finish(){
        IOUtils.close(mOutputStream,mInputStream);
    }
    public void disConnect(){

    }
}
