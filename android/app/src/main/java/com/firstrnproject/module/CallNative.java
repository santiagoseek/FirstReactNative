package com.firstrnproject.module;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.firstrnproject.activity.HomeActivity;
import com.firstrnproject.activity.ItemActivity;
import com.firstrnproject.util.HttpDownUtil;
import com.firstrnproject.util.StringUtil;



/**
 * Created by longge on 16/6/8.
 */
public class CallNative extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "CallNative";
    private Context context;

    public CallNative(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }


    @ReactMethod
    public void callNativeWithCallback(String pluginName, String method, ReadableMap parameters, Callback callback){}

    //startActivity  goToHome/openItem {}/{url:'/sdcard/Download/index.android2.bundle',projectName:"Awesome2"}
    @ReactMethod
    public void callNative(String pluginName, String method, ReadableMap parameters){

        if(StringUtil.equalsIgnoreCase("startActivity",pluginName)){
            Log.e("aaa",method);
            if(StringUtil.equalsIgnoreCase("goToHome",method)){
                getCurrentActivity().startActivity(new Intent(getReactApplicationContext(), HomeActivity.class));
            }else if(StringUtil.equalsIgnoreCase("openItem",method)){
                String url = parameters.getString("url");
                String projectName = parameters.getString("projectName");
                if(url.length()>=1 && projectName.length()>=1){
                    Intent intent = new Intent(getReactApplicationContext(),ItemActivity.class);
                    intent.putExtra("BundleName","/sdcard/Download/index.android3.bundle");
                    intent.putExtra("ProjectName","Awesome3");
                    getCurrentActivity().startActivity(intent);
                }
            }
        }else if(StringUtil.equalsIgnoreCase("downloadFile",pluginName)){
            if(StringUtil.equalsIgnoreCase("downloadFile",method)){
                Log.e("callNative",pluginName + "----" + method);
                final String fileSrc = "http://pic002.cnblogs.com/images/2012/465866/2012111115224986.jpg";
                final String desDir = context.getFilesDir() + "/bundles/";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int result = HttpDownUtil.downloadFile(fileSrc,desDir,"down.jpg");
                        int result1 = HttpDownUtil.downloadFile("http://www.baidu.com",desDir,"baidu.html");
                        Log.e("aaa", "downloadFile the result is:" + result + "---result1:" + result1);
                    }
                }).start();

            }
        }else if(StringUtil.equalsIgnoreCase("downloadConfigJson",pluginName)){
            if(StringUtil.equalsIgnoreCase("download",method)){
                final String downUrl = "https://opsgateway.ctrip.com/mobileapp_21232f297a57a5a743894a0e4a801fc3/FX/kpi/module.json";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String json =  HttpDownUtil.httpOrhttpsGet(downUrl);
                        if(json == null || json.trim().length()<1){
                            Log.e("aaa","downloadConfigJson fail, url is:" + downUrl);
                        }
                        Log.e("aaa","result json is:" + json);
                        JSONObject jsonObject = JSON.parseObject(json);
                        if(jsonObject != null && jsonObject.size()>=1){
                            Log.e("aaa","version:" + jsonObject.get("version"));
                            Log.e("aaa","download url is:" + jsonObject.get("download"));
                            HttpDownUtil.downloadFile(jsonObject.get("download").toString(),context.getFilesDir()+"/bundles/","kpi.zip");
                        }
                    }
                }).start();


            }
        }

    }


}
