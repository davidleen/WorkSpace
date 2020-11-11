package com.nostra13.universalimageloader.core.assist.ws;

import android.text.TextUtils;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;



public class HttpsConfig {




    public static X509TrustManager x509TrustManager=new   X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }


        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }


    };

    static SSLSocketFactoryCompat compat=new SSLSocketFactoryCompat(x509TrustManager);


    public static void config(String url, URLConnection urlConnection)
    {
        boolean useHttps = url.startsWith("https");
        if (useHttps) {
            HttpsURLConnection https = (HttpsURLConnection) urlConnection;
            SSLSocketFactory  oldSocketFactory = trustAllHosts(https);
            HostnameVerifier  oldHostnameVerifier = https.getHostnameVerifier();
            https.setHostnameVerifier(DO_NOT_VERIFY);
        }

    }
    private static String[] VERIFY_HOST_NAME_ARRAY = new String[]{};
    /**
     * 覆盖java默认的证书验证
     */
    private static final TrustManager[] trustAllCerts = new TrustManager[]{x509TrustManager};

    /**
     * 设置不验证主机
     */
    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            if (TextUtils.isEmpty(hostname)) {
                return false;
            }
            return !Arrays.asList(VERIFY_HOST_NAME_ARRAY).contains(hostname);
        }
    };


    /**
     * 信任所有
     *
     * @param connection
     * @return
     */
    private static SSLSocketFactory trustAllHosts(HttpsURLConnection connection) {
        SSLSocketFactory oldFactory = connection.getSSLSocketFactory();
        try {
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            SSLSocketFactory newFactory = sc.getSocketFactory();


            connection.setSSLSocketFactory(compat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oldFactory;
    }


}
