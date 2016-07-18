package com.firstrnproject.service;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.firstrnproject.Constant;
import com.firstrnproject.util.HttpDownUtil;
import com.firstrnproject.util.SPUtil;
import com.firstrnproject.util.VersionUtil;
import com.firstrnproject.util.ZipUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by longge on 16/7/12.
 */
public class LoadVersionService implements Runnable {

    private static final String tag = Constant.TAG + "LoadVersionService";
    private Thread loadVersionThread;
    private Context context;

    public LoadVersionService(Context context){
        this.context = context;
        this.loadVersionThread = new Thread(this,"LoadVersion-Thread");
        this.loadVersionThread.setPriority(Thread.MAX_PRIORITY);
    }

    public void startLoadVersion(){
        if(!loadVersionThread.isAlive()){
            this.loadVersionThread.start();
        }
    }

    public Map<String,String> loadVersion(String url){
        if(url == null || url.length()<=1) return null;
        Map<String,String> result = new HashMap<>(4);
        String json =  HttpDownUtil.httpOrhttpsGet(url);
        if(json == null || json.trim().length()<1){
            Log.e(tag,"downloadConfigJson fail, url is:" + url);
        }
        Log.d(tag,"result json is:" + json);
        result = (Map<String, String>) JSON.parse(json);
//        JSONObject jsonObject = JSON.parseObject(json);
//        if(jsonObject != null && jsonObject.size()>=1){
//            Object ver = jsonObject.get("version");
//            Object download = jsonObject.get("download");
//            result.put("version",ver == null ? "":ver.toString());
//            result.put("download",download == null ? "":download.toString());
//        }
        return result;
    }

    @Override
    public void run() {
        Map<String,String> versionMap = loadVersion(Constant.MAINVERURL);
        if(versionMap != null && versionMap.size()>=1){
            String ver = versionMap.get("version");
            String downUrl = versionMap.get("androidUrl");
            if(updateHome(ver,downUrl)){
                SPUtil.getInstance(context).commit(Constant.HOMEDESC,versionMap.get("desc"));
                SPUtil.getInstance(context).commit(Constant.HOMEVER,versionMap.get("version"));
                SPUtil.getInstance(context).commit(Constant.HOMEDOWNLOADURL,versionMap.get("plist"));
            }else{
                Log.e(tag,"updateHome fail!");
            }
        }
        Map<String,String> kpiVersionMap = loadVersion(Constant.KPIVERURL);
        if(kpiVersionMap != null && kpiVersionMap.size()>=1){
            String ver = kpiVersionMap.get("version");
            String downUrl = kpiVersionMap.get("download");
            if(updateKpi(ver,downUrl)){
                SPUtil.getInstance(context).commit(Constant.KPIVER,kpiVersionMap.get("version"));
                SPUtil.getInstance(context).commit(Constant.KPIDOWNLOADURL,kpiVersionMap.get("download"));
            }else{
                Log.d(tag,"updateKpi fail!");
            }

        }
    }

    public boolean updateHome(String onlineVersion,String downloadUrl){
        if(VersionUtil.isNewVersion(SPUtil.getInstance(context).getStringProperty(Constant.HOMEVER,"1.0.0"),onlineVersion)){
            int status = HttpDownUtil.downloadFile(downloadUrl,context.getFilesDir().toString(),"main.apk");
            return status == 1 ? true:false;
        }else{
            Log.d(tag,"Not found the New Home Version. onlineVersion is:" + onlineVersion);
        }
        return false;
    }

    public boolean updateKpi(String onlineVersion,String downloadUrl){
        if(VersionUtil.isNewVersion(SPUtil.getInstance(context).getStringProperty(Constant.KPIVER,"1.0.0"),onlineVersion)){
            String descPath = context.getFilesDir() + "/bundles/";
            int status = HttpDownUtil.downloadFile(downloadUrl,descPath,"kpi.zip");
            if(status == 1){
                ZipUtil.unZip(new File(descPath+"kpi.zip"),descPath+"kpi/");
                Log.e(tag,"kpi.zip download succeed!");
            }
            return status == 1 ? true:false;
        }else{
            Log.d(tag,"Not found the New Kpi Version. onlineVersion is:" + onlineVersion);
        }
        return false;
    }
}
