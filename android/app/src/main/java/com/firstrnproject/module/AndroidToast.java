package com.firstrnproject.module;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by longge on 16/6/8.
 */
public class AndroidToast extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "ToastAndroid";

    private static final String DURATION_SHOT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    public AndroidToast(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public Map<String,Object> getConstants(){
        final Map<String,Object> constants = new HashMap<>();
        constants.put(DURATION_SHOT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void show(String message, int duration){
        Toast.makeText(getReactApplicationContext(),message,duration).show();
    }

    @Override
    public boolean canOverrideExistingModule(){return true;}
}
