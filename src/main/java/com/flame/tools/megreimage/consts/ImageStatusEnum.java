package com.flame.tools.megreimage.consts;


/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.consts.ImageStatusEnum.java
 * @Description: 图片状态枚举
 * @Create: DerekWu  2016年4月19日 下午5:00:33 
 * @version: V1.0 
 */
public enum ImageStatusEnum {

	/** 未裁切 */
	NO_EXE(3), 
	
	/** 已经裁切成矩形   */
	EXE_RECT(1),
	
	/** 已经裁切成比矩形更多边的多边形   */
	EXE_POLYGON(2);
	
	private int code;

	private ImageStatusEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}
