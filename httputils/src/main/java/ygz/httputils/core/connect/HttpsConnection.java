package ygz.httputils.core.connect;

import java.io.IOException;
import java.net.ProtocolException;

import javax.net.ssl.HttpsURLConnection;

import ygz.httputils.HttpUtilsClient;

/**
 * Created by YGZ on 2017/5/4.
 */

public class HttpsConnection extends Connection {
    private HttpsURLConnection mConnection;

    public HttpsConnection(HttpUtilsClient client) {
        super(client);
    }

    @Override
    protected int getResponseCode() throws IOException {
        return mConnection.getResponseCode();
    }

    @Override
    protected void initMethod(String method) throws ProtocolException {
        mConnection.setRequestMethod(method);
    }

    @Override
    protected void convertConnect() {
        mConnection = (HttpsURLConnection) mUrlConnection;
        mConnection.setSSLSocketFactory(mClient.getSslSocketFactory());
    }

    @Override
    public void disConnect() {
        if (mConnection!=null){
            mConnection.disconnect();
        }
    }
}
