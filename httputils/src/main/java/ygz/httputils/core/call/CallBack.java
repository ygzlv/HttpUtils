package ygz.httputils.core.call;

import ygz.httputils.core.Response;

/**
 * Created by YGZ on 2017/4/29.
 */
/*request callback*/
public interface CallBack {
    void onResponse(Response response);
    void onFailure(Exception e);
}
