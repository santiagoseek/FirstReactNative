package com.firstrnproject.file;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by longge on 16/7/7.
 */
public class FileUtils {
    private String SDPATH;

    public String getSDPATH() {
        return SDPATH;
    }

    public FileUtils() {
        //得到当前外部存储设备的目录
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }
    public File creatSDFile(String fileName) throws IOException {

        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    public File creatSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    public boolean isFileExist(String fileName){

        File file = new File(SDPATH + fileName);
        file.delete();
        return file.exists();

    }

    public File writeToSDFromInput(String path,String fileName,InputStream input){

        File file =null;
        OutputStream output =null;
        try{
            creatSDDir(path);
            file = creatSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer [] = new byte[1024];
            int len  = 0;
            //如果下载成功就开往SD卡里些数据
            while((len =input.read(buffer))  != -1){
                output.write(buffer,0,len);
            }
            output.flush();
        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                input.close();
                output.close();
            }catch(Exception e){

                e.printStackTrace();
            }
        }
        return file;
    }
}
