package com.flame.tools.megreimage.vo;



/**
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MagreImage
 * @File: com.flame.tools.megreimage.MyRect.java
 * @Description: 自定义矩形对象
 * @Create: DerekWu 2016年4月18日 下午12:50:53
 * @version: V1.0
 */
public class MyRectVO {

	/** 原图中心点在本矩形内的位置 */
	private MyPointVO baseCenterPoint;

	private MyPointVO leftMinPoint;

	private MyPointVO rightMinPoint;

	private MyPointVO topMinPoint;

	private MyPointVO bottomMinPoint;

	private MyPointVO leftTopPoint;

	private int width;

	private int height;
	
	/** 面积 */
	private int area;
	
	/** 角度  */
	private int angel;

	/**
	 * @param centerPoint
	 * @param leftMinPoint
	 * @param rightMinPoint
	 * @param topMinPoint
	 * @param bottomMinPoint
	 */
	public MyRectVO(MyPointVO baseCenterPoint, MyPointVO leftMinPoint,
			MyPointVO rightMinPoint, MyPointVO topMinPoint,
			MyPointVO bottomMinPoint) {
		this.baseCenterPoint = baseCenterPoint;
		this.leftMinPoint = leftMinPoint;
		this.rightMinPoint = rightMinPoint;
		this.topMinPoint = topMinPoint;
		this.bottomMinPoint = bottomMinPoint;
		this.width = rightMinPoint.getX() - leftMinPoint.getX()+1; 
		this.height = bottomMinPoint.getY() - topMinPoint.getY()+1; 
		this.leftTopPoint = new MyPointVO(leftMinPoint.getX(), topMinPoint.getY());
		this.area = this.width * this.height;
	}

	public MyPointVO getLeftMinPoint() {
		return leftMinPoint;
	}

	public void setLeftMinPoint(MyPointVO leftMinPoint) {
		this.leftMinPoint = leftMinPoint;
	}

	public MyPointVO getRightMinPoint() {
		return rightMinPoint;
	}

	public void setRightMinPoint(MyPointVO rightMinPoint) {
		this.rightMinPoint = rightMinPoint;
	}

	public MyPointVO getTopMinPoint() {
		return topMinPoint;
	}

	public void setTopMinPoint(MyPointVO topMinPoint) {
		this.topMinPoint = topMinPoint;
	}

	public MyPointVO getBottomMinPoint() {
		return bottomMinPoint;
	}

	public void setBottomMinPoint(MyPointVO bottomMinPoint) {
		this.bottomMinPoint = bottomMinPoint;
	}

	public MyPointVO getLeftTopPoint() {
		return leftTopPoint;
	}

	public void setLeftTopPoint(MyPointVO leftTopPoint) {
		this.leftTopPoint = leftTopPoint;
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

	public MyPointVO getBaseCenterPoint() {
		return baseCenterPoint;
	}

	public void setBaseCenterPoint(MyPointVO baseCenterPoint) {
		this.baseCenterPoint = baseCenterPoint;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}
	
	public int getAngel() {
		return angel;
	}

	public void setAngel(int angel) {
		this.angel = angel;
	}
	
//	public MyRectVO genCutPolygonRect() { 
//		MyPointVO newBaseCenterPoint = new MyPointVO(baseCenterPoint.getX(), baseCenterPoint.getY());
//		MyPointVO newLeftMinPoint = new MyPointVO(0, this.leftMinPoint.getY() - this.leftTopPoint.getY()-1);
//		MyPointVO newRightMinPoint = new MyPointVO(this.rightMinPoint.getX() - this.leftTopPoint.getX()-1, this.rightMinPoint.getY() - this.leftTopPoint.getY()-1);
//		MyPointVO newTopMinPoint = new MyPointVO(this.topMinPoint.getX() - this.leftTopPoint.getX()-1, 0);
//		MyPointVO newBottomMinPoint = new MyPointVO(this.bottomMinPoint.getX() - this.leftTopPoint.getX()-1, this.bottomMinPoint.getY() - this.leftTopPoint.getY()-1);
//		return new MyRectVO(newBaseCenterPoint, newLeftMinPoint, newRightMinPoint, newTopMinPoint, newBottomMinPoint);
//	}
	
	public MyRectVO genCutPolygonRect() { 
		MyPointVO newBaseCenterPoint = new MyPointVO(baseCenterPoint.getX(), baseCenterPoint.getY());
		MyPointVO newLeftMinPoint = new MyPointVO(0, this.leftMinPoint.getY() - this.leftTopPoint.getY());
		MyPointVO newRightMinPoint = new MyPointVO(this.rightMinPoint.getX() - this.leftTopPoint.getX(), this.rightMinPoint.getY() - this.leftTopPoint.getY());
		MyPointVO newTopMinPoint = new MyPointVO(this.topMinPoint.getX() - this.leftTopPoint.getX(), 0);
		MyPointVO newBottomMinPoint = new MyPointVO(this.bottomMinPoint.getX() - this.leftTopPoint.getX(), this.bottomMinPoint.getY() - this.leftTopPoint.getY());
		return new MyRectVO(newBaseCenterPoint, newLeftMinPoint, newRightMinPoint, newTopMinPoint, newBottomMinPoint);
	} 
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MyRectVO [baseCenterPoint=");
		builder.append(baseCenterPoint);
		builder.append(", leftTopPoint=");
		builder.append(leftTopPoint);
		builder.append(", width=");
		builder.append(width);
		builder.append(", height=");
		builder.append(height);
		builder.append(", area=");
		builder.append(area);
		builder.append(", angel=");
		builder.append(angel);
		builder.append("]");
		return builder.toString();
	}

}
