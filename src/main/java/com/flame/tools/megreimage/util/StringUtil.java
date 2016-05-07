package com.flame.tools.megreimage.util;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.util.StringUtil.java
 * @Description: String 工具类 
 * @Create: DerekWu  2016年4月21日 下午5:33:40 
 * @version: V1.0 
 */
public class StringUtil {

	public static String connectStr(Object...strings) {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<strings.length; ++i) {
			builder.append(strings[i]);
		} 
		return builder.toString();
	}

}
