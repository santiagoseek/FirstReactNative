package com.firstrnproject.module;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by longge on 16/6/8.
 */
public class OperateActivity extends ReactContextBaseJavaModule {

    private static final String MODULE_NAME = "OperateActivity";

    public OperateActivity(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void gotoHomeActivity(){

        //Intent intent = new Intent(OperateActivity.this, MyReactActivity.class);

    }
}
