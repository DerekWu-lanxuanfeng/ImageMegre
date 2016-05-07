package com.flame.tools.megreimage.consts;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.consts.RectVertexPositionEnum.java
 * @Description: 矩形顶点位置枚举
 * @Create: DerekWu  2016年4月27日 上午10:10:37 
 * @version: V1.0 
 */
public enum RectVertexPositionEnum {

	/** 左上角  */
	LEFT_TOP(0), 
	
	/** 右上角  */
	RIGHT_TOP(0), 
	
	/** 右下角  */
	RIGHT_BOTTOM(0), 
	
	/** 左下角  */
	LEFT_BOTTOM(0); 
	
	private int code;

	private RectVertexPositionEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
