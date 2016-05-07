package com.flame.tools.megreimage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage
 * @File: com.flame.tools.megreimage.AppConfig.java
 * @Description: 配置 
 * @Create: DerekWu 2016年5月4日 下午8:34:59
 * @version: V1.0
 */
public class AppConfig {

	private static Properties prop = new Properties();

	static {
		try {
			URL configPath = AppConfig.class.getClassLoader().getResource("config.properties"); 
			prop.load(new FileInputStream(new File(configPath.getPath())));  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return prop.getProperty(key);
	}

	public static void setProperty(String key, String value) {
		prop.setProperty(key, value);
	}

}
