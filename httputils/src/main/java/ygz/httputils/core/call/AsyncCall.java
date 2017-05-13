package ygz.httputils.core.call;

import ygz.httputils.HttpUtilsClient;
import ygz.httputils.buidler.Request;
import ygz.httputils.core.connect.Connection;
import ygz.httputils.core.connect.HttpConnection;
import ygz.httputils.core.connect.HttpsConnection;

/**
 * Created by YGZ on 2017/5/1.
 */

public class AsyncCall implements Runnable {
    private CallBack mCallBack;
    private Request mRequest;
    private Connection mConnection;

    public AsyncCall(HttpUtilsClient client,  Request request,CallBack callBack) {
        this.mCallBack = callBack;
        this.mRequest = request;
        mConnection=request.url().startsWith("https")?new HttpsConnection(client):new HttpConnection(client);


    }

    @Override
    public void run() {
        mConnection.connect(mRequest,mCallBack);
    }
    public CallBack getCallBack(){
        return mCallBack;
    }
    public void setCallBack(CallBack mCallBack){
        this.mCallBack=mCallBack;

    }
    public void setRequest(Request mRequest){
        this.mRequest=mRequest;
    }
    public Request getRequest(){
        return mRequest;
    }
    public void setConnection(Connection mConnection){
        this.mConnection=mConnection;
    }
    public Connection getConnection(){
        return mConnection;
    }

    @Override
    public boolean equals(Object o) {
        if (o!=null&& o instanceof AsyncCall){
            return mRequest.url().equalsIgnoreCase(((AsyncCall) o).getRequest().url());
        }
        return super.equals(o);
    }
}
