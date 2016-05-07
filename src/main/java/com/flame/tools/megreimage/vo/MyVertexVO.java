package com.flame.tools.megreimage.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flame.tools.megreimage.consts.RectVertexPositionEnum;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.vo.MyVertexVO.java
 * @Description: 顶点信息
 * @Create: DerekWu  2016年4月27日 上午9:57:03 
 * @version: V1.0 
 */
public class MyVertexVO {
	
	/** 所属图片 */
	private short imageName; 
	
	/** 顶点所在位置  */
	private RectVertexPositionEnum position; 
	
	/** 顶点基准坐标（基准坐标为矩形四个角之一）  ，中心点转化为0,0时*/
	private MyPointVO basePoint;
	
	/** 顶点基准坐标（基准坐标为矩形四个角之一）  */
	private MyPointVO baseCutPoint;
	
	/** 顶点uv坐标  */
	private MyUVInfoVO baseUVInfo; 
	
	/** 多边形左坐标 ，中心点转化为0,0时 */
	private MyPointVO polygonLeftPoint;
	
	/** 多边形左坐标 */
	private MyPointVO polygonCutLeftPoint;
	
	/** 多边形左坐标uv坐标  */
	private MyUVInfoVO polygonLeftUVInfo; 
	
	/** 多边形右坐标，中心点转化为0,0时  */
	private MyPointVO polygonRightPoint;
	
	/** 多边形右坐标*/
	private MyPointVO polygonCutRightPoint; 
	
	/** 多边形右坐标uv坐标  */
	private MyUVInfoVO polygonRightUVInfo; 
	
	/** 多边形计算的左右坐标和顶点坐标围起来的直角三角形中可容纳的最大矩形面积  */
	private int containMaxRectArea;
	
	/** 多边形处理后顶点处围成三角形的斜率=较短边除以较长边  */
	private float slope;
	
	/** 包含的子图片  */
	private Map<Short, VertexImage> subImages;
	
	public MyVertexVO(MyPointVO basePoint, MyPointVO baseCutPoint, short imageName, RectVertexPositionEnum position) {
		this.basePoint = basePoint; 
		this.baseCutPoint = baseCutPoint; 
		this.imageName = imageName;
		this.position = position;
	}

	public MyPointVO getBasePoint() {
		return basePoint;
	}

	public void setBasePoint(MyPointVO basePoint) {
		this.basePoint = basePoint;
	}

	public short getImageName() {
		return imageName;
	}

	public void setImageName(short imageName) {
		this.imageName = imageName;
	}

	public RectVertexPositionEnum getPosition() {
		return position;
	}

	public void setPosition(RectVertexPositionEnum position) {
		this.position = position;
	}

	public MyUVInfoVO getBaseUVInfo() {
		return baseUVInfo;
	}

	public void setBaseUVInfo(MyUVInfoVO baseUVInfo) {
		this.baseUVInfo = baseUVInfo;
	}

	public MyPointVO getPolygonLeftPoint() {
		return polygonLeftPoint;
	}

	public void setPolygonLeftPoint(MyPointVO polygonLeftPoint) {
		this.polygonLeftPoint = polygonLeftPoint;
	}

	public MyUVInfoVO getPolygonLeftUVInfo() {
		return polygonLeftUVInfo;
	}

	public void setPolygonLeftUVInfo(MyUVInfoVO polygonLeftUVInfo) {
		this.polygonLeftUVInfo = polygonLeftUVInfo;
	}

	public MyPointVO getPolygonRightPoint() {
		return polygonRightPoint;
	}

	public void setPolygonRightPoint(MyPointVO polygonRightPoint) {
		this.polygonRightPoint = polygonRightPoint;
	}

	public MyUVInfoVO getPolygonRightUVInfo() {
		return polygonRightUVInfo;
	}

	public void setPolygonRightUVInfo(MyUVInfoVO polygonRightUVInfo) {
		this.polygonRightUVInfo = polygonRightUVInfo;
	}

	public int getContainMaxRectArea() {
		return containMaxRectArea;
	}
	
	public float getSlope() {
		return slope;
	}

	public MyPointVO getBaseCutPoint() {
		return baseCutPoint;
	}

	public void setBaseCutPoint(MyPointVO baseCutPoint) {
		this.baseCutPoint = baseCutPoint;
	}

	public MyPointVO getPolygonCutLeftPoint() {
		return polygonCutLeftPoint;
	}

	public void setPolygonCutLeftPoint(MyPointVO polygonCutLeftPoint) {
		this.polygonCutLeftPoint = polygonCutLeftPoint;
	}

	public MyPointVO getPolygonCutRightPoint() {
		return polygonCutRightPoint;
	}

	public void setPolygonCutRightPoint(MyPointVO polygonCutRightPoint) {
		this.polygonCutRightPoint = polygonCutRightPoint;
	}

	/**
	 * 生成围成的直角三角形中可包含的最大矩形面积(注意间隔一像素)  和 斜率 
	 */
	public void genContainMaxRectAreaAndSlope() {
//		if (RectVertexPositionEnum.LEFT_TOP.equals(this.position)) {
//			containMaxRectArea = (Math.abs(this.polygonLeftPoint.getY()-this.basePoint.getY())-2)*(Math.abs(this.polygonRightPoint.getX()-this.basePoint.getX())-2)/4;
//			//containMaxRectArea = Math.abs((this.polygonLeftPoint.getY()-this.basePoint.getY())*(this.polygonRightPoint.getX()-this.basePoint.getX())/4);
//		} else if (RectVertexPositionEnum.RIGHT_TOP.equals(this.position)) {
//			containMaxRectArea = Math.abs((this.polygonRightPoint.getY()-this.basePoint.getY())*(this.basePoint.getX()-this.polygonLeftPoint.getX())/4);
//		} else if (RectVertexPositionEnum.LEFT_BOTTOM.equals(this.position)) {
//			containMaxRectArea = Math.abs((this.basePoint.getY()-this.polygonLeftPoint.getY())*(this.basePoint.getX()-this.polygonRightPoint.getX())/4);
//		} else if (RectVertexPositionEnum.LEFT_BOTTOM.equals(this.position)) {
//			containMaxRectArea = Math.abs((this.basePoint.getY()-this.polygonRightPoint.getY())*(this.polygonLeftPoint.getX()-this.basePoint.getX())/4);
//		}
		
		//计算斜率 
		int lengthX = Math.abs(this.polygonLeftPoint.getX() - this.polygonRightPoint.getX())+1;
		int lengthY = Math.abs(this.polygonLeftPoint.getY() - this.polygonRightPoint.getY())+1;
		if (lengthX == lengthY) {
			this.slope = 1;
		} else if (lengthX < lengthY) { 
			this.slope = new Float(lengthX)/lengthY;
		} else {
			this.slope = new Float(lengthY)/lengthX;
		}
		//计算容纳的最小矩形面积
		containMaxRectArea = (lengthX-2)*(lengthY-2)/4;
		
	}

	public Map<Short, VertexImage> getSubImages() {
		return subImages;
	}
	
	public VertexImage getSubImage(Short imageName) {
		if (this.subImages == null) {
			return null;
		} else {
			return this.subImages.get(imageName);
		}
	}

	public void setSubImages(Map<Short, VertexImage> subImages) { 
		this.subImages = subImages;
	}
	
	public void putSubImage(VertexImage vertexImage) {
		if (this.subImages == null) {
			this.subImages = new HashMap<Short, VertexImage>();
		}
		this.subImages.put(vertexImage.getImageName(), vertexImage);
	}
	
	/**
	 * 获得运算的顶点数 
	 * @return
	 */
	public int getVertexNum() {
		if (polygonLeftPoint != null) {
			return 2;
		} else {
			return 1;
		}
	}
	
	/**
	 * 获得合图使用的顶点数 
	 * @return
	 */
	public int getMagreUseVertexNum() {
		if (subImages != null) { 
			return 2;
		} else {
			return 1;
		} 
	}
	
	/**
	 * 获得运算的顶点数集合,终点坐标为0,0时，原始数据不能被改变 
	 * @return
	 */
	public List<MyPointVO> getMyPointVOList() {
		List<MyPointVO> points = new ArrayList<MyPointVO>();
		if (polygonLeftPoint != null) {
			points.add(new MyPointVO(polygonLeftPoint.getX(), polygonLeftPoint.getY()));
			points.add(new MyPointVO(polygonRightPoint.getX(), polygonRightPoint.getY()));
		} else {
			points.add(new MyPointVO(basePoint.getX(), basePoint.getY())); 
		}
		return points;
	}
	
	/**
	 * 获得合图使用的顶点数集合,原始数据不能被改变 
	 * @return
	 */
	public List<MyPointVO> getMegreUseMyPointVOList() {
		List<MyPointVO> points = new ArrayList<MyPointVO>();
		if (subImages != null) { 
			points.add(new MyPointVO(polygonCutLeftPoint.getX(), polygonCutLeftPoint.getY()));
			points.add(new MyPointVO(polygonCutRightPoint.getX(), polygonCutRightPoint.getY()));
		} else { 
			points.add(new MyPointVO(baseCutPoint.getX(), baseCutPoint.getY()));
		} 
		return points;
	}
	
	/**
	 * 获得合图使用的顶点数集合，中心点转化为0,0时，原始数据不能被改变 
	 * @return
	 */
	public List<MyPointVO> getMegreUseRealMyPointVOList() {
		List<MyPointVO> points = new ArrayList<MyPointVO>();
		if (subImages != null) { 
			points.add(new MyPointVO(polygonLeftPoint.getX(), polygonLeftPoint.getY()));
			points.add(new MyPointVO(polygonRightPoint.getX(), polygonRightPoint.getY()));
		} else { 
			points.add(new MyPointVO(basePoint.getX(), basePoint.getY()));
		} 
		return points;
	}
	
	/**
	 * 获得合图使用的顶点的uv坐标的集合，这个数据可以改变 ，没有的话要初始化
	 * @return
	 */
	public List<MyUVInfoVO> getMegreUseMyUVinfoVOList() {
		List<MyUVInfoVO> uvInfos = new ArrayList<MyUVInfoVO>();
		if (subImages != null) { 
			if (polygonLeftUVInfo == null) {
				polygonLeftUVInfo = new MyUVInfoVO(0,0);
			}
			uvInfos.add(polygonLeftUVInfo);
			if (polygonRightUVInfo == null) {
				polygonRightUVInfo = new MyUVInfoVO(0,0);
			}
			uvInfos.add(polygonRightUVInfo); 
		} else { 
			if (baseUVInfo == null) {
				baseUVInfo = new MyUVInfoVO(0,0);
			}
			uvInfos.add(baseUVInfo); 
		} 
		return uvInfos;
	}
	
	/**
	 * 获得合图使用的顶点数集合,当坐标为0,0时，原始数据不能被改变  
	 * @return 
	 */
//	public List<MyPointVO> getMegreUseMyPointVOList() {
//		List<MyPointVO> points = new ArrayList<MyPointVO>();
//		if (subImages != null) { 
//			points.add(new MyPointVO(polygonCutLeftPoint.getX(), polygonCutLeftPoint.getY()));
//			points.add(new MyPointVO(polygonCutRightPoint.getX(), polygonCutRightPoint.getY()));
//		} else { 
//			points.add(new MyPointVO(baseCutPoint.getX(), baseCutPoint.getY()));
//		} 
//		return points;
//	}
}
