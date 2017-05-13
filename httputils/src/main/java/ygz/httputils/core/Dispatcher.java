package ygz.httputils.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ygz.httputils.core.call.AsyncCall;

/**
 * Created by YGZ on 2017/5/9.
 */
/*请求分发器，控制并发量*/
public class Dispatcher {
    private ExecutorService mExecutorService;

    public Dispatcher() {
        this.mExecutorService = new ThreadPoolExecutor(3,Integer.MAX_VALUE,60, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
    }
    public void enqueue(AsyncCall call){
        this.mExecutorService.execute(call);
    }
}
