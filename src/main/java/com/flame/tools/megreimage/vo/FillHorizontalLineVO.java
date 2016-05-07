package com.flame.tools.megreimage.vo;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.vo.FillHorizontalLineVO.java
 * @Description: 填充水平线VO
 * @Create: DerekWu  2016年4月28日 下午10:42:50 
 * @version: V1.0 
 */
public class FillHorizontalLineVO {

	/** 水平线起点  */
	private MyPointVO startPoint;
	
	/** 水平线终点  */
	private MyPointVO endPoint;

	public FillHorizontalLineVO(MyPointVO startPoint, MyPointVO endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public MyPointVO getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(MyPointVO startPoint) {
		this.startPoint = startPoint;
	}

	public MyPointVO getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(MyPointVO endPoint) {
		this.endPoint = endPoint;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FillHorizontalLineVO [startPoint=");
		builder.append(startPoint);
		builder.append(", endPoint=");
		builder.append(endPoint);
		builder.append("]");
		return builder.toString();
	}
	
}
