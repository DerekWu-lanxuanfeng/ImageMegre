package com.flame.tools.megreimage.util;

import java.util.ArrayList;
import java.util.List;

import com.flame.tools.megreimage.consts.RectVertexPositionEnum;
import com.flame.tools.megreimage.vo.FillHorizontalLineVO;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.MyPointVO;
import com.flame.tools.megreimage.vo.MyVertexVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.util.VertexFillUtil.java
 * @Description: 顶点填充工具 
 * @Create: DerekWu  2016年4月28日 下午10:35:33 
 * @version: V1.0 
 */
public class VertexFillUtil {
	
	/** 顶点对象 */
	private MyVertexVO vertexVO;
	
	/** 所在图片 */
	private ImageInfoVO imageInfoVO;

	/** 宽度   */
	private int width;
	
	/** 高度  */
	private int height;
	
	/** 第一次可填充的最大面积  */
	private int firstFillMaxArea;

	/** 斜率（高度/宽度）  */
	private float slope;
	
	/** 填充水平线集合，顺序从高到低 */
	private List<FillHorizontalLineVO> hLineList;
	
	public VertexFillUtil(MyVertexVO vertexVO, ImageInfoVO imageInfoVO) {
		this.vertexVO = vertexVO; 
		this.imageInfoVO = imageInfoVO;
		if (RectVertexPositionEnum.LEFT_TOP.equals(vertexVO.getPosition()) || RectVertexPositionEnum.RIGHT_BOTTOM.equals(vertexVO.getPosition())) {
			width = MyPointVO.getDistance(vertexVO.getBasePoint(), vertexVO.getPolygonRightPoint()); 
			height = MyPointVO.getDistance(vertexVO.getBasePoint(), vertexVO.getPolygonLeftPoint()); 
		} else {
			width = MyPointVO.getDistance(vertexVO.getBasePoint(), vertexVO.getPolygonLeftPoint()); 
			height = MyPointVO.getDistance(vertexVO.getBasePoint(), vertexVO.getPolygonRightPoint());
		}
		//width = Math.abs(vertexVO.getPolygonLeftPoint().getX() - vertexVO.getPolygonRightPoint().getX()+1);
		//height = Math.abs(vertexVO.getPolygonLeftPoint().getY() - vertexVO.getPolygonRightPoint().getY()+1);
		firstFillMaxArea = vertexVO.getContainMaxRectArea();
		if (height == width) {
			this.slope = 1;
		} else { 
			this.slope = new Float(height)/width;
		}
		FillHorizontalLineVO defaultFillHorizontalLineVO = new FillHorizontalLineVO(new MyPointVO(0, 0), new MyPointVO(width, 0));
		hLineList = new ArrayList<FillHorizontalLineVO>();
		hLineList.add(defaultFillHorizontalLineVO);
		
	}

	public MyVertexVO getVertexVO() {
		return vertexVO;
	}

	public ImageInfoVO getImageInfoVO() {
		return imageInfoVO;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getSlope() { 
		return slope;
	}

	public int getFirstFillMaxArea() {
		return firstFillMaxArea;
	}

	public List<FillHorizontalLineVO> gethLineList() {
		return hLineList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VertexFillUtil [width=");
		builder.append(width);
		builder.append(", height=");
		builder.append(height);
		builder.append(", image=");
		builder.append(this.imageInfoVO.getImageName()); 
		builder.append("]");
		return builder.toString();
	}
	
}
