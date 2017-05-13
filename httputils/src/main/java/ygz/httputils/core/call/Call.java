package ygz.httputils.core.call;

/**
 * Created by YGZ on 2017/5/1.
 */
/*执行请求*/
public interface Call {
    void execute(CallBack callBack);//执行一个请求
    void cancel();//取消一个http请求
}
