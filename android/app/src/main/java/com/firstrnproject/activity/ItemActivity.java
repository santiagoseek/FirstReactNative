package com.firstrnproject.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.facebook.react.BuildConfig;
import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.firstrnproject.shell.CommonReactPackage;

/**
 * Created by test on 16/7/13.
 */
public class ItemActivity extends Activity implements DefaultHardwareBackBtnHandler {

	private ReactRootView mReactRootView;
	private ReactInstanceManager mReactInstanceManager;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		Intent intent = getIntent();
		String bundleName = intent.getStringExtra("BundleName");
		String projectName = intent.getStringExtra("ProjectName");

		mReactRootView = new ReactRootView(this);
		mReactInstanceManager = ReactInstanceManager.builder()
				.setApplication(getApplication())
				.setJSBundleFile(bundleName)
				.setJSMainModuleName(projectName)
				.addPackage(new MainReactPackage())
				.addPackage(new CommonReactPackage())
				.setUseDeveloperSupport(BuildConfig.DEBUG)
				.setInitialLifecycleState(LifecycleState.RESUMED)
				.build();

		mReactRootView.startReactApplication(mReactInstanceManager, projectName, null);
		setContentView(mReactRootView);
	}

	@Override
	public void invokeDefaultOnBackPressed() {
		super.onBackPressed();
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
		if(keyCode == KeyEvent.KEYCODE_H){
			gotoHomeActivity();
			//setBundleFile("/sdcard/Download/index.android3.bundle");
			// startActivity(new Intent(MyReactActivity.this,TestActivity.class));
		}
		return super.onKeyUp(keyCode, event);
	}

	public void gotoHomeActivity(){
		Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
		startActivity(intent);
	}
}
