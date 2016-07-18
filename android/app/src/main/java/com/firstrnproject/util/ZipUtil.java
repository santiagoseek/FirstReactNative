package com.firstrnproject.util;

import android.util.Log;

import com.firstrnproject.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by test on 16/7/15.
 */
public class ZipUtil {

	private static final String tag = Constant.TAG + "ZipUtil";

	/**
	 * not support 中文字符
	 * zip(new File("G:\\zip.zip"),"",new File("G:\\LogTest.txt"),new File("G:\\1.txt"),new File("G:\\testmain"));
	 * @param zip
	 * @param path
	 * @param srcFiles
	 */
	public static void zip(File zip, String path, File... srcFiles){
		try {
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zip));
			zipFiles(zipOutputStream,path,srcFiles);
			zipOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void zipFiles(ZipOutputStream outputStream,String path,File... srcFiles){
		if(path==null) return;
		String newPath = path.replace("\\\\+","/");
		if(newPath.length()!=0 && !newPath.endsWith("/")){
			newPath += "/";
		}
		byte[] buffer = new byte[2048];
		try{
			for(int i=0;i<srcFiles.length;i++){
				if(!srcFiles[i].exists()) continue;
				if(srcFiles[i].isDirectory()){
					File[] files = srcFiles[i].listFiles();
					String srcPath = srcFiles[i].getName();
					srcPath = srcPath.replaceAll("\\\\+","/");
					if(!srcPath.endsWith("/")){
						srcPath += "/";
					}
					outputStream.putNextEntry(new ZipEntry(newPath+srcPath));
					zipFiles(outputStream,newPath+srcPath,files);
				}else{
					FileInputStream inputStream = new FileInputStream(srcFiles[i]);
					outputStream.putNextEntry(new ZipEntry(newPath + srcFiles[i].getName()));
					int len;
					while((len = inputStream.read(buffer))>0){
						outputStream.write(buffer,0,len);
					}
					outputStream.closeEntry();
					inputStream.close();
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
			Log.e(tag,e.getMessage());
		}
	}

	/**
	 * unZip
	 * not support 中文字符
	 * unZip("G:\\1\\home.zip","G:\\1\\home\\");
	 * unZip("G:\\1\\kpi.zip","G:\\1\\kpi\\");
	 * @param zipFile
	 * @param descDir
	 */
	public static void unZip(File zipFile,String descDir){
		if(!zipFile.exists() || descDir == null) return;
		File pathFile = new File(descDir);
		if(!pathFile.exists()){
			pathFile.mkdirs();
		}
		try{
			ZipFile zip = new ZipFile(zipFile,ZipFile.OPEN_READ);
			for(Enumeration entries = zip.entries();entries.hasMoreElements();){
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream inputStream = zip.getInputStream(entry);
				Log.d(tag,"descDir+zipEntryName:" + descDir+zipEntryName);
				String outPath = (descDir+zipEntryName).replaceAll("\\\\+","/");
				File file = new File(outPath.substring(0,outPath.lastIndexOf("/")));
				if(!file.exists()){
					file.mkdirs();
				}
				if(new File(outPath).isDirectory()){
					continue;
				}
				OutputStream outputStream = new FileOutputStream(outPath);
				byte[] buffer = new byte[2048];
				int len;
				while((len = inputStream.read(buffer))>0){
					outputStream.write(buffer,0,len);
				}
				inputStream.close();
				outputStream.close();
			}
			zip.close();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(tag,e.toString());

		}
	}
}
