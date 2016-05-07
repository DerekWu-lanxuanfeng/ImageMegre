package com.flame.tools.megreimage.vo;

import com.flame.tools.megreimage.consts.RectVertexPositionEnum;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.vo.VertexImages.java
 * @Description: 顶点出裁切三角形中的组合图片  
 * @Create: DerekWu  2016年4月27日 上午10:14:26 
 * @version: V1.0 
 */
public class VertexImage {

	/** 图片名称  */
	private short imageName;
	
	/** 顶点所在位置  */
	private RectVertexPositionEnum position;
	
	/** 是否旋转（顺时针旋转90度）  */ 
	private boolean isRotare;
	
	/** 左上角顶点 所在父图片的坐标 ，如果旋转了，就变成了左下角坐标位置  */
	private MyPointVO inParentPoint;
	
	public VertexImage(short imageName, RectVertexPositionEnum position, boolean isRotare, MyPointVO inParentPoint) { 
		this.imageName = imageName;
		this.position = position;
		this.isRotare = isRotare;
		this.inParentPoint = inParentPoint;
	} 

	public short getImageName() {
		return imageName;
	}

	public RectVertexPositionEnum getPosition() {
		return position;
	}

	public boolean isRotare() {
		return isRotare;
	}

	public MyPointVO getInParentPoint() {
		return inParentPoint;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VertexImage [imageName=");
		builder.append(imageName);
		builder.append(", position=");
		builder.append(position);
		builder.append(", isRotare=");
		builder.append(isRotare);
		builder.append(", inParentPoint=");
		builder.append(inParentPoint);
		builder.append("]");
		return builder.toString();
	} 

}
