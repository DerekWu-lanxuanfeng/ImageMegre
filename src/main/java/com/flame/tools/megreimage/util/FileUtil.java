package com.flame.tools.megreimage.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage
 * @File: com.flame.tools.megreimage.util.FileUtil.java
 * @Description: 文件工具类
 * @Create: DerekWu  2015年12月31日 下午5:40:35 
 * @version: V1.0 
 */
public class FileUtil {

	/**
	 * 获取文件的输入流
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getFileInputStream(String path) throws Exception {
		String dir = path; 
		File file = new File(dir);
		InputStream in = null;
		if (file.exists()) {
			in = new FileInputStream(file);
		}
		if (in == null) {
			throw new FileNotFoundException();
		}
		return in;
	}
	
	/**
	 * 获取文件内容 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getFileText(String path) throws Exception { 
		InputStream in = getFileInputStream(path);
		StringBuffer out = new StringBuffer();
		try {
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return out.toString();
	}
	
	/**
	 * 写入文本 
	 * @param filePath
	 * @param fileText
	 */
	public static void writeText(String filePath, String fileText) { 
		File fukeName = new File(filePath);  
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fukeName); 
			byte[] bytes = fileText.getBytes("UTF-8");  
			int length = bytes.length;
			fos.write(bytes, 0, length);  
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			try {
				if (fos!=null) 
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获得文件的md5码
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String getFileMD5(File file) throws Exception {
		String value = null;
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16).toUpperCase(); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) { 
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;

	}
	
}
