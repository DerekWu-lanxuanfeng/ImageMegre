package com.flame.tools.megreimage.consts;


/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.consts.ProjectStatusEnum.java
 * @Description: 项目状态枚举
 * @Create: DerekWu  2016年4月19日 下午5:00:33 
 * @version: V1.0 
 */
public enum ProjectStatusEnum {

	/** 还有图片未进行矩形裁剪，请点击“批量矩形裁切”  */
	NOT_CUT(0),
	
	/** 已矩形裁剪，请点击“批量多边形运算”   */
	CUT_RECT(1),
	
	/** 已进行多边形运算 ，可以合图  */
	POLYGON_EXE(1);
	
	private int code;

	private ProjectStatusEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}
