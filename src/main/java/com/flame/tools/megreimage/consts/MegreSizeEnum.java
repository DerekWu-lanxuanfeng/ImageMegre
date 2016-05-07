package com.flame.tools.megreimage.consts;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.consts.MegreSizeEnum.java
 * @Description: 合图尺寸枚举
 * @Create: DerekWu  2016年4月20日 下午5:56:07 
 * @version: V1.0 
 */
public enum MegreSizeEnum {

	/** 256*256 */
	S_256(0),
	
	/** 512*512 */
	S_512(0),
	
	/** 1024*1024 */
	S_1024(0),
	
	/** 2048*2048 */
	S_2048(0),
	
	/** 4096*4096 */
	S_4096(0);
	
	private int code;

	private MegreSizeEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static String getStr(MegreSizeEnum megreSize) {
		if (MegreSizeEnum.S_256.equals(megreSize)) {
			return "256*256";
		} else if (MegreSizeEnum.S_512.equals(megreSize)) {
			return "512*512";
		} else if (MegreSizeEnum.S_1024.equals(megreSize)) {
			return "1024*1024";
		} else if (MegreSizeEnum.S_2048.equals(megreSize)) {
			return "2048*2048";
		} else if (MegreSizeEnum.S_4096.equals(megreSize)) {
			return "4096*4096";
		}
		return "";
	}
}
