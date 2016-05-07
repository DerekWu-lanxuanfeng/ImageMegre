package com.flame.tools.megreimage.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.flame.tools.megreimage.consts.ImageStatusEnum;
import com.flame.tools.megreimage.consts.RectVertexPositionEnum;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.MyPointVO;
import com.flame.tools.megreimage.vo.MyRectVO;
import com.flame.tools.megreimage.vo.MyVertexVO;
import com.flame.tools.megreimage.vo.ProjectInfoVO;

/**
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MagreImage
 * @File: com.flame.tools.megreimage.ImageUtil.java
 * @Description: 圖片处理工具
 * @Create: DerekWu 2016年4月18日 下午1:42:02
 * @version: V1.0
 */
public class ImageUtil {

	
	/**
	 * 裁切没有透明区域的最小矩形并写入磁盘 
	 * @param imageInfoVO
	 * @param imagePath
	 * @param writePath
	 * @return 
	 */
	public static boolean cutLessNoAlphaRectAndWriteToDisk(ImageInfoVO imageInfoVO, String imagePath, String writePath) {
		try {
			//System.out.println(imageInfoVO); 
			File imageFile = new File(imagePath);
			BufferedImage src = ImageIO.read(imageFile); 
			
//			if (src.getWidth() >=500 || src.getHeight() >= 500) {
//				imageFile.delete();
//				return false;
//			}
			
			//第一次矩形裁剪
			MyRectVO firstCutRect = cutNoAlpha(src, null, 0);
			//System.out.println(firstCutRect);
			BufferedImage firstCutImage = src.getSubimage(firstCutRect.getLeftTopPoint().getX(), firstCutRect.getLeftTopPoint().getY(), firstCutRect.getWidth(), firstCutRect.getHeight());
			
			//设置最小面积矩形
			MyRectVO lessAreaRect = firstCutRect;
			
			//假如第一次裁切后宽高中有小于10像素的，不进行旋转，否则进行旋转 
			if (firstCutImage.getWidth() > 20 || firstCutImage.getHeight() > 20) { 
				//旋转各个角度进行计算面积，找出面积最小的矩形,每5度计算一次
				for (int angel=5; angel<=90; angel+=5) { 
					BufferedImage newRotateImage = rotate(firstCutImage, angel);  
					MyRectVO newCutRect = cutNoAlpha(newRotateImage, null, angel);
					if (newCutRect.getArea() < lessAreaRect.getArea()) {
						//替换最小面积
						lessAreaRect = newCutRect;
					}
				}
			}
			//面积最小的裁剪矩形 
			BufferedImage cutLessImage = null;
			if (lessAreaRect.getAngel() == 0) {
				cutLessImage =  firstCutImage;
			} else { 
				BufferedImage newRotateImage = rotate(firstCutImage, lessAreaRect.getAngel());				
				MyPointVO firstCenterPoint = firstCutRect.getBaseCenterPoint(); 
				MyPointVO lessCenterPoint = getLessCenterPoint(firstCenterPoint, firstCutImage, newRotateImage, lessAreaRect.getAngel());
				lessCenterPoint.setX(lessCenterPoint.getX() - lessAreaRect.getLeftTopPoint().getX()); 
				lessCenterPoint.setY(lessCenterPoint.getY() - lessAreaRect.getLeftTopPoint().getY());  
				lessAreaRect.setBaseCenterPoint(lessCenterPoint); 
				cutLessImage =  newRotateImage.getSubimage(lessAreaRect.getLeftTopPoint().getX(), lessAreaRect.getLeftTopPoint().getY(), lessAreaRect.getWidth(), lessAreaRect.getHeight());
			} 
			
			//写入磁盘 
			ImageIO.write(cutLessImage, "png", new File(writePath));  
			
			//原始重点 
			int srcCenterX = src.getWidth()/2; 
			int srcCenterY = src.getHeight()/2; 
			
			MyPointVO srcCenterPoint = new MyPointVO(srcCenterX, srcCenterY);

			//左上角
			int leftTopX = srcCenterX - lessAreaRect.getBaseCenterPoint().getX();
			int leftTopY = srcCenterY - lessAreaRect.getBaseCenterPoint().getY();
			//右上角
			int rightTopX = leftTopX + lessAreaRect.getWidth()-1;
			//int rightTopX = srcCenterX + lessAreaRect.getWidth()-lessAreaRect.getBaseCenterPoint().getX();
			int rightTopY = leftTopY; 
			//左上角
			int leftBottomX = leftTopX;
			//int leftBottomY = srcCenterY + lessAreaRect.getHeight()-lessAreaRect.getBaseCenterPoint().getY();
			int leftBottomY = leftTopY + lessAreaRect.getHeight()-1;
			//左上角
			int rightBottomX = rightTopX; 
			int rightBottomY = leftBottomY; 
			
			//得到4个顶点位置 
			MyPointVO leftTopMyPointVO = AlgorithmUtil.getRotatePoint(leftTopX, leftTopY, srcCenterX, srcCenterY, -(lessAreaRect.getAngel()));
			MyPointVO rightTopMyPointVO = AlgorithmUtil.getRotatePoint(rightTopX, rightTopY, srcCenterX, srcCenterY, -(lessAreaRect.getAngel()));
			MyPointVO rightBottomMyPointVO = AlgorithmUtil.getRotatePoint(rightBottomX, rightBottomY, srcCenterX, srcCenterY, -(lessAreaRect.getAngel()));
			MyPointVO leftBottomMyPointVO = AlgorithmUtil.getRotatePoint(leftBottomX, leftBottomY, srcCenterX, srcCenterY, -(lessAreaRect.getAngel()));
			
			//转换为对应 中心坐标（0,0）的相对坐标
			leftTopMyPointVO.reducePoint(srcCenterPoint);
			rightTopMyPointVO.reducePoint(srcCenterPoint);
			rightBottomMyPointVO.reducePoint(srcCenterPoint);
			leftBottomMyPointVO.reducePoint(srcCenterPoint);
			
			//顶点集合 
//			List<MyPointVO> cutPoints = new ArrayList<MyPointVO>();
//			cutPoints.add(leftTopMyPointVO);
//			cutPoints.add(rightTopMyPointVO);
//			cutPoints.add(rightBottomMyPointVO);
//			cutPoints.add(leftBottomMyPointVO);
			
			MyPointVO leftTopCutPoint = new MyPointVO(0,0); 
			MyPointVO rightTopCutPoint = new MyPointVO(lessAreaRect.getWidth()-1, 0); 
			MyPointVO rightBottomCutPoint = new MyPointVO(lessAreaRect.getWidth()-1, lessAreaRect.getHeight()-1); 
			MyPointVO leftBottomCutPoint = new MyPointVO(0, lessAreaRect.getHeight()-1); 
			
			//顶点信息设置 ，中心坐标变为0,0时，和裁切图片顶点信息 
			imageInfoVO.setLeftTopVertex(new MyVertexVO(leftTopMyPointVO, leftTopCutPoint, imageInfoVO.getImageName(), RectVertexPositionEnum.LEFT_TOP)); 
			imageInfoVO.setRightTopVertex(new MyVertexVO(rightTopMyPointVO, rightTopCutPoint, imageInfoVO.getImageName(), RectVertexPositionEnum.RIGHT_TOP)); 
			imageInfoVO.setRightBottomVertex(new MyVertexVO(rightBottomMyPointVO, rightBottomCutPoint, imageInfoVO.getImageName(), RectVertexPositionEnum.RIGHT_BOTTOM)); 
			imageInfoVO.setLeftBottomVertex(new MyVertexVO(leftBottomMyPointVO, leftBottomCutPoint, imageInfoVO.getImageName(), RectVertexPositionEnum.LEFT_BOTTOM)); 
			
			//设置imageInfoVO
			imageInfoVO.setWidth(src.getWidth());
			imageInfoVO.setHeight(src.getHeight()); 
			imageInfoVO.setStatus(ImageStatusEnum.EXE_RECT); 
			//imageInfoVO.setPointNum(4); //4边形 
			//imageInfoVO.setCutPoints(cutPoints); //顶点集合 ,改变了存储方式 
			imageInfoVO.setCutCenterPoint(lessAreaRect.getBaseCenterPoint());  //中点位置   
			imageInfoVO.setCutAngel(lessAreaRect.getAngel()); //设置角度  
			imageInfoVO.setCutWidth(lessAreaRect.getWidth()); //宽
			imageInfoVO.setCutHeight(lessAreaRect.getHeight()); //高  
			imageInfoVO.setCutArea(lessAreaRect.getArea()); //面积
			imageInfoVO.genCutSlope(); //计算斜率 
			
			return true;
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 多边形运算 针对 裁剪最小矩形图片
	 * @param imageInfoVO
	 * @param imagePath
	 * @param projectInfoVO
	 * @return 0=成功进行多边形运算  1=失败  2=没运算成多边形 
	 */
	public static int exePolygonByCutLessRectImage(ImageInfoVO imageInfoVO, String cutLessImagePath, ProjectInfoVO projectInfoVO) {
		try {
			File cutLessImageFile = new File(cutLessImagePath);
			if (!cutLessImageFile.exists()) { 
				return 1; //运算失败 
			}
			BufferedImage cutLessImage = ImageIO.read(cutLessImageFile); 
			//绘制多边形框
			List<MyPointVO> polygonPointArray = exePolygon(cutLessImage, null, projectInfoVO, imageInfoVO);
			if (polygonPointArray.size() > 4) { //大于4个顶点才处理 
				
				//原图中点坐标
				int srcCenterX = imageInfoVO.getWidth()/2;
				int srcCenterY = imageInfoVO.getHeight()/2;
				
				MyPointVO srcCenterPoint = new MyPointVO(srcCenterX, srcCenterY);
				
				//先进行偏移
				int offsetBySrcCenterX = srcCenterX - imageInfoVO.getCutCenterPoint().getX();
				int offsetBySrcCenterY = srcCenterY - imageInfoVO.getCutCenterPoint().getY(); 
				if (offsetBySrcCenterX !=0 || offsetBySrcCenterY !=0 ) { 
					for (MyPointVO oneMyPointVO:polygonPointArray) { 
						oneMyPointVO.setX(oneMyPointVO.getX() + offsetBySrcCenterX);
						oneMyPointVO.setY(oneMyPointVO.getY() + offsetBySrcCenterY);
					}
				}
				//再计算旋转坐标
				if (imageInfoVO.getCutAngel() != 0) {
					for (MyPointVO oneMyPointVO:polygonPointArray) { 
						MyPointVO newRatorePoint = AlgorithmUtil.getRotatePoint(oneMyPointVO.getX(), oneMyPointVO.getY(), srcCenterX, srcCenterY, -imageInfoVO.getCutAngel());
						oneMyPointVO.setX(newRatorePoint.getX());
						oneMyPointVO.setY(newRatorePoint.getY());
					}
				} 
				
				//转换为对应 中心坐标（0,0）的相对坐标
				for (MyPointVO oneMyPointVO:polygonPointArray) {  
					oneMyPointVO.reducePoint(srcCenterPoint);
				}
				imageInfoVO.setStatus(ImageStatusEnum.EXE_POLYGON); 
				//imageInfoVO.setPointNum(polygonPointArray.size()); //多边形顶点数  
				//imageInfoVO.setCutPolygonPoints(polygonPointArray); //多边形顶点集合
				return 0; //成功运算成多边形 
			} else {
				return 2;//没有运算成多边形 
			}
			
		} catch (Exception e) {  
			e.printStackTrace();
			return 1; //运算失败 
		}
	}
	
	
	public static void main(String[] args) {  
		ImageUtil.exeImageTest("e://test//dd.png");
//		System.out.println(getRotatePoint(10,25,10,20,90)); 
	}
	
	/**
	 * 测试用 
	 * @param srcPath
	 */
	private static void exeImageTest(String srcPath) {
		try {
			
			BufferedImage src = ImageIO.read(new File(srcPath)); 
			
			MyRectVO firstCutRect = cutNoAlpha(src, null, 0);
			
			BufferedImage firstCutImage = src.getSubimage(firstCutRect.getLeftTopPoint().getX(), firstCutRect.getLeftTopPoint().getY(), firstCutRect.getWidth(), firstCutRect.getHeight());
			
//			Graphics2D g2 = firstCutImage.createGraphics(); 
//			g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//			g2.setColor(Color.GREEN); 
//			g2.fillRect(firstCutRect.getBaseCenterPoint().getX(), firstCutRect.getBaseCenterPoint().getY(), 1, 1); 
//			g2.fillRect(firstCutImage.getWidth()/2, firstCutImage.getHeight()/2, 1, 1); 
		     
			ImageIO.write(firstCutImage, "png", new File("e://test//dd_first.png")); 
			
			//设置最小面积矩形
			MyRectVO lessAreaRect = firstCutRect;
			//旋转各个角度进行计算面积，找出面积最小的矩形,每5度计算一次
			for (int angel=5; angel<=90; angel+=5) {
				BufferedImage newRotateImage = rotate(firstCutImage, angel);  
				MyRectVO newCutRect = cutNoAlpha(newRotateImage, null, angel);
				if (newCutRect.getArea() < lessAreaRect.getArea()) {
					//替换最小面积
					lessAreaRect = newCutRect;
				}
			}
			
			//面积最小的裁剪矩形 
			BufferedImage cutLessImage = null;
			if (lessAreaRect.getAngel() == 0) {
				cutLessImage =  firstCutImage;
			} else { 
				//面积最小的裁剪矩形 的旋转矩形 
				BufferedImage cutLessRotateImage = rotate(firstCutImage, lessAreaRect.getAngel());
//				Graphics2D g3 = newRotateImage.createGraphics();  
//				g3.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//				g3.setColor(Color.YELLOW);  
//				g3.fillRect(lessAreaRect.getBaseCenterPoint().getX()-1, lessAreaRect.getBaseCenterPoint().getY()-1, 1, 1); 
//				g3.fillRect(newRotateImage.getWidth()/2, newRotateImage.getHeight()/2, 1, 1); 
//				ImageIO.write(newRotateImage, "png", new File("e://test//dd_lesscat.png"));  
				
				//重新计算中点 
				MyPointVO firstCenterPoint = firstCutRect.getBaseCenterPoint(); 
				MyPointVO lessCenterPoint = getLessCenterPoint(firstCenterPoint, firstCutImage, cutLessRotateImage, lessAreaRect.getAngel());
				lessCenterPoint.setX(lessCenterPoint.getX() - lessAreaRect.getLeftTopPoint().getX()); 
				lessCenterPoint.setY(lessCenterPoint.getY() - lessAreaRect.getLeftTopPoint().getY());  
				lessAreaRect.setBaseCenterPoint(lessCenterPoint); 
				//裁剪最小矩形 
				cutLessImage =  cutLessRotateImage.getSubimage(lessAreaRect.getLeftTopPoint().getX(), lessAreaRect.getLeftTopPoint().getY(), lessAreaRect.getWidth(), lessAreaRect.getHeight());
			} 
			
//			Graphics2D g3 = cutLessImage.createGraphics();  
//			g3.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//			g3.setColor(Color.YELLOW);  
//			g3.fillRect(lessAreaRect.getBaseCenterPoint().getX()-1, lessAreaRect.getBaseCenterPoint().getY()-1, 3, 3); 
//			g3.fillRect(cutLessImage.getWidth()/2-1, cutLessImage.getHeight()/2-1, 3, 3); 
			
			ImageIO.write(cutLessImage, "png", new File("e://test//dd1.png"));  
			
			//绘制效果
			int srcCenterX = src.getWidth()/2; 
			int srcCenterY = src.getHeight()/2; 

			//左上角
			int leftTopX = srcCenterX - lessAreaRect.getBaseCenterPoint().getX();
			int leftTopY = srcCenterY - lessAreaRect.getBaseCenterPoint().getY();
			//右上角
			int rightTopX = leftTopX + lessAreaRect.getWidth()-1;
			//int rightTopX = srcCenterX + lessAreaRect.getWidth()-lessAreaRect.getBaseCenterPoint().getX();
			int rightTopY = leftTopY; 
			//左上角
			int leftBottomX = leftTopX;
			//int leftBottomY = srcCenterY + lessAreaRect.getHeight()-lessAreaRect.getBaseCenterPoint().getY();
			int leftBottomY = leftTopY + lessAreaRect.getHeight()-1; 
			//左上角
			int rightBottomX = rightTopX;
			int rightBottomY = leftBottomY;
			
			Polygon onePolygon = new Polygon(); 
			MyPointVO leftTopMyPointVO = AlgorithmUtil.getRotatePoint(leftTopX, leftTopY, srcCenterX, srcCenterY, -(lessAreaRect.getAngel()));
			MyPointVO rightTopMyPointVO = AlgorithmUtil.getRotatePoint(rightTopX, rightTopY, srcCenterX, srcCenterY, -(lessAreaRect.getAngel()));
			MyPointVO rightBottomMyPointVO = AlgorithmUtil.getRotatePoint(rightBottomX, rightBottomY, srcCenterX, srcCenterY, -(lessAreaRect.getAngel()));
			MyPointVO leftBottomMyPointVO = AlgorithmUtil.getRotatePoint(leftBottomX, leftBottomY, srcCenterX, srcCenterY, -(lessAreaRect.getAngel()));

			onePolygon.addPoint(leftTopMyPointVO.getX(), leftTopMyPointVO.getY());
			onePolygon.addPoint(rightTopMyPointVO.getX(), rightTopMyPointVO.getY());
			onePolygon.addPoint(rightBottomMyPointVO.getX(), rightBottomMyPointVO.getY());
			onePolygon.addPoint(leftBottomMyPointVO.getX(), leftBottomMyPointVO.getY());
			
			
			//绘制矩形框
			Graphics2D g4 = src.createGraphics(); 
			//绘制矩形框
			g4.setColor(Color.GREEN);    
			g4.fillRect(srcCenterX-1, srcCenterY-1, 3, 3);  
			g4.setColor(Color.RED); 
			g4.drawPolygon(onePolygon); 
			
			BufferedImage cutLessImageNew = ImageIO.read(new File("e://test//dd1.png")); 
			
			//绘制多边形框
			List<MyPointVO> polygonPointArray = exePolygon(cutLessImageNew, null, null, null); 
			if (polygonPointArray.size() > 4) { //大于4个顶点才处理 
				//先进行偏移
				int offsetBySrcCenterX = srcCenterX - lessAreaRect.getBaseCenterPoint().getX();
				int offsetBySrcCenterY = srcCenterY - lessAreaRect.getBaseCenterPoint().getY(); 
				if (offsetBySrcCenterX !=0 || offsetBySrcCenterY !=0 ) { 
					for (MyPointVO oneMyPointVO:polygonPointArray) { 
						oneMyPointVO.setX(oneMyPointVO.getX() + offsetBySrcCenterX);
						oneMyPointVO.setY(oneMyPointVO.getY() + offsetBySrcCenterY);
					}
				}
				//再计算旋转坐标
				if (lessAreaRect.getAngel() > 0) {
					for (MyPointVO oneMyPointVO:polygonPointArray) { 
						MyPointVO newRatorePoint = AlgorithmUtil.getRotatePoint(oneMyPointVO.getX(), oneMyPointVO.getY(), srcCenterX, srcCenterY, -lessAreaRect.getAngel());
						oneMyPointVO.setX(newRatorePoint.getX());
						oneMyPointVO.setY(newRatorePoint.getY());
					}
				}
				//组装多边形 
				Polygon newPolygon = new Polygon();
				for (MyPointVO oneMyPointVO:polygonPointArray) {
					newPolygon.addPoint(oneMyPointVO.getX(), oneMyPointVO.getY()); 
				}
				//绘制多边形  
				g4.setColor(Color.BLUE); 
				g4.drawPolygon(newPolygon);
			}
			
			g4.dispose(); 
			
			ImageIO.write(src, "png", new File("e://test//dd_end.png")); 
			
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	/**
	 * 裁切多边形 
	 * 多边形裁切规则： (参数在项目中设置)
		1.图片的宽或者高有小于或等于40像素的图片不进行多边形裁切；
		2.实际上就是将最小矩形进行4个角落的最大透明直角三角形裁切；
		3.如果裁切下来的三角形面积小于等于648，则不裁切；
		4.如果裁切下来的三角形中有一条边的长度小于等于18，则不裁切；
	 * @param cutLessImage
	 * @param lessAreaRect
	 * @return
	 */
	private static List<MyPointVO> exePolygon(BufferedImage cutLessImage, MyRectVO lessAreaRect, ProjectInfoVO projectInfoVO, ImageInfoVO imageInfoVO) { 
		
		//生成需要的 RectVO
		MyRectVO cutPolygnAreaRect = null;
		if (lessAreaRect != null) { 
			cutPolygnAreaRect = lessAreaRect.genCutPolygonRect();  
		} else {
			cutPolygnAreaRect = cutNoAlpha(cutLessImage, null, 0); 
		}
		
		//裁切图片最小宽高限制 ，必须宽和高都大于这个数
		int minWidthHeight = 40;
		//裁切下来直角三角形面积的最小限制 .必须大于这个数 
		int cutLessArea = 648;
		//裁切下来的直角三角形的两条垂直边的限制，必须大于这个数
		int cutMinWidthHeight = 18;
		
		//使用项目设置 
		if (projectInfoVO != null) {
			minWidthHeight = projectInfoVO.getPolygonImageSizeLimit();
			cutLessArea = projectInfoVO.getPolygonCutAreaLimit();
			cutMinWidthHeight = projectInfoVO.getPolygonCutBorderLengthLimit();
		}
		
		List<MyPointVO> pointArray = new ArrayList<MyPointVO>();
		
		//图片宽高 
		int cutLessImageWidth = cutLessImage.getWidth();
		int cutLessImageHeight = cutLessImage.getHeight();
		//判断图片是否满足裁切要求
		if (cutLessImageWidth <= minWidthHeight || cutLessImageHeight <= minWidthHeight) 
			return pointArray;
		
		
		//定义太小跳出距离
		int moreLessDistance = 4;
		//左上角裁切开始 -------------------------------------------------
		//获得理论上可裁切的最大直角三角形的宽和高
		int width = cutPolygnAreaRect.getTopMinPoint().getX() - cutPolygnAreaRect.getLeftTopPoint().getX();
		int height = cutPolygnAreaRect.getLeftMinPoint().getY() - cutPolygnAreaRect.getLeftTopPoint().getY();
		//判断是否满足要求 
		if (width <= cutMinWidthHeight || height <= cutMinWidthHeight) {
			//不能满足要求，这里只有一个顶点
			pointArray.add(cutPolygnAreaRect.getLeftTopPoint()); 
		} else {
			//划线的起点和终点
			MyPointVO tempStartPoint = new MyPointVO(cutPolygnAreaRect.getLeftMinPoint().getX(), cutPolygnAreaRect.getLeftMinPoint().getY()); 
			MyPointVO tempEndPoint;
			
			//当前最大裁切三角形的面积，起始坐标，结束坐标 
			int currBiggerArea = 0;
			MyPointVO currBiggerStartMyPointVO = null;
			MyPointVO currBiggerEndMyPointVO = null;
			//太小了的地方则退出 
			boolean isMoreLess = false; 
			for (int i=1; i<=width; i++) { 
				if (isMoreLess) {
					break; 
				}
				//终点  
				tempEndPoint = new MyPointVO(cutPolygnAreaRect.getLeftTopPoint().getX()+i, cutPolygnAreaRect.getLeftTopPoint().getY()); 
				List<MyPointVO> inLinePointArray = AlgorithmUtil.getInLinePoints(tempStartPoint.getX(), tempStartPoint.getY(), tempEndPoint.getX(), tempEndPoint.getY());
				//逐个点找到最后一个不透明的点 
				MyPointVO lastNoAlphaPoint = new MyPointVO(tempStartPoint.getX(), tempStartPoint.getY()); 
				for (int j=1; j<inLinePointArray.size(); j++) { 
					MyPointVO oneInLinePoint = inLinePointArray.get(j);
					int pixel = cutLessImage.getRGB(oneInLinePoint.getX(), oneInLinePoint.getY()); 
					if (pixel >> 24 != 0) { //不透明则改变
						//if (oneInLinePoint.getY() < lastNoAlphaPoint.getY()) {
							int x1 =  tempEndPoint.getX() - oneInLinePoint.getX();
							int y1 =  oneInLinePoint.getY() - tempEndPoint.getY();
							int x2 = tempEndPoint.getX() - tempStartPoint.getX();
							int y2 = tempStartPoint.getY() - tempEndPoint.getY();
							if (y1*x2 <= x1*y2) {//更大斜率 则允许 
								lastNoAlphaPoint.setX(oneInLinePoint.getX());
								lastNoAlphaPoint.setY(oneInLinePoint.getY());
							}
							//太小了则标记一下，下次循环跳出 
							if (y1 <= moreLessDistance) {
								isMoreLess = true;
							}
						//} 
					}
				}
				//判断点的合法性 
				if (i>cutMinWidthHeight && MyPointVO.getDistance(cutPolygnAreaRect.getLeftTopPoint(), lastNoAlphaPoint)>cutMinWidthHeight) {
					int xDistanceStartToEnd = tempEndPoint.getX() - lastNoAlphaPoint.getX();
					int yDistanceStartToEnd = lastNoAlphaPoint.getY() - tempEndPoint.getY();
					int leftY = 0; 
					if (xDistanceStartToEnd > 0 && yDistanceStartToEnd > 0) {
						leftY = (int)((float)i*yDistanceStartToEnd/xDistanceStartToEnd);
					}
					//获得裁切的直角三角形的面积
					int cutArea = leftY*i/2; 
					if (cutArea > cutLessArea && cutArea > currBiggerArea) {
						//满足条件重设
						currBiggerStartMyPointVO = new MyPointVO(cutPolygnAreaRect.getLeftTopPoint().getX(), cutPolygnAreaRect.getLeftTopPoint().getY()+leftY);
						currBiggerEndMyPointVO = new MyPointVO(cutPolygnAreaRect.getLeftTopPoint().getX()+i, cutPolygnAreaRect.getLeftTopPoint().getY());
						currBiggerArea = cutArea;
					} 
					//else if (cutArea < currBiggerArea) {
						//break; //开始变小则退出，不做这个处理了 
					//}
				}
				tempStartPoint = lastNoAlphaPoint;
			}
			
			if (currBiggerArea > 0) {
				pointArray.add(currBiggerStartMyPointVO); 
				pointArray.add(currBiggerEndMyPointVO); 
				//设置到imageInfoVO
				if (imageInfoVO != null) {
					//这里左边会变换为终点为0,0时的坐标
					imageInfoVO.getLeftTopVertex().setPolygonLeftPoint(currBiggerStartMyPointVO);
					imageInfoVO.getLeftTopVertex().setPolygonRightPoint(currBiggerEndMyPointVO);
					imageInfoVO.getLeftTopVertex().genContainMaxRectAreaAndSlope(); //计算围起来的直角三角形中可容纳的最大矩形面积
					//这里坐标保持不变 
					imageInfoVO.getLeftTopVertex().setPolygonCutLeftPoint(new MyPointVO(currBiggerStartMyPointVO.getX(), currBiggerStartMyPointVO.getY()));
					imageInfoVO.getLeftTopVertex().setPolygonCutRightPoint(new MyPointVO(currBiggerEndMyPointVO.getX(), currBiggerEndMyPointVO.getY()));
				}
			} else {
				pointArray.add(cutPolygnAreaRect.getLeftTopPoint()); 
			}
		}
		
		//右上角裁切开始 -------------------------------------------------
		//右上角要重新计算  TopMinPoint的X ，因为之前的是从左开始扫描的
		for (int i=cutLessImageWidth-1; i<=0; i--) {
			int pixel = cutLessImage.getRGB(i, 0); 
			if (pixel >> 24 != 0) { //不透明则改变
				cutPolygnAreaRect.getTopMinPoint().setX(i); 
				break;
			}
		}
		//获得理论上可裁切的最大直角三角形的宽和高
		width = cutPolygnAreaRect.getRightMinPoint().getX() - cutPolygnAreaRect.getTopMinPoint().getX(); 
		height = cutPolygnAreaRect.getRightMinPoint().getY() - cutPolygnAreaRect.getTopMinPoint().getY(); 
		//右上角坐标
		MyPointVO rightTopPoint = new MyPointVO(cutPolygnAreaRect.getRightMinPoint().getX(), cutPolygnAreaRect.getTopMinPoint().getY());
		//判断是否满足要求 
		if (width <= cutMinWidthHeight || height <= cutMinWidthHeight) {
			//不能满足要求，这里只有一个顶点
			pointArray.add(rightTopPoint); 
		} else {
			//划线的起点和终点
			MyPointVO tempStartPoint = new MyPointVO(cutPolygnAreaRect.getTopMinPoint().getX(), cutPolygnAreaRect.getTopMinPoint().getY()); 
			MyPointVO tempEndPoint;
			
			//当前最大裁切三角形的面积，起始坐标，结束坐标 
			int currBiggerArea = 0;
			MyPointVO currBiggerStartMyPointVO = null;
			MyPointVO currBiggerEndMyPointVO = null;
			
			//太小了的地方则退出 
			boolean isMoreLess = false; 
			for (int i=1; i<=height; i++) { 
				if (isMoreLess) {
					break; 
				}
				//终点  
				tempEndPoint = new MyPointVO(rightTopPoint.getX(), rightTopPoint.getY()+i);  
				List<MyPointVO> inLinePointArray = AlgorithmUtil.getInLinePoints(tempStartPoint.getX(), tempStartPoint.getY(), tempEndPoint.getX(), tempEndPoint.getY());
				//逐个点找到最后一个不透明的点 
				MyPointVO lastNoAlphaPoint = new MyPointVO(tempStartPoint.getX(), tempStartPoint.getY()); 
				
				//System.out.println("start tempStartPoint:"+tempStartPoint);
				//System.out.println("end tempEndPoint:"+tempEndPoint);
				
				for (int j=1; j<inLinePointArray.size(); j++) {
					MyPointVO oneInLinePoint = inLinePointArray.get(j);
					int pixel = cutLessImage.getRGB(oneInLinePoint.getX(), oneInLinePoint.getY()); 
					if (pixel >> 24 != 0) { //不透明则改变
						int x1 = tempEndPoint.getX() - oneInLinePoint.getX();
						int y1 = tempEndPoint.getY() - oneInLinePoint.getY();
						int x2 = tempEndPoint.getX() - tempStartPoint.getX();
						int y2 = tempEndPoint.getY() - tempStartPoint.getY();
						if (y1*x2 >= x1*y2) {//更大斜率 则允许 
							lastNoAlphaPoint.setX(oneInLinePoint.getX());
							lastNoAlphaPoint.setY(oneInLinePoint.getY());
						}
						//太小了则标记一下，下次循环跳出 
						if (x1 <= moreLessDistance) {
							isMoreLess = true;
						}
					}
				}
				//判断点的合法性 
				if (i>cutMinWidthHeight && MyPointVO.getDistance(rightTopPoint, lastNoAlphaPoint)>cutMinWidthHeight) {
					int xDistanceStartToEnd = tempEndPoint.getX() - lastNoAlphaPoint.getX();
					int yDistanceStartToEnd = tempEndPoint.getY() - lastNoAlphaPoint.getY();
					int topX = 0;  
					if (xDistanceStartToEnd > 0 && yDistanceStartToEnd > 0) {
						topX = (int)((float)i*xDistanceStartToEnd/yDistanceStartToEnd);
					}
					//获得裁切的直角三角形的面积
					int cutArea = topX*i/2; 
					if (cutArea > cutLessArea && cutArea > currBiggerArea) {
						//满足条件重设
						currBiggerStartMyPointVO = new MyPointVO(rightTopPoint.getX()-topX, rightTopPoint.getY());
						currBiggerEndMyPointVO = new MyPointVO(rightTopPoint.getX(), rightTopPoint.getY()+i);
						currBiggerArea = cutArea;
					} 
					//else if (cutArea < currBiggerArea) {
						//break; //开始变小则退出，不做这个处理了 
					//}
				}
				tempStartPoint = lastNoAlphaPoint;
			}
			
			if (currBiggerArea > 0) {
				pointArray.add(currBiggerStartMyPointVO); 
				pointArray.add(currBiggerEndMyPointVO); 
				//设置到imageInfoVO
				if (imageInfoVO != null) {
					//这里左边会变换为终点为0,0时的坐标
					imageInfoVO.getRightTopVertex().setPolygonLeftPoint(currBiggerStartMyPointVO);
					imageInfoVO.getRightTopVertex().setPolygonRightPoint(currBiggerEndMyPointVO);
					imageInfoVO.getRightTopVertex().genContainMaxRectAreaAndSlope(); //计算围起来的直角三角形中可容纳的最大矩形面积
					//这里坐标保持不变 
					imageInfoVO.getRightTopVertex().setPolygonCutLeftPoint(new MyPointVO(currBiggerStartMyPointVO.getX(), currBiggerStartMyPointVO.getY()));
					imageInfoVO.getRightTopVertex().setPolygonCutRightPoint(new MyPointVO(currBiggerEndMyPointVO.getX(), currBiggerEndMyPointVO.getY()));
				}
			} else {
				pointArray.add(rightTopPoint); 
			}
		}
		
		//右下角裁切开始 -------------------------------------------------
		//右下角要重新计算  getRightMinPoint的Y ，因为之前的是从左开始扫描的
		int rightMinPointX =cutLessImageWidth-1;
		for (int i=cutLessImageHeight-1; i>=0; i--) {
			int pixel = cutLessImage.getRGB(rightMinPointX, i); 
			if (pixel >> 24 != 0) { //不透明则改变
				cutPolygnAreaRect.getRightMinPoint().setY(i);  
				break;
			}
		}
		//右下角要重新计算  getBottomMinPoint的X ，因为之前的是从左开始扫描的 
		int bottomMinPointY =cutLessImageHeight-1;
		for (int i=cutLessImageWidth-1; i>=0; i--) {
			int pixel = cutLessImage.getRGB(i, bottomMinPointY); 
			if (pixel >> 24 != 0) { //不透明则改变
				cutPolygnAreaRect.getBottomMinPoint().setX(i);  
				break;
			}
		}
		//获得理论上可裁切的最大直角三角形的宽和高
		width = cutPolygnAreaRect.getRightMinPoint().getX() - cutPolygnAreaRect.getBottomMinPoint().getX(); 
		height = cutPolygnAreaRect.getBottomMinPoint().getY() - cutPolygnAreaRect.getRightMinPoint().getY(); 
		//右下角坐标
		MyPointVO rightBottmPoint = new MyPointVO(cutPolygnAreaRect.getRightMinPoint().getX(), cutPolygnAreaRect.getBottomMinPoint().getY());
		//判断是否满足要求 
		if (width <= cutMinWidthHeight || height <= cutMinWidthHeight) {
			//不能满足要求，这里只有一个顶点
			pointArray.add(rightBottmPoint); 
		} else { 
			//划线的起点和终点
			MyPointVO tempStartPoint = new MyPointVO(cutPolygnAreaRect.getRightMinPoint().getX(), cutPolygnAreaRect.getRightMinPoint().getY()); 
			MyPointVO tempEndPoint;
			
			//当前最大裁切三角形的面积，起始坐标，结束坐标 
			int currBiggerArea = 0;
			MyPointVO currBiggerStartMyPointVO = null;
			MyPointVO currBiggerEndMyPointVO = null;
			
			//太小了的地方则退出 
			boolean isMoreLess = false; 
			for (int i=1; i<=width; i++) { 
				if (isMoreLess) {
					break; 
				}
				//终点  
				tempEndPoint = new MyPointVO(rightBottmPoint.getX()-i, rightBottmPoint.getY());  
				List<MyPointVO> inLinePointArray = AlgorithmUtil.getInLinePoints(tempStartPoint.getX(), tempStartPoint.getY(), tempEndPoint.getX(), tempEndPoint.getY());
				//逐个点找到最后一个不透明的点 
				MyPointVO lastNoAlphaPoint = new MyPointVO(tempStartPoint.getX(), tempStartPoint.getY()); 
				
				//System.out.println("start tempStartPoint:"+tempStartPoint);
				//System.out.println("end tempEndPoint:"+tempEndPoint);
				
				for (int j=1; j<inLinePointArray.size(); j++) {
					MyPointVO oneInLinePoint = inLinePointArray.get(j);
					int pixel = cutLessImage.getRGB(oneInLinePoint.getX(), oneInLinePoint.getY()); 
					if (pixel >> 24 != 0) { //不透明则改变
						int x1 = oneInLinePoint.getX() - tempEndPoint.getX();
						int y1 = tempEndPoint.getY() - oneInLinePoint.getY();
						int x2 = tempStartPoint.getX() - tempEndPoint.getX();
						int y2 = tempEndPoint.getY() - tempStartPoint.getY();
						if (y1*x2 <= x1*y2) {//更大斜率 则允许 
							lastNoAlphaPoint.setX(oneInLinePoint.getX());
							lastNoAlphaPoint.setY(oneInLinePoint.getY());
						}
						//太小了则标记一下，下次循环跳出 
						if (y1 <= moreLessDistance) {
							isMoreLess = true;
						}
					}
				}
				//判断点的合法性 
				if (i>cutMinWidthHeight && MyPointVO.getDistance(rightBottmPoint, lastNoAlphaPoint)>cutMinWidthHeight) {
					int xDistanceStartToEnd = lastNoAlphaPoint.getX() - tempEndPoint.getX();
					int yDistanceStartToEnd = tempEndPoint.getY() - lastNoAlphaPoint.getY();
					int rightY = 0;   
					if (xDistanceStartToEnd > 0 && yDistanceStartToEnd > 0) {
						rightY = (int)((float)i*yDistanceStartToEnd/xDistanceStartToEnd); 
					} 
					//获得裁切的直角三角形的面积
					int cutArea = rightY*i/2; 
					if (cutArea > cutLessArea && cutArea > currBiggerArea) {
						//满足条件重设
						currBiggerStartMyPointVO = new MyPointVO(rightBottmPoint.getX(), rightBottmPoint.getY()-rightY);
						currBiggerEndMyPointVO = new MyPointVO(rightBottmPoint.getX()-i, rightBottmPoint.getY());
						currBiggerArea = cutArea;
					} 
					//else if (cutArea < currBiggerArea) {
					//break; //开始变小则退出，不做这个处理了 
					//}
				}
				tempStartPoint = lastNoAlphaPoint;
			}
			
			if (currBiggerArea > 0) {
				pointArray.add(currBiggerStartMyPointVO); 
				pointArray.add(currBiggerEndMyPointVO); 
				//设置到imageInfoVO
				if (imageInfoVO != null) {
					//这里左边会变换为终点为0,0时的坐标
					imageInfoVO.getRightBottomVertex().setPolygonLeftPoint(currBiggerStartMyPointVO);
					imageInfoVO.getRightBottomVertex().setPolygonRightPoint(currBiggerEndMyPointVO);
					imageInfoVO.getRightBottomVertex().genContainMaxRectAreaAndSlope(); //计算围起来的直角三角形中可容纳的最大矩形面积
					//这里坐标保持不变 
					imageInfoVO.getRightBottomVertex().setPolygonCutLeftPoint(new MyPointVO(currBiggerStartMyPointVO.getX(), currBiggerStartMyPointVO.getY()));
					imageInfoVO.getRightBottomVertex().setPolygonCutRightPoint(new MyPointVO(currBiggerEndMyPointVO.getX(), currBiggerEndMyPointVO.getY()));
				}
			} else {
				pointArray.add(rightBottmPoint); 
			}
		}
		
		//左下角裁切开始 -------------------------------------------------
		//左下角要重新计算  getBottomMinPoint的X ，因为之前的是从左开始扫描的 
		//int bottomMinPointY = cutLessImageHeight-1;
		for (int i=0; i<cutLessImageWidth; i++) {
			int pixel = cutLessImage.getRGB(i, bottomMinPointY); 
			if (pixel >> 24 != 0) { //不透明则改变
				cutPolygnAreaRect.getBottomMinPoint().setX(i);  
				break;
			}
		}
		//左下角要重新计算  getLeftMinPoint的Y ，因为之前的是从左开始扫描的
		for (int i=cutLessImageHeight-1; i>=0; i--) { 
			int pixel = cutLessImage.getRGB(0, i);  
			if (pixel >> 24 != 0) { //不透明则改变
				cutPolygnAreaRect.getLeftMinPoint().setY(i); 
				break;
			}
		}
		//获得理论上可裁切的最大直角三角形的宽和高
		width = cutPolygnAreaRect.getBottomMinPoint().getX() - cutPolygnAreaRect.getLeftMinPoint().getX(); 
		height = cutPolygnAreaRect.getBottomMinPoint().getY() - cutPolygnAreaRect.getLeftMinPoint().getY(); 
		//做下角坐标
		MyPointVO leftBottmPoint = new MyPointVO(cutPolygnAreaRect.getLeftMinPoint().getX(), cutPolygnAreaRect.getBottomMinPoint().getY());
		//判断是否满足要求 
		if (width <= cutMinWidthHeight || height <= cutMinWidthHeight) { 
			//不能满足要求，这里只有一个顶点
			pointArray.add(leftBottmPoint); 
		} else {
			//划线的起点和终点
			MyPointVO tempStartPoint = new MyPointVO(cutPolygnAreaRect.getBottomMinPoint().getX(), cutPolygnAreaRect.getBottomMinPoint().getY()); 
			MyPointVO tempEndPoint;
			
			//当前最大裁切三角形的面积，起始坐标，结束坐标 
			int currBiggerArea = 0;
			MyPointVO currBiggerStartMyPointVO = null;
			MyPointVO currBiggerEndMyPointVO = null;
			
			//太小了的地方则退出 
			boolean isMoreLess = false; 
			for (int i=1; i<=height; i++) {  
				if (isMoreLess) {
					break; 
				}
				//终点  
				tempEndPoint = new MyPointVO(leftBottmPoint.getX(), leftBottmPoint.getY()-i);  
				List<MyPointVO> inLinePointArray = AlgorithmUtil.getInLinePoints(tempStartPoint.getX(), tempStartPoint.getY(), tempEndPoint.getX(), tempEndPoint.getY());
				//逐个点找到最后一个不透明的点 
				MyPointVO lastNoAlphaPoint = new MyPointVO(tempStartPoint.getX(), tempStartPoint.getY()); 
				
				//System.out.println("start tempStartPoint:"+tempStartPoint);
				//System.out.println("end tempEndPoint:"+tempEndPoint);
				
				for (int j=1; j<inLinePointArray.size(); j++) {
					MyPointVO oneInLinePoint = inLinePointArray.get(j);
					int pixel = cutLessImage.getRGB(oneInLinePoint.getX(), oneInLinePoint.getY()); 
					if (pixel >> 24 != 0) { //不透明则改变
						int x1 = oneInLinePoint.getX() - tempEndPoint.getX();
						int y1 = oneInLinePoint.getY() - tempEndPoint.getY();
						int x2 = tempStartPoint.getX() - tempEndPoint.getX();
						int y2 = tempStartPoint.getY() - tempEndPoint.getY();
						if (y1*x2 >= x1*y2) {//更大斜率 则允许 
							lastNoAlphaPoint.setX(oneInLinePoint.getX());
							lastNoAlphaPoint.setY(oneInLinePoint.getY());
						}
						//太小了则标记一下，下次循环跳出 
						if (x1 <= moreLessDistance) { 
							//System.out.println("tempStartPoint="+tempStartPoint);
							//System.out.println("tempEndPoint="+tempEndPoint);
							//System.out.println("oneInLinePoint="+oneInLinePoint);
							isMoreLess = true;
						}
					}
				}
				//判断点的合法性 
				if (i>cutMinWidthHeight && MyPointVO.getDistance(leftBottmPoint, lastNoAlphaPoint)>cutMinWidthHeight) {
					int xDistanceStartToEnd = lastNoAlphaPoint.getX() - tempEndPoint.getX();
					int yDistanceStartToEnd = lastNoAlphaPoint.getY() - tempEndPoint.getY();
					int bottomX = 0;   
					if (xDistanceStartToEnd > 0 && yDistanceStartToEnd > 0) {
						bottomX = (int)((float)i*xDistanceStartToEnd/yDistanceStartToEnd);
					}
					//获得裁切的直角三角形的面积
					int cutArea = bottomX*i/2; 
					if (cutArea > cutLessArea && cutArea > currBiggerArea) {
						//满足条件重设
						currBiggerStartMyPointVO = new MyPointVO(leftBottmPoint.getX()+bottomX, leftBottmPoint.getY());
						currBiggerEndMyPointVO = new MyPointVO(leftBottmPoint.getX(), leftBottmPoint.getY()-i);
						currBiggerArea = cutArea;
					} 
					//else if (cutArea < currBiggerArea) { 
						//break; //开始变小则退出，不做这个处理了 
					//}
				}
				tempStartPoint = lastNoAlphaPoint;
			}
			
			if (currBiggerArea > 0) {
				pointArray.add(currBiggerStartMyPointVO); 
				pointArray.add(currBiggerEndMyPointVO); 
				//设置到imageInfoVO
				if (imageInfoVO != null) { 
					//这里左边会变换为终点为0,0时的坐标
					imageInfoVO.getLeftBottomVertex().setPolygonLeftPoint(currBiggerStartMyPointVO);
					imageInfoVO.getLeftBottomVertex().setPolygonRightPoint(currBiggerEndMyPointVO);
					imageInfoVO.getLeftBottomVertex().genContainMaxRectAreaAndSlope(); //计算围起来的直角三角形中可容纳的最大矩形面积
					//这里坐标保持不变 
					imageInfoVO.getLeftBottomVertex().setPolygonCutLeftPoint(new MyPointVO(currBiggerStartMyPointVO.getX(), currBiggerStartMyPointVO.getY()));
					imageInfoVO.getLeftBottomVertex().setPolygonCutRightPoint(new MyPointVO(currBiggerEndMyPointVO.getX(), currBiggerEndMyPointVO.getY()));
				}
			} else {
				pointArray.add(leftBottmPoint); 
			}
		}
		
		//测试  -------------------------------------------------
		//pointArray.add(new MyPointVO(cutPolygnAreaRect.getRightMinPoint().getX(), cutPolygnAreaRect.getTopMinPoint().getY()));
		//pointArray.add(new MyPointVO(cutPolygnAreaRect.getRightMinPoint().getX(), cutPolygnAreaRect.getBottomMinPoint().getY()));
		//pointArray.add(new MyPointVO(cutPolygnAreaRect.getLeftMinPoint().getX(), cutPolygnAreaRect.getBottomMinPoint().getY()));
		
		//组装多边形 
//		Polygon newPolygon = new Polygon();
//		for (MyPointVO oneMyPointVO:pointArray) {
//			newPolygon.addPoint(oneMyPointVO.getX(), oneMyPointVO.getY()); 
//		}
//		Graphics2D g4 = cutLessImage.createGraphics();  
//		g4.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//		//绘制多边形  
//		g4.setColor(Color.RED); 
//		g4.drawPolygon(newPolygon); 
		
//		try {
//			ImageIO.write(cutLessImage, "png", new File("e://test//dd_111.png"));
//		} catch (IOException e) { 
//			e.printStackTrace();
//		} 
		
		return pointArray;
	}
	
	/**
	 * 新的中点 
	 * @param firstCenterPoint
	 * @param firstCutImage
	 * @param rotateImage
	 * @param rotareAngel
	 * @return
	 */
	private static MyPointVO getLessCenterPoint(MyPointVO firstCenterPoint, BufferedImage firstCutImage, BufferedImage rotateImage, int rotareAngel) {
		int firstCutImageWidth = firstCutImage.getWidth(); 
		int firstCutImageHeight = firstCutImage.getHeight(); 
		int rotateImageWidth = rotateImage.getWidth();  
		int rotateImageHeight = rotateImage.getHeight();  
		int widthLess = (rotateImageWidth - firstCutImageWidth)/2;  
		int heightLess = (rotateImageHeight - firstCutImageHeight)/2;  
		int rotateCenterX = rotateImageWidth/2; 
		int rotateCenterY = rotateImageHeight/2; 
		int tempBaseCenterX = firstCenterPoint.getX() + widthLess; 
		int tempBaseCenterY = firstCenterPoint.getY() + heightLess; 
//		System.out.println("tempBaseCenterX:"+new MyPointVO(tempBaseCenterX, tempBaseCenterY));
		return AlgorithmUtil.getRotatePoint(tempBaseCenterX, tempBaseCenterY, rotateCenterX, rotateCenterY, rotareAngel); 
	}
	
	/**
	 * 旋转图片得到一个新的图片
	 * @param src
	 * @param angel
	 * @return
	 */
	private static BufferedImage rotate(Image src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		// calculate the new image size
		Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);

		BufferedImage res = null;
		res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = res.createGraphics();
		//alpha 混合最精确算法
		g2.setRenderingHint( RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		//抗锯齿模式
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//颜色呈现 最精确
		g2.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		//抖动提示 不抖动
		g2.setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		//插值提示键  周围9种样本
		g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		//插值提示键  周围4种样本
		//g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//插值提示键  最接近的样本
		//g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		//呈现提示  重质量
		g2.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		// transform
		g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

		g2.drawImage(src, null, null);
		g2.dispose(); 
		return res;
	}

	/**
	 * 计算旋转后的尺寸
	 * @param src
	 * @param angel
	 * @return
	 */
	private static Rectangle calcRotatedSize(Rectangle src, int angel) {
		// if angel is greater than 90 degree, we need to do some conversion
		if (angel >= 90) {
			if(angel / 90 % 2 == 1){
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}

		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);

		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
				- angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
				- angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;
		return new java.awt.Rectangle(new Dimension(des_width+6, des_height+6));
	}

	/**
	 * 裁剪没有透明通道区域的矩形
	 * @param src
	 * @param baseCenterPoint
	 * @param angel
	 * @return
	 */
	private static MyRectVO cutNoAlpha(BufferedImage src, MyPointVO baseCenterPoint, int angel) {
		//获得宽高 
		int height = src.getHeight();
		int width = src.getWidth();
//		System.out.println("-------------angel:"+angel); 
		// 中心点
		if (baseCenterPoint == null) { 
			baseCenterPoint = new MyPointVO(width / 2, height / 2);
		} 
		//左右上下，进行扫描，检测不透明像素进行裁剪 
		MyPointVO leftMinPoint = null, rightMinPoint = null, topMinPoint = null, bottomMinPoint = null;

		leftFor: for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixel = src.getRGB(i, j);
				if (pixel >> 24 != 0) {
					leftMinPoint = new MyPointVO(i, j);
					break leftFor;
				}
			}
		}

		topFor: for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = src.getRGB(j, i);
				if (pixel >> 24 != 0) {
					topMinPoint = new MyPointVO(j, i);
					break topFor;
				}
			}
		}

		rightFor: for (int i = width - 1; i >= 0; i--) {
			for (int j = 0; j < height; j++) {
				int pixel = src.getRGB(i, j);
				if (pixel >> 24 != 0) {
					rightMinPoint = new MyPointVO(i, j);
					break rightFor;
				}
			}
		}

		bottomFor: for (int i = height - 1; i >= 0; i--) {
			for (int j = 0; j < width; j++) { 
				int pixel = src.getRGB(j, i);
				if (pixel >> 24 != 0) {
					bottomMinPoint = new MyPointVO(j, i);
					break bottomFor;
				}
			}
		}
		
		//基础中心点在裁剪后的子图像里面的坐标 
		MyPointVO baseCenterPointInSubImage = new MyPointVO(baseCenterPoint.getX()-leftMinPoint.getX(), baseCenterPoint.getY()-topMinPoint.getY());
		//组装矩形对象
		MyRectVO myRect = new MyRectVO(baseCenterPointInSubImage, leftMinPoint, rightMinPoint, topMinPoint, bottomMinPoint); 
		//设置角度
		myRect.setAngel(angel);
		return myRect;
	}  
	
	/**
	 * 写入子图片
	 * @param image
	 * @param subImage 
	 * @param x 写入位置
	 * @param y 写入位置 
	 * @param isRotate = true 为顺时针90度旋转 
	 */
	public static BufferedImage writeSubImage(BufferedImage image, BufferedImage subImage, int x, int y, boolean isRotate) {  
		BufferedImage tag = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB); 
		Graphics2D g2 = tag.createGraphics();    
		g2.drawImage(image, 0, 0, null);  
		//alpha 混合最精确算法 
		//g2.setRenderingHint( RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		//抗锯齿模式
		//g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//颜色呈现 最精确
		//g2.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		//抖动提示 不抖动
		//g2.setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		//插值提示键  周围9种样本
		//g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		//插值提示键  周围4种样本
		//g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//插值提示键  最接近的样本
		//g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		//呈现提示  重质量
		//g2.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		if (isRotate) {
			int imageCenterX = image.getWidth()/2; 
			int imageCenterY = image.getHeight()/2; 
			g2.rotate(Math.toRadians(90), imageCenterX, imageCenterY);   
			MyPointVO rotatePoint = AlgorithmUtil.getRotatePoint(x+subImage.getHeight(), y, imageCenterX, imageCenterY, -90);
			g2.drawImage(subImage, rotatePoint.getX(), rotatePoint.getY(), null);   
		} else {
			g2.drawImage(subImage, x, y, null);  
		}
		g2.dispose(); 
		return tag; 
	}
	
}
