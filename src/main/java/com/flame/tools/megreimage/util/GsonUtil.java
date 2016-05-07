package com.flame.tools.megreimage.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

/**
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage
 * @File: com.flame.tools.megreimage.util.GsonUtil.java
 * @Description: GsonUtil
 * @Create: DerekWu 2016年4月19日 下午5:53:55
 * @version: V1.0
 */
public class GsonUtil {

	public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public static JsonParser jsonParser = new JsonParser();

}
