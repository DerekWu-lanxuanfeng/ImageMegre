package com.flame.tools.megreimage.vo;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MagreImage 
 * @File: com.flame.tools.megreimage.PointVO.java
 * @Description: 点对象 
 * @Create: DerekWu  2016年4月18日 下午12:00:40 
 * @version: V1.0 
 */
public class MyPointVO {

	private int x;
	
	private int y;

	/**
	 * @param x
	 * @param y
	 */
	public MyPointVO(int x, int y) {
		super();
		this.x = x;
		this.y = y;
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
	
	public void addPoint(MyPointVO point) {
		this.x += point.getX();
		this.y += point.getY();
	}
	
	public void reducePoint(MyPointVO point) {
		this.x -= point.getX();
		this.y -= point.getY();
	}
	
	public static MyPointVO addPoint(MyPointVO point1, MyPointVO point2) {
		return new MyPointVO(point1.getX() + point2.getX(), point1.getY() + point2.getY());
	}
	
	public static MyPointVO reducePoint(MyPointVO basePoint, MyPointVO reducePoint) { 
		return new MyPointVO(basePoint.getX() - reducePoint.getX(), basePoint.getY() - reducePoint.getY());
	}
	
	public static int getDistance(MyPointVO point1, MyPointVO point2) {  
		int xDistance = Math.abs(point1.getX() - point2.getX())+1; 
		int yDistance = Math.abs(point1.getY() - point2.getY())+1;  
		return (int)Math.sqrt(xDistance*xDistance + yDistance*yDistance);
	}
	
	@Override
	public String toString() {  
		StringBuilder sb = new StringBuilder("Point[");
		sb.append(this.x);
		sb.append(",");
		sb.append(this.y);
		sb.append("]");
	    return sb.toString();
	}  

}
