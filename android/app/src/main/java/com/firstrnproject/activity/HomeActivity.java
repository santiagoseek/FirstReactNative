package com.firstrnproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.facebook.react.BuildConfig;
import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.firstrnproject.shell.CommonReactPackage;
import com.firstrnproject.util.BundleManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by test on 16/7/13.
 */
public class HomeActivity extends Activity implements DefaultHardwareBackBtnHandler{
	private ReactRootView mReactRootView;
	private ReactInstanceManager mReactInstanceManager;

	@Override
	public void invokeDefaultOnBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("ssss","MyReactActivity onCreate...");
		mReactRootView = new ReactRootView(this);
		mReactInstanceManager = ReactInstanceManager.builder()
				.setApplication(getApplication())
				//.setBundleAssetName("index.android3.bundle")
				//.setJSMainModuleName("Awesome3")
                .setBundleAssetName("main.js")
                .setJSMainModuleName("home")
				//.setBundleAssetName("index.android.bundle")
                //.setJSMainModuleName("firstRNProject")
				//.setJSBundleFile("/sdcard/Download/index.android2.bundle")
				.addPackage(new MainReactPackage())
				.addPackage(new CommonReactPackage())
				.setUseDeveloperSupport(BuildConfig.DEBUG)
				.setInitialLifecycleState(LifecycleState.RESUMED)
				.build();


		//mReactRootView.startReactApplication(mReactInstanceManager, "firstRNProject", null);
		mReactRootView.startReactApplication(mReactInstanceManager, "home", null);
		//mReactRootView.startReactApplication(mReactInstanceManager, "Awesome3", null);
		setContentView(mReactRootView);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mReactInstanceManager != null) {
			mReactInstanceManager.onHostPause();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mReactInstanceManager != null) {
			mReactInstanceManager.onHostResume( this, this );
		}
	}

	@Override
	public void onBackPressed() {
		if (mReactInstanceManager != null) {
			mReactInstanceManager.onBackPressed();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
			mReactInstanceManager.showDevOptionsDialog();
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_A){
			//startActivity(new Intent());
			//setBundleFile("/sdcard/Download/index.android3.bundle");
//            Intent intent = new Intent(MyReactActivity.this,ItemActivity.class);
//            intent.putExtra("BundleName","/sdcard/Download/index.android3.bundle");
//            intent.putExtra("ProjectName","Awesome3");
//            startActivity(intent);
			test();
		}else if (keyCode == KeyEvent.KEYCODE_S){
			Intent intent = new Intent(HomeActivity.this,ItemActivity.class);
//            intent.putExtra("BundleName","/sdcard/Download/index.android3.bundle");
//            intent.putExtra("ProjectName","Awesome3");
			intent.putExtra("BundleName","/sdcard/Download/main.js");
			intent.putExtra("ProjectName","kpi");
			startActivity(intent);
		}
		return super.onKeyUp(keyCode, event);
	}

    /*
    "/sdcard/Download/index.android3.bundle",Awesome3
     */
//    public static void goToItemPage(String bundleName, String projectName){
//        Intent intent = new Intent(getApplicationContext(),ItemActivity.class);
//        intent.putExtra("BundleName", Constant.TEMPPATH + bundleName);
//        intent.putExtra("ProjectName",projectName);
//        startActivity(intent);
//    }

	public void test(){

		InputStream comm = null;
		InputStream main = null;
		try {
			comm = getAssets().open("common.jsbundle");
			main =  getAssets().open("main0.jsbundle");
		} catch (IOException e) {
			e.printStackTrace();
		}
		BundleManager.mergeBundleFilesFromInputStream(comm,main);

		Intent intent = new Intent(HomeActivity.this,ItemActivity.class);
		intent.putExtra("BundleName","/sdcard/test/index.android.bundle");
		intent.putExtra("ProjectName","moles");
		startActivity(intent);
	}
}
