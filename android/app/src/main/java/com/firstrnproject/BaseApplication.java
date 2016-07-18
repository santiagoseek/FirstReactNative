package com.firstrnproject;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.firstrnproject.service.LoadVersionService;

import java.io.File;

/**
 * Created by longge on 16/7/8.
 */
public class BaseApplication extends Application {
    private static final String tag = "firstrnproject---BaseApplication";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //initTempFolder(context);
        new LoadVersionService(context).startLoadVersion();
    }

    private void initApp(Context context){
//        HashMap<String,String> map = new HashMap<>();
//        map.put(Constant.HOMEVER,Constant.VER);
//        map.put(Constant.KPIVER,Constant.VER);
//        SPUtil.getInstance(context).commit(map);
    }

    private void initTempFolder(Context context){

        File file = context.getFilesDir();
        String filePath = file.getAbsolutePath();

        Log.e(tag, "root file Path:" + filePath);
        File bundleFolder = new File(filePath + "/bundles");
        bundleFolder.mkdirs();
        if(bundleFolder.exists() && bundleFolder.isDirectory()){
            Log.e(tag, bundleFolder.getAbsolutePath());
        }

    }

}
