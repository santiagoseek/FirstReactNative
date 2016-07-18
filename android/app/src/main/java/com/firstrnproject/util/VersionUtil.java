package com.firstrnproject.util;

/**
 * Created by longge on 16/7/12.
 */
public class VersionUtil {
    public static boolean isNewVersion(String localVersion,String onlineVersion){
        if(localVersion == null || onlineVersion == null || localVersion.equals(onlineVersion)){
            return false;
        }
        String[] localArray = localVersion.split(".");
        String[] onlineArray = onlineVersion.split(".");

        int length = localArray.length < onlineArray.length ? localArray.length:onlineArray.length;
        for(int i = 0;i<length;i++){
            if(Integer.parseInt(onlineArray[i])>Integer.parseInt(localArray[i])){
                return true;
            }else if(Integer.parseInt(onlineArray[i])<Integer.parseInt(localArray[i])){
                return false;
            }
        }
        return length == localArray.length ? true:false;
    }
}
