package ygz.httputils;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;

import javax.net.ssl.SSLSocketFactory;

import ygz.httputils.buidler.Request;
import ygz.httputils.core.Dispatcher;
import ygz.httputils.core.call.Call;
import ygz.httputils.core.call.RealCall;
import ygz.httputils.core.connect.SSLManager;

/**
 * Created by YGZ on 2017/5/2.
 */
/*Http客户端*/
@SuppressWarnings("unused")
public class HttpUtilsClient {
    private Dispatcher mDispatcher;

    private Proxy mProxy;//为当前客户端开启全局代理

    private SSLManager mSslManager;

    public HttpUtilsClient() {
        mDispatcher = new Dispatcher();
        mSslManager = new SSLManager();
    }

    public Call newCall(Request request) {
        return new RealCall(this, request);
    }

    public Call newCall(String url) {
        return new RealCall(this, getDefaultRequest(url));
    }

    public Dispatcher dispatcher() {
        return mDispatcher;
    }


    public Proxy getProxy() {
        return mProxy;
    }

    public void setProxy(Proxy proxy) {
        this.mProxy = proxy;
    }

    public void setProxy(String host,int port){
        this.mProxy=new Proxy(Proxy.Type.HTTP,new InetSocketAddress(host,port));
    }

    public HttpUtilsClient setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        mSslManager.setSslSocketFactory(sslSocketFactory);
        return this;
    }

    public HttpUtilsClient setSslSocketFactory(InputStream... cerInputStream) {
        mSslManager.setSslSocketFactory(cerInputStream);
        return this;
    }

    public HttpUtilsClient setSslSocketFactory(String... cerPaths) {
        mSslManager.setSslSocketFactory(cerPaths);
        return this;
    }

    public HttpUtilsClient setSslSocketFactory(String cerValues){
        mSslManager.setSslSocketFactoryAsString(cerValues);
        return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return mSslManager.getSslSocketFactory();
    }

    Request getDefaultRequest(String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }
}
