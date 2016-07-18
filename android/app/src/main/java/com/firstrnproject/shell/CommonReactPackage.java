package com.firstrnproject.shell;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.firstrnproject.module.AndroidToast;
import com.firstrnproject.module.CTModuleManager;
import com.firstrnproject.module.CallNative;
import com.firstrnproject.module.OperateActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by longge on 16/6/8.
 */
public class CommonReactPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new AndroidToast(reactContext));
        modules.add(new CallNative(reactContext));
        modules.add(new OperateActivity(reactContext));
        modules.add(new CTModuleManager(reactContext));
        return modules;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
