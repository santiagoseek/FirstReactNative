package com.firstrnproject.util;

import android.os.Build;
import android.util.Log;

import com.firstrnproject.Constant;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by longge on 16/7/11.
 */
public class HttpDownUtil {
    private static final String LOG_TAG = Constant.TAG + "-" + HttpDownUtil.class.getSimpleName();

    private static final int CONNECT_TIMEOUT =  10 * 1000;

    private static final int READ_TIMEOUT = 10 * 1000;

    public interface HttpFinishedListener{
        public void onPostFinished(String host, int status, String message);
    }

    private HttpDownUtil(){

    }

    static {
        disableConnectionReuseIfNecessary();
    }

    public static String download(String uri){
        BufferedReader bufReader = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.connect();

            // TODO 增加返回值判断
            int code = connection.getResponseCode();
            if(code<200 || code>=300){
                Log.e("aaa","connection responseCode is:" + code);
                return null;
            }
            bufReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();

        } catch(Throwable t){
            Log.e(LOG_TAG, t.getMessage(), t);
            return null;
        }
        finally {
            if(bufReader != null){
                try {
                    bufReader.close();
                } catch (IOException e) {}
            }
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public static int downloadFile(String srcurl,String dir,String fileName){
        if(srcurl==null || srcurl.length()<=1) return -1;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            if(new File(dir).exists()){
                Log.e(Constant.TAG,"dir is exist. dir:" + dir);
            }else{
                new File(dir).mkdirs();
                Log.e(Constant.TAG,"create new dir. dir:" + dir);
            }
            File newFile = new File(dir + fileName);
            if(newFile.exists()){
                Log.e(Constant.TAG,"file alread exists. So break, Existing file is:" + dir + fileName);
                //newFile.delete();
                return 0;
            }
            URL url = new URL(srcurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.connect();

            // TODO 增加返回值判断
            int code = connection.getResponseCode();
            if(code<200 || code>=300){
                return -1;
            }

            inputStream = new BufferedInputStream(connection.getInputStream());
            fileOutputStream = new FileOutputStream(newFile);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int b;
            while((b=inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer,0,b);
            }
            byte[] fileByte = byteArrayOutputStream.toByteArray();
            fileOutputStream.write(fileByte);
            fileOutputStream.close();
            return 1;
        } catch(Throwable t){
            Log.e(LOG_TAG, t.getMessage(), t);
            return -1;
        }
        finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream != null){
                try{
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public static int upload(String uri) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.addRequestProperty("User-Agent", Constant.TAG + " App");
            connection.connect();

            int code = connection.getResponseCode();
            return code;
        }catch(Throwable t){
            Log.e(LOG_TAG, t.getMessage(), t);
            return 0;
        }
        finally {
            if(connection != null){
                connection.disconnect();
            }
        }
    }

    public static String sendRequest(String uri, HttpFinishedListener httpFinishedListener){
        if(uri == null || uri.length()<1) return "";
        BufferedReader bufReader = null;
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(CONNECT_TIMEOUT);
            connection.connect();
            bufReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = "";
            while ((line = bufReader.readLine()) != null) {
                sb.append(line);
            }

            int code = connection.getResponseCode();
            httpFinishedListener.onPostFinished(uri, code, sb.toString());
        } catch(Throwable t){
            httpFinishedListener.onPostFinished(uri, -1, sb.toString());
            Log.e(LOG_TAG, t.getMessage(), t);
            return sb.toString();
        }
        finally {
            if(bufReader != null){
                try {
                    bufReader.close();
                } catch (IOException e) {}
            }
            if(connection != null) {
                connection.disconnect();
            }
        }
        return sb.toString();
    }

    public static String sendPost(String url, String params, HttpFinishedListener httpPostFinishedListener){
        if(url == null || url.length()<1) return "";
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            out = new PrintWriter(conn.getOutputStream());
            out.write(params);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line="";
            while((line = in.readLine()) != null){
                result += "\n" + line;
            }
            int code = conn.getResponseCode();
            httpPostFinishedListener.onPostFinished(url, code, result);
        } catch (Throwable t) {
            httpPostFinishedListener.onPostFinished(url, -1, result);
            Log.e(LOG_TAG, t.getMessage(), t);
        }
        finally {
            try {
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Throwable tt) {
                Log.e(LOG_TAG, tt.getMessage(), tt);
            }
        }
        return result;
    }

    private static void disableConnectionReuseIfNecessary() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    public static String httpsGetRequest(URL realUrl,HttpFinishedListener httpFinishedListener){
        if(realUrl == null){
            return null;
        }
        HttpsURLConnection httpsURLConnection = null;
        BufferedReader bufferedReader = null;
        SSLContext sslContext = null;
        StringBuilder sb = new StringBuilder();
        try{
            TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,trustManagers,new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            httpsURLConnection = (HttpsURLConnection) realUrl.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            httpsURLConnection.connect();
            bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                sb.append(line);
            }
            int code = httpsURLConnection.getResponseCode();
            httpFinishedListener.onPostFinished(realUrl.toString(), code, sb.toString());
        } catch (Throwable t) {
            httpFinishedListener.onPostFinished(realUrl.toString(), -1, sb.toString());
            Log.e(LOG_TAG,t.getMessage(),t);
            return sb.toString();
        }
        finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpsURLConnection != null){
                httpsURLConnection.disconnect();
            }
        }
        return sb.toString();
    }

    public static int downloadFileFromHttps(String srcurl,String dir,String fileName){
        if(srcurl==null || srcurl.length()<=1) return -1;
        BufferedWriter bufferedWriter = null;
        HttpsURLConnection httpsURLConnection = null;
        BufferedReader bufferedReader = null;
        SSLContext sslContext = null;
        StringBuilder sb = new StringBuilder();

        try {
            if(new File(dir).exists()){
                Log.e(Constant.TAG,"dir is exist. dir:" + dir);
            }else{
                new File(dir).mkdirs();
                Log.e(Constant.TAG,"create new dir. dir:" + dir);
            }
            File newFile = new File(dir + fileName);
            if(newFile.exists()){
                Log.e(Constant.TAG,"file alread exists. So break, Existing file is:" + dir + fileName);
                return 0;
            }
            newFile.createNewFile();
            URL url = new URL(srcurl);
            TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,trustManagers,new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            httpsURLConnection.connect();
            bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

            int code = httpsURLConnection.getResponseCode();
            if(code<200 || code>=300){
                return -1;
            }

            bufferedWriter = new BufferedWriter(new FileWriter(newFile));
            String input = null;
            while((input = bufferedReader.readLine()) != null){
                bufferedWriter.write(input);
                bufferedWriter.newLine();
            }
            return 1;
        } catch(Throwable t){
            Log.e(LOG_TAG, t.getMessage(), t);
            return -1;
        }
        finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedWriter != null){
                try{
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
    }

    public static String httpOrhttpsGet(String url){
        if(url == null || url.length()<=1){
            return null;
        }
        String result = "";
        try {
            URL realUrl = new URL(url);
            if("https".equals(realUrl.getProtocol())){
                result = httpsGetRequest(realUrl, new HttpFinishedListener() {
                    @Override
                    public void onPostFinished(String host, int status, String message) {
                        if(status>=200 && status<300){
                            Log.e(LOG_TAG,"Succeed.");
                        }else{
                            Log.e(LOG_TAG,"Fail. Host:" + host + "----Status:" + status + "---message:" + message);
                        }
                    }
                });
            }else{
                result = sendRequest(url, new HttpFinishedListener() {
                    @Override
                    public void onPostFinished(String host, int status, String message) {
                        if(status>=200 && status<300){
                            Log.e(LOG_TAG,"Succeed.");
                        }else{
                            Log.e(LOG_TAG,"Fail. Host:" + host + "----Status:" + status + "---message:" + message);
                        }
                    }
                });
            }
        } catch (Throwable tt) {
            Log.e(LOG_TAG, tt.getMessage(), tt);
        }
        return result;
    }
}
