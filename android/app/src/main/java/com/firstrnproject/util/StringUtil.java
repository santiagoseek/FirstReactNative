package com.firstrnproject.util;

/**
 * Created by longge on 16/6/8.
 */
public class StringUtil {
    public static boolean emptyOrNull(String str){
        return str == null  || str.length() == 0;
    }

    public static boolean equals(String str1, String str2){
        return str1 == null? str2 == null : str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2){
        return str1 == null? str2 == null : str1.equalsIgnoreCase(str2);
    }

    public static boolean emptyOrNull(String... arrStr){
        String[] strArr = arrStr;
        int var = arrStr.length;

        for (int i = 0;i<var;i++){
            String str = strArr[i];
            if(emptyOrNull(str)){
                return  true;
            }
        }
        return false;
    }
}
