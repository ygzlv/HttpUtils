package ygz.httputils.core;

/**
 * Created by YGZ on 2017/4/26.
 */
/*上传文件进度监听*/
public interface ProgressListener {
    void onProgress(long currentLength,long allLength);
}
