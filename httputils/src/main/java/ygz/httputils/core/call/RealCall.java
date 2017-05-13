package ygz.httputils.core.call;

import ygz.httputils.HttpUtilsClient;
import ygz.httputils.buidler.Request;

/**
 * Created by YGZ on 2017/5/1.
 */

public class RealCall implements Call {
    private HttpUtilsClient mClient;
    private Request mRequest;
    private AsyncCall mAsyncCall;

    public RealCall(HttpUtilsClient client, Request request) {
        this.mClient = client;
        this.mRequest = request;
    }

    @Override
    public void execute(CallBack callBack) {
        if (mAsyncCall == null) {
            mAsyncCall = new AsyncCall(mClient, mRequest, callBack);
            mClient.dispatcher().enqueue(mAsyncCall);

        }
    }

    @Override
    public void cancel() {
        mAsyncCall.getConnection().disConnect();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
