package com.flame.tools.megreimage.vo;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.vo.MyUVInfoVO.java
 * @Description: uc信息vo，注意uv坐标用（S,T）标识（两个值范围在0-1之间），对应图片左边（X，Y），但是注意，T轴对应Y轴主要反转
 * @Create: DerekWu  2016年4月21日 下午2:48:34 
 * @version: V1.0 
 */
public class MyUVInfoVO {

	private float s;
	
	private float t;

	/**
	 * @param s
	 * @param t
	 */
	public MyUVInfoVO(float s, float t) { 
		this.s = s;
		this.t = t;
	}

	public float getS() {
		return s;
	}

	public void setS(float s) {
		this.s = s;
	}

	public float getT() {
		return t;
	}

	public void setT(float t) {
		this.t = t;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MyUVInfoVO [s=");
		builder.append(s);
		builder.append(", t=");
		builder.append(t);
		builder.append("]");
		return builder.toString();
	}
	
}
