package com.firstrnproject.module;

import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.firstrnproject.activity.ItemActivity;

import java.io.File;

/**
 * Created by test on 16/7/14.
 */
public class CTModuleManager extends ReactContextBaseJavaModule {

	private static final String MODULE_NAME = "CTModuleManager";
	private static Context context = null;

	public CTModuleManager(ReactApplicationContext reactContext) {
		super(reactContext);
		context = reactContext;
	}

	@Override
	public String getName() {
		return MODULE_NAME;
	}

	@ReactMethod
	public void loadModule(String moduleName){
		if(moduleName != null && moduleName.length()>=1){
			File bundleFile = new File(context.getFilesDir()+"/bundles/" + moduleName + "/main.js");
			if(bundleFile.exists()){
				Intent intent = new Intent(getReactApplicationContext(),ItemActivity.class);
				intent.putExtra("BundleName",bundleFile.getPath());
				intent.putExtra("ProjectName",moduleName);
				getCurrentActivity().startActivity(intent);
			}

		}
	}
}
