package com.firstrnproject.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by longge on 16/6/12.
 */
public class BundleManager {
    public static String mergeBundleFilesFromInputStream(InputStream commonInputStream, InputStream mainInputStream){
        String filePath = "/sdcard/test/index.android.bundle";
        File bundleFile = new File(filePath);
        if(bundleFile.exists()) bundleFile.delete();
        bundleFile = new File(filePath);

        try{
            bundleFile.createNewFile();
            BufferedInputStream commonIns = new BufferedInputStream(commonInputStream);
            BufferedInputStream mainIns = new BufferedInputStream(mainInputStream);
            int len = commonIns.available();
            byte[] data = new byte[len];
            commonIns.read(data);
            FileOutputStream fileOutputStream = new FileOutputStream(bundleFile);
            fileOutputStream.write(data);
            int mainLen = mainIns.available();
            byte[] mainData = new byte[mainLen];
            mainIns.read(mainData);
            fileOutputStream.write(mainData);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }


    public static String mergeBundleFiles(File commonFile, File mainFile){
        String filePath = "/sdcard/Download/test/index.android.bundle";
        File bundleFile = new File(filePath);
        if(bundleFile.exists()) bundleFile.delete();
        bundleFile = new File(filePath);
        FileInputStream commonInputStream = null;
        FileInputStream mainInputStream = null;

        try{
            commonInputStream = new FileInputStream(commonFile);
            BufferedInputStream commonIns = new BufferedInputStream(commonInputStream);

            mainInputStream = new FileInputStream(mainFile);
            BufferedInputStream mainIns = new BufferedInputStream(mainInputStream);
            int len = commonIns.available();
            byte[] data = new byte[len];
            commonIns.read(data);
            FileOutputStream fileOutputStream = new FileOutputStream(bundleFile,true);
            fileOutputStream.write(data);
            int mainLen = mainIns.available();
            byte[] mainData = new byte[mainLen];
            mainIns.read(mainData);
            fileOutputStream.write(mainData);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public static boolean appendBundleFiles(File inFile, File outputFile){
        try{
            FileInputStream inputStream = new FileInputStream(inFile);
            BufferedInputStream inputStreamBuffer = new BufferedInputStream(inputStream);
            int len = inputStreamBuffer.available();
            byte[] data = new byte[len];
            inputStreamBuffer.read(data);

            FileOutputStream outputStream = new FileOutputStream(outputFile,true);
            outputStream.write(data);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
