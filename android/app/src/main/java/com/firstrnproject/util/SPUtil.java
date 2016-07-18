package com.firstrnproject.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.firstrnproject.Constant;

import java.util.Map;

/**
 * Created by longge on 16/7/12.
 */
public class SPUtil {
    private static String tag = Constant.TAG + "SPUtil";

    private static volatile SPUtil instance = null;

    private SharedPreferences settings = null;

    private SPUtil(Context context){
        if(context != null){
            this.settings = context.getSharedPreferences("Awesome_SP", Context.MODE_MULTI_PROCESS);
        }
    }

    public static SPUtil getInstance(Context context){
        if(instance == null){
            synchronized (SPUtil.class){
                if(instance == null){
                    instance = new SPUtil(context);
                }
            }
        }
        return instance;
    }

    public void commit(String key, String value){
        if(key == null || key.trim().length() < 1 || value == null){
            return;
        }
        if(settings != null){
            SharedPreferences.Editor editor = this.settings.edit();
            editor.putString(key,value);
            editor.commit();
        }
    }

    public void commit(Map<String,String> map){
        if(this.settings != null && map != null && !map.isEmpty()){
            SharedPreferences.Editor editor = this.settings.edit();
            for(Map.Entry<String,String> entry : map.entrySet()){
                editor.putString(entry.getKey(),entry.getValue());
            }
            editor.commit();
        }
    }

    public String getStringProperty(String key, String defVal){
        try{
            if(this.settings == null){
                return defVal;
            }else{
                String value = settings.getString(key,defVal);
                if(value == null || value.trim().length() < 1){
                    value = defVal;
                }
                return value;
            }
        }catch(Exception e){
            Log.e(tag, e.getMessage());
            return defVal;
        }
    }

    public int getIntProperty(String key, int defVal){
        try{
            String value = getStringProperty(key,String.valueOf(defVal));
            return Integer.parseInt(value);
        }catch(Exception e){
            Log.e(tag,e.getMessage());
            return defVal;
        }
    }

    public long getLongProperty(String key, long defVal){
        try{
            String value = getStringProperty(key,String.valueOf(defVal));
            return Long.getLong(value);
        }catch(Exception e){
            Log.e(tag,e.getMessage());
            return defVal;
        }
    }
}
