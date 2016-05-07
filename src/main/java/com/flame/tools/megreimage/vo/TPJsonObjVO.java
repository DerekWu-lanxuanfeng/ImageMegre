package com.flame.tools.megreimage.vo;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.vo.TPJsonObjVO.java
 * @Description: testurePacker json 单个对象 
 * "31.png":
	{
		"frame": {"x":273,"y":182,"w":95,"h":251},
		"rotated": true,
		"trimmed": false,
		"spriteSourceSize": {"x":0,"y":0,"w":95,"h":251},
		"sourceSize": {"w":95,"h":251},
		"pivot": {"x":0.5,"y":0.5}
	}
 * @Create: DerekWu  2016年5月6日 上午10:19:54 
 * @version: V1.0 
 */
public class TPJsonObjVO { 
	
	private short imageName;
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	/** 是否旋转  */
	private boolean rotated;

	/**
	 * @param imageName
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param rotated
	 */
	public TPJsonObjVO(short imageName, int x, int y, int width, int height,
			boolean rotated) {
		super();
		this.imageName = imageName;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotated = rotated;
	}

	public short getImageName() {
		return imageName;
	}

	public void setImageName(short imageName) {
		this.imageName = imageName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isRotated() {
		return rotated;
	}

	public void setRotated(boolean rotated) {
		this.rotated = rotated;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TPJsonObjVO [imageName=");
		builder.append(imageName);
		builder.append(", x=");
		builder.append(x);
		builder.append(", y=");
		builder.append(y);
		builder.append(", width=");
		builder.append(width);
		builder.append(", height=");
		builder.append(height);
		builder.append(", rotated=");
		builder.append(rotated);
		builder.append("]");
		return builder.toString();
	}
	
}
