package ygz.httputils.core.connect;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import ygz.httputils.core.io.IOUtils;

/**
 * Created by YGZ on 2017/5/5.
 */
/*using https ssl 证书管理工具*/
public class SSLManager {
    private static SSLSocketFactory mDefaultSslSocketFactory;
    private SSLSocketFactory mSslSoketFactory;

    private synchronized SSLSocketFactory getDefaultSslSocketFactory(){
        if (mDefaultSslSocketFactory==null){
            try{
                SSLContext sslContext=SSLContext.getInstance("TLS");
                sslContext.init(null,null,null);
            } catch (GeneralSecurityException e) {
               throw new AssertionError();//The system has no TLS. Just give up.

            }
        }
        return mDefaultSslSocketFactory;
    }
    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory){
        this.mSslSoketFactory=sslSocketFactory;
    }
    public static KeyStore getKeyStore(String keyStorePath,String pwd)throws Exception{
        KeyStore ks=KeyStore.getInstance("JKS");
        FileInputStream is=new FileInputStream(keyStorePath);
        ks.load(is,pwd.toCharArray());
        return ks;
    }
    public static SSLContext getSSLContext(String keyStorePath,String pwd,String trustStorePath)throws Exception{
        KeyManagerFactory keyManagerFactory=KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore=getKeyStore(pwd,keyStorePath);
        keyManagerFactory.init(keyStore,pwd.toCharArray());
        TrustManagerFactory trustManagerFactory=TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore=getKeyStore(pwd,trustStorePath);
        trustManagerFactory.init(trustStore);
        SSLContext sslContext=SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(),null);
        return sslContext;

    }
    /*添加证书文件流，可添加多个*/
    public void setSslSocketFactory(InputStream... cerInputStream){
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index=0;
            for (InputStream is:cerInputStream){
                X509Certificate cert= (X509Certificate) certificateFactory.generateCertificate(is);
                keyStore.setCertificateEntry("alias"+index,cert);
            }
            SSLContext sslContext=SSLContext.getInstance("TLS");//安全数据层
            //信任证书管理工厂
            TrustManagerFactory trustManagerFactory=TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(null,trustManagerFactory.getTrustManagers(),new SecureRandom());

            mSslSoketFactory=sslContext.getSocketFactory();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(cerInputStream);
        }
    }

    /*添加证书文件，可添加多个*/
    public void setSslSocketFactory(String... cerPaths){
        FileInputStream[] cers=new FileInputStream[cerPaths.length];
        for (int i=0;i<cerPaths.length;i++){
            File file=new File(cerPaths[i]);
            if (file.exists()){
                try{
                    cers[i]=new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        setSslSocketFactory(cers);
    }
    /*添加证书文本*/
    public void setSslSocketFactoryAsString(String... cerValues){
        ByteArrayInputStream[] cers=new ByteArrayInputStream[cerValues.length];
        for (int i=0;i<cerValues.length;i++){
            cers[i]=new ByteArrayInputStream(cerValues[i].getBytes());
        }
        setSslSocketFactory(cers);
    }
    public SSLSocketFactory getSslSocketFactory(){
        return mSslSoketFactory==null?getDefaultSslSocketFactory():mSslSoketFactory;
    }
}
