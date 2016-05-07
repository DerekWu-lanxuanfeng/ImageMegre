package com.flame.tools.megreimage.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.flame.tools.megreimage.consts.ImageStatusEnum;
import com.flame.tools.megreimage.consts.RectVertexPositionEnum;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.vo.ImageInfoVO.java
 * @Description: 图片信息 
 * @Create: DerekWu  2016年4月20日 下午10:27:22 
 * @version: V1.0 
 */
public class ImageInfoVO {

	/** 图片名字  */
	private short imageName;
	
	/** 所在父图片（0表示没有父级） */
	private short parentImageName;
	
	/** 所在父级图片的顶点处  */
	private RectVertexPositionEnum parentPosition;
	
	/** 原图宽度  */
	private int width;
	
	/** 原图高度 */
	private int height;
	
	/** 状态  */
	private ImageStatusEnum status;
	
	/** 顶点数，即多边形数  */
//	private int pointNum; 
	
	/** 图片所属项目名  */
	private transient String projectName;
	
	/** 图片MD5码，如果当前md5码不对，则重写图片信息文件(*_info.json)  和图片裁切文件(*_cut.png)  */
	private String md5Code;
	
	/** 裁切矩形的4个顶点位置(为中心点(0,0)偏移的相对位置) */ 
	//private List<MyPointVO> cutPoints;
	
	/** 左上角顶点信息  */
	private MyVertexVO leftTopVertex;
	
	/** 右上角顶点信息  */
	private MyVertexVO rightTopVertex;
	
	/** 右下角顶点信息  */
	private MyVertexVO rightBottomVertex;
	
	/** 左下角顶点信息  */
	private MyVertexVO leftBottomVertex;
	
	/** 裁剪矩形的中点在原图的坐标  */
	private MyPointVO cutCenterPoint;
	
	/** 裁剪矩形的旋转角度  */
	private int cutAngel;
	
	/** 裁切后矩形的宽  */
	private int cutWidth;
	
	/** 裁切后矩形的高 */
	private int cutHeight;
	
	/** 裁切后矩形的面积  */
	private int cutArea; 
	
	/** 裁切后矩形的斜率高除以宽  */
	private float cutSlope; 
	
	/** 合图中的左上角坐标位置  */
	private MyPointVO inMagreImagePoint; 
	
	/** 裁切多边形的顶点位置，必须大于4个顶点(为中心点(0,0)偏移的相对位置) */ 
	//private List<MyPointVO> cutPolygonPoints; 
	
	/** 裁切后多边形的面积  */
	//private int cutPolygonArea;
	
	/** 使用的uv信息 */
	//private transient List<MyUVInfoVO> useUVInfos;

	public short getImageName() {
		return imageName;
	}

	public void setImageName(short imageName) {
		this.imageName = imageName;
	}

	public short getParentImageName() {
		return parentImageName;
	}

	public void setParentImageName(short parentImageName) {
		this.parentImageName = parentImageName; 
	}

	public RectVertexPositionEnum getParentPosition() {
		return parentPosition;
	}

	public void setParentPosition(RectVertexPositionEnum parentPosition) {
		this.parentPosition = parentPosition;
	}

	public ImageStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ImageStatusEnum status) {
		this.status = status;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getMd5Code() {
		return md5Code;
	}

	public void setMd5Code(String md5Code) {
		this.md5Code = md5Code;
	}

//	public List<MyPointVO> getCutPoints() {
//		return cutPoints;
//	}
//
//	public void setCutPoints(List<MyPointVO> cutPoints) {
//		this.cutPoints = cutPoints;
//	}

	public int getCutWidth() {
		return cutWidth;
	}

	public void setCutWidth(int cutWidth) {
		this.cutWidth = cutWidth;
	}

	public int getCutHeight() {
		return cutHeight;
	}

	public void setCutHeight(int cutHeight) {
		this.cutHeight = cutHeight;
	}
	
	public int getLessBorder() {
		return cutHeight>=cutWidth?cutWidth:cutHeight;
	}

	public int getCutArea() {
		return cutArea;
	}

	public void setCutArea(int cutArea) {
		this.cutArea = cutArea;
	}

//	public List<MyPointVO> getCutPolygonPoints() {
//		return cutPolygonPoints;
//	}
//
//	public void setCutPolygonPoints(List<MyPointVO> cutPolygonPoints) {
//		this.cutPolygonPoints = cutPolygonPoints;
//	}
//
//	public int getCutPolygonArea() {
//		return cutPolygonArea;
//	}
//
//	public void setCutPolygonArea(int cutPolygonArea) {
//		this.cutPolygonArea = cutPolygonArea;
//	}
//
//	public List<MyUVInfoVO> getUseUVInfos() {
//		return useUVInfos;
//	}
//
//	public void setUseUVInfos(List<MyUVInfoVO> useUVInfos) {
//		this.useUVInfos = useUVInfos;
//	}

//	public int getPointNum() {
//		return pointNum;
//	}
//
//	public void setPointNum(int pointNum) {
//		this.pointNum = pointNum;
//	}

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

	public MyPointVO getCutCenterPoint() {
		return cutCenterPoint;
	}

	public void setCutCenterPoint(MyPointVO cutCenterPoint) {
		this.cutCenterPoint = cutCenterPoint;
	}

	public int getCutAngel() {
		return cutAngel;
	}

	public void setCutAngel(int cutAngel) {
		this.cutAngel = cutAngel;
	}

	public MyVertexVO getLeftTopVertex() {
		return leftTopVertex;
	}

	public void setLeftTopVertex(MyVertexVO leftTopVertex) {
		this.leftTopVertex = leftTopVertex;
	}

	public MyVertexVO getRightTopVertex() {
		return rightTopVertex;
	}

	public void setRightTopVertex(MyVertexVO rightTopVertex) {
		this.rightTopVertex = rightTopVertex;
	}

	public MyVertexVO getRightBottomVertex() {
		return rightBottomVertex;
	}

	public void setRightBottomVertex(MyVertexVO rightBottomVertex) {
		this.rightBottomVertex = rightBottomVertex;
	}

	public MyVertexVO getLeftBottomVertex() {
		return leftBottomVertex;
	}

	public void setLeftBottomVertex(MyVertexVO leftBottomVertex) {
		this.leftBottomVertex = leftBottomVertex;
	}

	public float getCutSlope() {
		return cutSlope;
	}
	
	public void genCutSlope() {
		if (cutWidth == cutHeight) {
			cutSlope = 1;
		} else {
			cutSlope = new Float(cutHeight)/cutWidth;
		}
	}

	public MyPointVO getInMagreImagePoint() {
		return inMagreImagePoint;
	}

	public void setInMagreImagePoint(MyPointVO inMagreImagePoint) {
		this.inMagreImagePoint = inMagreImagePoint;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (ImageStatusEnum.NO_EXE.equals(this.status)) {
			builder.append("X--");
		} else if (ImageStatusEnum.EXE_RECT.equals(this.status)) {
			builder.append("R--"); 
		} else if (ImageStatusEnum.EXE_POLYGON.equals(this.status)) {
			builder.append("P--");
		}
		builder.append(this.imageName);
		if (this.cutWidth > 0) { 
			builder.append("--(");
			builder.append(this.cutWidth); 
			builder.append("*"); 
			builder.append(this.cutHeight); 
			builder.append(")--");
			builder.append(getVertexNum());
		}
		return builder.toString();
	}  
	
	/**
	 * 获得顶点数
	 * @return
	 */
	public int getVertexNum() {
		if (leftTopVertex == null) {
			return 0; 
		} else {
			return leftTopVertex.getVertexNum() + rightTopVertex.getVertexNum() + rightBottomVertex.getVertexNum() + leftBottomVertex.getVertexNum();
		}
		
	}
	
	/**
	 * 获得矩形的顶点数集合 
	 * @return
	 */
	public List<MyPointVO> getRectMyPointVOList() {
		List<MyPointVO> points = new ArrayList<MyPointVO>();
		if (leftTopVertex != null) {
			points.add(new MyPointVO(leftTopVertex.getBasePoint().getX(), leftTopVertex.getBasePoint().getY()));
			points.add(new MyPointVO(rightTopVertex.getBasePoint().getX(), rightTopVertex.getBasePoint().getY()));
			points.add(new MyPointVO(rightBottomVertex.getBasePoint().getX(), rightBottomVertex.getBasePoint().getY()));
			points.add(new MyPointVO(leftBottomVertex.getBasePoint().getX(), leftBottomVertex.getBasePoint().getY()));
		}  
		return points;
	}
	
	/**
	 * 获得多边形运算的顶点数集合 
	 * @return
	 */
	public List<MyPointVO> getPolygonMyPointVOList() {
		List<MyPointVO> points = new ArrayList<MyPointVO>();
		if (leftTopVertex != null) {
			points.addAll(leftTopVertex.getMyPointVOList());
			points.addAll(rightTopVertex.getMyPointVOList());
			points.addAll(rightBottomVertex.getMyPointVOList());
			points.addAll(leftBottomVertex.getMyPointVOList());
		} 
		return points;
	}
	
	/**
	 * 获得合图使用的顶点数集合,原始数据不能被改变 
	 * @return
	 */
	public List<MyPointVO> getMegreUseMyPointVOList() { 
		List<MyPointVO> points = new ArrayList<MyPointVO>();
		if (leftTopVertex != null) {
			points.addAll(leftTopVertex.getMegreUseMyPointVOList());
			points.addAll(rightTopVertex.getMegreUseMyPointVOList());
			points.addAll(rightBottomVertex.getMegreUseMyPointVOList());
			points.addAll(leftBottomVertex.getMegreUseMyPointVOList());
		} 
		return points;
	}
	
	/**
	 * 获得合图使用的顶点数集合，中心点转化为0,0时，原始数据不能被改变 
	 * @return
	 */
	public List<MyPointVO> getMegreUseRealMyPointVOList() {
		List<MyPointVO> points = new ArrayList<MyPointVO>();
		if (leftTopVertex != null) {
			points.addAll(leftTopVertex.getMegreUseRealMyPointVOList());
			points.addAll(rightTopVertex.getMegreUseRealMyPointVOList());
			points.addAll(rightBottomVertex.getMegreUseRealMyPointVOList());
			points.addAll(leftBottomVertex.getMegreUseRealMyPointVOList());
		} 
		return points;
	}
	
	/**
	 * 获得合图使用的顶点的uv坐标的集合，这个数据可以改变 ，没有的话要初始化
	 * @return
	 */
	public List<MyUVInfoVO> getMegreUseMyUVinfoVOList() { 
		List<MyUVInfoVO> uvInfos = new ArrayList<MyUVInfoVO>();
		if (leftTopVertex != null) {
			uvInfos.addAll(leftTopVertex.getMegreUseMyUVinfoVOList());
			uvInfos.addAll(rightTopVertex.getMegreUseMyUVinfoVOList());
			uvInfos.addAll(rightBottomVertex.getMegreUseMyUVinfoVOList());
			uvInfos.addAll(leftBottomVertex.getMegreUseMyUVinfoVOList());
		} 
		return uvInfos;
	}
	
	/**
	 * 获得多边形顶点信息，排除不是多边形顶点
	 * @return
	 */
	public List<MyVertexVO> getPolygonVertexList() {
		List<MyVertexVO> vertexs = new ArrayList<MyVertexVO>();
		if (leftTopVertex.getVertexNum() > 1) vertexs.add(leftTopVertex); 
		if (rightTopVertex.getVertexNum() > 1) vertexs.add(rightTopVertex); 
		if (rightBottomVertex.getVertexNum() > 1) vertexs.add(rightBottomVertex); 
		if (leftBottomVertex.getVertexNum() > 1) vertexs.add(leftBottomVertex); 
		return vertexs;
	}
	
	/**
	 * 获得裁切形状名字 
	 * @return
	 */
	public String getCutShapeName() {
		int vertexNum = getVertexNum();
		if (vertexNum == 0) {
			return "未处理";
		} else if (vertexNum == 4) {
			return "矩形";
		} else if (vertexNum > 4) {
			return vertexNum + "边形";
		}
		return "未知形状";
	}
	
	/**
	 * 获得状态描述
	 * @return
	 */
	public String getStatusDesc() {
		if (ImageStatusEnum.NO_EXE.equals(this.status)) {
			return "未裁切";
		} else if (ImageStatusEnum.EXE_RECT.equals(this.status)) {
			return "已经裁切成矩形 ";
		} else if (ImageStatusEnum.EXE_POLYGON.equals(this.status)) {
			return "已经裁切成比矩形更多边的多边形 ";
		}
		return "未知状态"; 
	}
	
	public List<VertexImage> getAllSubImageList() {
		List<VertexImage> allSubImageList = new ArrayList<VertexImage>();
		addSubImageToList(leftTopVertex, allSubImageList);
		addSubImageToList(rightTopVertex, allSubImageList);
		addSubImageToList(rightBottomVertex, allSubImageList);
		addSubImageToList(leftBottomVertex, allSubImageList); 
		return allSubImageList;
	}
	
	public void addSubImageToList(MyVertexVO oneVertex, List<VertexImage> subImageList) {
		if (oneVertex.getSubImages() != null) {
			for (Entry<Short, VertexImage> oneEntry:oneVertex.getSubImages().entrySet()) {
				subImageList.add(oneEntry.getValue());
			}
		}
	}
	
	public VertexImage getOneSubImage(short subImageName, RectVertexPositionEnum inParentPosition) {
		VertexImage oneVertexImage = null;
		if (RectVertexPositionEnum.LEFT_TOP.equals(inParentPosition)) {
			return leftTopVertex.getSubImage(subImageName);
		} else if (RectVertexPositionEnum.RIGHT_TOP.equals(inParentPosition)) {
			return rightTopVertex.getSubImage(subImageName);
		} else if (RectVertexPositionEnum.RIGHT_BOTTOM.equals(inParentPosition)) {
			return rightBottomVertex.getSubImage(subImageName);
		} else if (RectVertexPositionEnum.LEFT_BOTTOM.equals(inParentPosition)) {
			return leftBottomVertex.getSubImage(subImageName);
		}
		return oneVertexImage;
	}
	
}
