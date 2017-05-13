package ygz.httputils.core.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by YGZ on 2017/4/24.
 */
/*IO流操作*/
public final class IOUtils {
    public static void close(Closeable...closeables){
        for (Closeable cb:closeables){
            try{
                if (null==cb){
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


