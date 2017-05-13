package ygz.httputils.core.connect;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

import ygz.httputils.HttpUtilsClient;

/**
 * Created by YGZ on 2017/5/4.
 */

public class HttpConnection extends Connection {
    private HttpURLConnection mConnecttion;

    public HttpConnection(HttpUtilsClient client) {
        super(client);
    }

    @Override
    protected int getResponseCode() throws IOException {
        return mConnecttion.getResponseCode();
    }

    @Override
    protected void initMethod(String method) throws ProtocolException {
        mConnecttion.setRequestMethod(method);
    }

    @Override
    protected void convertConnect() {
        mConnecttion = (HttpURLConnection) mUrlConnection;
        mConnecttion.setRequestProperty("Cache-Control","on-cache");
        mConnecttion.setUseCaches(false);
        mConnecttion.setAllowUserInteraction(false);
        mConnecttion.setChunkedStreamingMode(1024);
    }

    @Override
    public void disConnect() {
        if (mConnecttion!=null){
            mConnecttion.disconnect();
        }
    }
}
