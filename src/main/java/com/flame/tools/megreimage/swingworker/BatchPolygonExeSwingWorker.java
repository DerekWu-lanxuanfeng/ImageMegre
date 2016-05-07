package com.flame.tools.megreimage.swingworker;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import com.flame.tools.megreimage.AppManager;
import com.flame.tools.megreimage.AppMediator;
import com.flame.tools.megreimage.consts.ImageStatusEnum;
import com.flame.tools.megreimage.consts.ProjectStatusEnum;
import com.flame.tools.megreimage.consts.RectVertexPositionEnum;
import com.flame.tools.megreimage.util.ImageUtil;
import com.flame.tools.megreimage.util.StringUtil;
import com.flame.tools.megreimage.util.VertexFillUtil;
import com.flame.tools.megreimage.vo.FillHorizontalLineVO;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.MyPointVO;
import com.flame.tools.megreimage.vo.MyVertexVO;
import com.flame.tools.megreimage.vo.ProjectInfoVO;
import com.flame.tools.megreimage.vo.VertexImage;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.swingworker.BatchPolygonExeSwingWorker.java
 * @Description: 批量多边形运算 
 * @Create: DerekWu  2016年4月25日 下午2:35:45 
 * @version: V1.0 
 */
public class BatchPolygonExeSwingWorker extends SwingWorker<ProjectInfoVO, Integer> {

	/** 项目 */
	private ProjectInfoVO projectInfoVO;
	
	/** 图片文件 */
	private List<ImageInfoVO> imageList;
	
	/** 进度条  */
	private ProgressMonitor progressMonitor; 
	
	/** 进度描述 */
	private String noteDesc;
	
	/**
	 * 构造函数
	 */
	public BatchPolygonExeSwingWorker(ProjectInfoVO projectInfoVO, List<ImageInfoVO> imageList) { 
		this.projectInfoVO = projectInfoVO;
		this.imageList = imageList;
	}

	
	@Override
	protected ProjectInfoVO doInBackground() throws Exception {
		//开启进度条
		progressMonitor = new ProgressMonitor(AppMediator.getAppJFrame().getjFrame(), "正在进行多边形组合运算...", "当前进度：0%", 0, imageList.size()*4);
				
		noteDesc = "多边形运算进度:";
		
		//统计各种多边形的数量
		int fourNum = 0, fiveNum = 0, sixNum = 0, sevenNum = 0, eightNum = 0, exedNum = 0; 
		for (int i=0; i<imageList.size(); i++) {
			ImageInfoVO oneImageInfoVO = imageList.get(i);
			//只有还没有处理的图片才进行处理
			if (!ImageStatusEnum.NO_EXE.equals(oneImageInfoVO.getStatus())) {
				//获得图片所在路径和存放路径 
				//String imagePath = AppManager.getImagePath(projectInfoVO.getUseDirectory(), oneImageInfoVO.getImageName()); 
				String writePath = AppManager.getImageCutImagePath(oneImageInfoVO.getProjectName(), oneImageInfoVO.getImageName());
				//多边形运算，同时更新imageInfoVO ， 0=成功进行多边形运算  1=失败  2=没运算成多边形 
				int exeResult = ImageUtil.exePolygonByCutLessRectImage(oneImageInfoVO, writePath, projectInfoVO); 
				if (exeResult == 1) {
					//通知设置进度
					publish(i+1); 
					continue; 
				} else if (exeResult == 0) { 
					//放入缓存
					AppManager.putImageInfoVOInCache(oneImageInfoVO); 
					//写入配置文件
					AppManager.writeOneImageConfig(oneImageInfoVO); 
				} 
			} 
			exedNum++;
			//统计多边形数量 
			int pointNum = oneImageInfoVO.getVertexNum(); 
			if (pointNum == 4) fourNum++;
			if (pointNum == 5) fiveNum++;
			if (pointNum == 6) sixNum++;
			if (pointNum == 7) sevenNum++;
			if (pointNum == 8) eightNum++;
		
			//通知设置进度
			publish(i+1);  
			
		} 
		
		//组合多边形开始  		
		//通过项目名获得image顶点裁切的三角形列表，排序是可放矩形面积大,斜率大优先 
		List<MyVertexVO> vertexList = AppManager.getImageVertexInfoListByProjectName(projectInfoVO.getName()); 
		if (vertexList.size() > 0) {  
			noteDesc = "多边形组合进度:"; 
			//通过项目名获得不超过指定面积的image列表，排序是可放矩形面积大,斜率大优先  
			List<ImageInfoVO> imageInfoList = AppManager.getImageInfoListByProjectName(projectInfoVO.getName(), vertexList.get(0).getContainMaxRectArea());
			for (int i=0; i<vertexList.size(); i++) { 
				MyVertexVO oneMyVertexVO = vertexList.get(i);
				if (imageInfoList.size() > 0) { 
					//当前顶点所在图片
					ImageInfoVO imageInfo = AppManager.getImageInfoVOFromCache(projectInfoVO.getName(), oneMyVertexVO.getImageName());
					//当前顶点所在图片的父图片名字 
					short parentImageName = imageInfo.getParentImageName();
					if (parentImageName > 0) { //最大处理两级的多边形嵌套
						System.out.println("parentImage===="+imageInfo.getImageName()); 
						//如果父图片还存在父图片，则不处理，只嵌套两层
						ImageInfoVO parentImageInfo = AppManager.getImageInfoVOFromCache(projectInfoVO.getName(), parentImageName);
						if (parentImageInfo.getParentImageName() > 0) {
							continue;  
						}
					}
					//处理一个顶点处的填充
					boolean isEnd = this.exeVertexFillImage(oneMyVertexVO, imageInfo, imageInfoList); 
					publish(i+1);  
					if (isEnd) {
						System.out.println("end polygon length="+vertexList.size()+" i="+i+" imageName="+imageInfo);  
						break; 
					}
				} else {
					break;
				}
			}
		}
		
		//保存图片开始 
		//先删除合图目录下所有文件 
		AppManager.clearMegreImageDir(projectInfoVO.getName());  
		//找到所有图片 
		List<ImageInfoVO> imageList = AppManager.getImageInfoListByProjectName(projectInfoVO.getName()); 
		noteDesc = "保存组合图片进度:";
		for (int i=0; i<imageList.size(); i++) {
			ImageInfoVO oneImageInfoVO = imageList.get(i);
			if (oneImageInfoVO.getParentImageName() == 0) { //没有父级的图片才要重新写
				//获得所有子图片 
				List<VertexImage> allSubImageList = oneImageInfoVO.getAllSubImageList();
				if (allSubImageList.size() == 0) {
					//没有子图片，直接保存 
					AppManager.writeImageToMegreDir(oneImageInfoVO); 
				} else {
					//保存父级图片 
					AppManager.writeParentImageToMegreDir(oneImageInfoVO, allSubImageList); 
					//放入缓存
					AppManager.putImageInfoVOInCache(oneImageInfoVO); 
					//写入配置文件
					AppManager.writeOneImageConfig(oneImageInfoVO); 
				}
			} else { //有父级的图片只用重写配置 
				//放入缓存
				AppManager.putImageInfoVOInCache(oneImageInfoVO); 
				//写入配置文件
				AppManager.writeOneImageConfig(oneImageInfoVO); 
			}
			publish(i+1); 
		}
		
		//重新设置数量 
		projectInfoVO.setImageNum(imageList.size());
		projectInfoVO.setNoExedNum(imageList.size()-exedNum); 
		projectInfoVO.setExedNum(exedNum); 
		if (imageList.size() == exedNum) {
			projectInfoVO.setStatus(ProjectStatusEnum.POLYGON_EXE);   
		} 
		projectInfoVO.setFourPolygonNum(fourNum); 
		projectInfoVO.setFivePolygonNum(fiveNum); 
		projectInfoVO.setSixPolygonNum(sixNum); 
		projectInfoVO.setSevenPolygonNum(sevenNum); 
		projectInfoVO.setEightPolygonNum(eightNum); 
		//写入项目配置文件
		AppManager.writeOneProjectConfig(projectInfoVO); 
				
		//System.out.println("--------end------------"); 
		return projectInfoVO;  
	}
	
	@Override 
	protected void process(List<Integer> chunks) { 
		for (int exeNum:chunks) {
			//int rate = exeNum*100/imageList.size(); 
			progressMonitor.setNote(StringUtil.connectStr(noteDesc, exeNum));  
			progressMonitor.setProgress(exeNum); 
		} 
    }
	
	@Override 
	protected void done() { 
		try {
			ProjectInfoVO projectInfoVO = get();
			AppMediator.oneProjectExeImportImage(projectInfoVO);
			progressMonitor.close(); 
		} catch (Exception e) { 
			progressMonitor.close(); 
			e.printStackTrace();
		}
    }
	
	/**
	 * 处理顶点处填充图片，采用高水平位选择填充策略 ，模拟左上角裁切的三角形进行处理，后面通过坐标变换来确定真实坐标  
	 * @param vertexVO
	 * @param imageInfoList
	 * @return 是否终止顶点填充 
	 */
	private boolean exeVertexFillImage(MyVertexVO vertexVO, ImageInfoVO parentImage, List<ImageInfoVO> imageInfoList) { 
		VertexFillUtil vertexFillUtil = new VertexFillUtil(vertexVO, parentImage); 
		//System.out.println("vertex==="+vertexFillUtil); 
		boolean isEnd = fillOneImage(vertexFillUtil, imageInfoList, true); 
		if (isEnd) { //终止循环 
			return true;
		}
		while (vertexFillUtil.gethLineList().size() > 0 && imageInfoList.size() > 0) {
			//如果还有可填充的最低水平线则进行填充 
			fillOneImage(vertexFillUtil, imageInfoList, false); 
		}
		return false;
	}
	
	/**
	 * 
	 * @param vertexFillUtil
	 * @param imageInfoList
	 * @param isFirst 是否第一次填充 
	 * @return 是否终止顶点填充 
	 */
	private boolean fillOneImage(VertexFillUtil vertexFillUtil, List<ImageInfoVO> imageInfoList, boolean isFirst) {
		//获得最低水平线 
		FillHorizontalLineVO lessHLine = vertexFillUtil.gethLineList().get(0);
		
		//System.out.println("========================================================");
		
		//System.out.println("lessHLine=="+lessHLine);
		
		//获得高于最低水平线的小于最低水平线开始x坐标的结束x坐标
		int maxEndX = getLessOneMaxEndPointX(lessHLine, vertexFillUtil.gethLineList());
		//变换最低水平线x坐标；
		lessHLine.getStartPoint().setX(maxEndX);
		
		//System.out.println("lessHLine==="+lessHLine);   
		
		//获得所有图片中的最小面积图片
		ImageInfoVO lessAreaImage = imageInfoList.get(imageInfoList.size()-1);
		//可填充最大面积 ,最大高度，最大宽度,当前水平线起点处离顶部的最大高度,当前水平线的宽度
		int fillMaxArea, fillMaxWidth, fillMaxHeight, currHLineMaxHeight, currHLineWidth;
		if (isFirst) {
			fillMaxArea = vertexFillUtil.getFirstFillMaxArea();
			//没有面积小于可填充面积的图片了 
			if (fillMaxArea < lessAreaImage.getCutArea()) {
				vertexFillUtil.gethLineList().remove(0); //删除当前水平线 
				return true; //由于是首次填充，所有终止顶点填充，后面更小面积的顶点就不用再计算了
			}
			//可填充最大宽高
			fillMaxWidth = vertexFillUtil.getWidth() - 2;
			fillMaxHeight = vertexFillUtil.getHeight() - 2;
			currHLineMaxHeight = vertexFillUtil.getHeight();  
			currHLineWidth = vertexFillUtil.getWidth(); 
		} else {
			//当前水平线起点处离顶部的最大高度
			//int maxHeight = vertexFillUtil.getHeight() - lessHLine.getStartPoint().getY(); 
			currHLineMaxHeight = vertexFillUtil.getHeight() * (vertexFillUtil.getWidth()-lessHLine.getStartPoint().getX()) / vertexFillUtil.getWidth();
			//当前水平线起点处离右边的最大宽度
			int currHLineMaxWidth = vertexFillUtil.getWidth()*currHLineMaxHeight/vertexFillUtil.getHeight();   
			//当前水平线宽度   
			currHLineWidth = lessHLine.getEndPoint().getX()-lessHLine.getStartPoint().getX();
			if (currHLineWidth >= currHLineMaxWidth/2) {
				fillMaxArea = (currHLineMaxHeight-2)*(currHLineMaxWidth-2)/4;
			} else {
				fillMaxArea = (currHLineMaxHeight-2)*(currHLineWidth-2)/4; 
			} 
			//没有面积小于可填充面积的图片了 
			if (fillMaxArea < lessAreaImage.getCutArea()) { 
				//删除此水平线 
				vertexFillUtil.gethLineList().remove(0); 
				return false; 
			}
			//可填充最大宽高
			fillMaxWidth = currHLineWidth - 2;
			fillMaxHeight = currHLineMaxHeight - 2;
		}
		//获得图片的更短边
		int lessBorder = lessAreaImage.getLessBorder();
		//最小面积中最短边对应的宽度和高度，减掉这个区域缩小搜索范围
		int lessCutWidth =  vertexFillUtil.getWidth()*lessBorder/vertexFillUtil.getHeight();
		int lessCutHeight = vertexFillUtil.getHeight()*lessBorder/vertexFillUtil.getWidth();
		if (lessCutWidth < 1) lessCutWidth = 1; 
		if (lessCutHeight < 1) lessCutHeight = 1; 
		//从新设置允许的最大宽高
		fillMaxWidth -= lessCutWidth;
		fillMaxHeight -= lessCutHeight;
		//
		
		//找到填充图片 
		VertexImage oneVertexImage = null;
		//删除列表，找到了合适的就从列表中删除，如果是首次填充，大于可填充最大面积的也删除，应为后面的顶点首次填充面积更小 
		List<ImageInfoVO> delImageList = new ArrayList<ImageInfoVO>();
		for (ImageInfoVO oneImageInfoVO:imageInfoList) {
			if (oneVertexImage != null) {
				break;  
			}
			//排除面积大的一部分 
			if (oneImageInfoVO.getCutArea() > fillMaxArea) {
				if (isFirst) { //如果是首次填充，放入删除列表 
					delImageList.add(oneImageInfoVO);
				}
				continue;
			}
			//排除超长的 
			int imageCutWidth = oneImageInfoVO.getCutWidth();
			int imageCutHeight = oneImageInfoVO.getCutHeight();
			if (imageCutWidth>fillMaxWidth || imageCutWidth>fillMaxHeight || imageCutHeight>fillMaxWidth || imageCutHeight>fillMaxHeight) {
				continue;
			}
			//是否需要旋转 
			boolean isRotare = false;
			//获得该顶点处的斜率
			float vertexFillSlope = vertexFillUtil.getSlope();
			//图片的斜率
			float imageSlope = oneImageInfoVO.getCutSlope();
			//判断是否需要旋转 
			if ((vertexFillSlope > 1 && imageSlope < 1) || (vertexFillSlope < 1 && imageSlope > 1)) {
				isRotare = true;
			}
			//图片宽高 
			int imageWidth, imageHeight;
			if (isRotare) { //旋转的交换 宽高 
				imageWidth = oneImageInfoVO.getCutHeight(); 
				imageHeight = oneImageInfoVO.getCutWidth(); 
			} else {
				imageWidth = oneImageInfoVO.getCutWidth(); 
				imageHeight = oneImageInfoVO.getCutHeight(); 
			}
			//根据图片高度计算可以放得下的最大宽度 
			//剩余高度 
			int surplusHeight = currHLineMaxHeight - lessHLine.getStartPoint().getY() - imageHeight; 
			//最大剩余宽度
			int maxSuplusWidth = surplusHeight*vertexFillUtil.getWidth()/vertexFillUtil.getHeight();
			//剩余宽度
			int surplusWidth = maxSuplusWidth-2<fillMaxWidth?maxSuplusWidth-2:fillMaxWidth;
			//图片宽度小于最大剩余宽度，可以放入 
			if (surplusWidth >= imageWidth) { 
				//添加到删除列表 
				delImageList.add(oneImageInfoVO);
				//当前的顶点
				MyVertexVO currVertex = vertexFillUtil.getVertexVO();
				//设置图片所在父图片的信息
				oneImageInfoVO.setParentImageName(currVertex.getImageName());
				oneImageInfoVO.setParentPosition(currVertex.getPosition()); 
				//初始化添加到该顶点处的图片 
				//图片在父图片的坐标位置
				MyPointVO inParentPoint = getPointInParentImage(vertexFillUtil, lessHLine.getStartPoint(), oneImageInfoVO, isRotare); 
				oneVertexImage = new VertexImage(oneImageInfoVO.getImageName(), currVertex.getPosition(), isRotare, inParentPoint);
				//将图片设置到顶点 
				currVertex.putSubImage(oneVertexImage); 
				//System.out.println("add image="+oneVertexImage); 
				
				//图片已经放入，计算放入后影响的水平线  
				//增加一条新水平线 ,不能太小 ，太小则不增加 
				if (surplusHeight - 2 > 5 && maxSuplusWidth - 2 > 5) { 
					//新水平线的Y坐标 
					int newHLineY = lessHLine.getStartPoint().getY() + imageHeight + 2;
					//新水平线的宽度
					int newHLineWidth = (currHLineMaxHeight - newHLineY) * vertexFillUtil.getWidth() / vertexFillUtil.getHeight();
//					if (newHLineWidth > currHLineWidth) { //不能大于当前水平线的宽度 
//						newHLineWidth = currHLineWidth;
//					}  
					MyPointVO newStartPoint = new MyPointVO(lessHLine.getStartPoint().getX(), newHLineY); 
					MyPointVO newEndPoint = new MyPointVO(newStartPoint.getX() + newHLineWidth, newHLineY); 
					FillHorizontalLineVO newHLine = new FillHorizontalLineVO(newStartPoint, newEndPoint); 
					//System.out.println("newHLine="+newHLine); 
					//高一级的水平线索引，如果有合并，则设置为-2，啥也不干 
					int bigNewHLineIndex = -1;
					for (int i=1; i<vertexFillUtil.gethLineList().size(); i++) { //跳过第一条水平线，后面单独处理 
						FillHorizontalLineVO oneHLine = vertexFillUtil.gethLineList().get(i);
						if (oneHLine.getStartPoint().getY() < newHLine.getStartPoint().getY()) { //处理被新水平线阶段的水平线 
							if (oneHLine.getStartPoint().getX() < newHLine.getStartPoint().getX() && oneHLine.getEndPoint().getX() > newHLine.getStartPoint().getX()) {
								oneHLine.getEndPoint().setX(newHLine.getStartPoint().getX());
							} 
						} else if (oneHLine.getStartPoint().getY() == newHLine.getStartPoint().getY()) { //处理和新水平线相等水平位置的水平线  
							if (!(oneHLine.getStartPoint().getX() > newHLine.getEndPoint().getX() || oneHLine.getEndPoint().getX() < newHLine.getStartPoint().getX())) {
								//满足合并调教则合并 
								int lessHLineStartX = oneHLine.getStartPoint().getX() <= newHLine.getStartPoint().getX()?oneHLine.getStartPoint().getX():newHLine.getStartPoint().getX();
								int lessHLineEndX = oneHLine.getEndPoint().getX() >= newHLine.getEndPoint().getX()?oneHLine.getEndPoint().getX():newHLine.getEndPoint().getX();
								oneHLine.getStartPoint().setX(lessHLineStartX);
								oneHLine.getEndPoint().setX(lessHLineEndX);
								bigNewHLineIndex = -2; //设置标记
								newHLine = oneHLine; //设置为新的，为后面更高的对新的进行截断做准备
							}
						} else { //看是否需要阶段
							if (bigNewHLineIndex == -1) {
								bigNewHLineIndex = i;
							}
							if (oneHLine.getStartPoint().getX() < newHLine.getEndPoint().getX() && oneHLine.getStartPoint().getX() > newHLine.getStartPoint().getX()) {
								newHLine.getEndPoint().setX(oneHLine.getStartPoint().getX()); //截断新的 
							}
						}
					}
					
					if (bigNewHLineIndex > 0) { //添加到指定 
						vertexFillUtil.gethLineList().add(bigNewHLineIndex, newHLine); 
					} else if (bigNewHLineIndex == -1) { //添加到末尾 
						vertexFillUtil.gethLineList().add(newHLine); 
					}
					
				}
				
				//当前水平线x变化 
				int currHLineX = lessHLine.getStartPoint().getX() + imageWidth + 2;
				if (lessHLine.getEndPoint().getX() - currHLineX <= 5 ) {
					//当前剩余空间太小，删除当前水平线 
					vertexFillUtil.gethLineList().remove(0);
				} else {
					//当前水平线变更
					lessHLine.getStartPoint().setX(currHLineX);  
				}
			}
		}
		if (oneVertexImage == null) {
			//没有找到合适的图片，删除当前水平线 
			vertexFillUtil.gethLineList().remove(0);
		}
		//删除无效的图片 
		for (ImageInfoVO oneImageInfoVO:delImageList) {
			imageInfoList.remove(oneImageInfoVO); 
		}
		return false;
	}
	
	/**
	 * 找到高于当前水平线并且结束x坐标小于当前水平线开始X坐标的水平线中的最大的结束x坐标
	 * @param lessHLine
	 * @param hLineList
	 * @return
	 */
	private int getLessOneMaxEndPointX(FillHorizontalLineVO lessHLine, List<FillHorizontalLineVO> hLineList) {
		int maxEndX = lessHLine.getStartPoint().getX(); 
//		boolean isNotUseMaxEndX = false;
//		int hLineListSize = hLineList.size();
//		if (hLineListSize > 1) {
//			for (int i=1; i<hLineListSize; i++) {
//				FillHorizontalLineVO oneHLine = hLineList.get(i); 
//				System.out.println("oneHLine=="+oneHLine); 
//				if (oneHLine.getStartPoint().getY() > lessHLine.getStartPoint().getY()) { 
//					if (oneHLine.getEndPoint().getX() < lessHLine.getStartPoint().getX() && oneHLine.getEndPoint().getX() < maxEndX) {
//						maxEndX = oneHLine.getEndPoint().getX(); 
//					} else if (oneHLine.getStartPoint().getX()<){
//						
//					}
//				}
//			}
//		}
//		if (isNotUseMaxEndX) {
//			return lessHLine.getStartPoint().getX();
//		} else {			
			return maxEndX;
//		}
	}

	/**
	 * 获得在父图片的坐标位置 
	 * @param vertexFillUtil
	 * @param lessHLineStartPoint
	 * @param isRotate 是否进行90度旋转 
	 * @return
	 */
	private MyPointVO getPointInParentImage(VertexFillUtil vertexFillUtil, MyPointVO lessHLineStartPoint, ImageInfoVO subImage, boolean isRotate) {
		//方位
		RectVertexPositionEnum position = vertexFillUtil.getVertexVO().getPosition();
		//默认是方位为左上方的坐标
		MyPointVO inParentPoint = new MyPointVO(lessHLineStartPoint.getX(), lessHLineStartPoint.getY());
		if (RectVertexPositionEnum.LEFT_TOP.equals(position)) { 
			return inParentPoint;
		}
		
		//父图片 及 其宽高 
		ImageInfoVO parentImage = vertexFillUtil.getImageInfoVO();
		int parentImageWidth = parentImage.getCutWidth();
		int parentImageHeight = parentImage.getCutHeight(); 
		//子图片宽高
		int subImageWidth, subImageHeight;
		if (isRotate) {
			subImageWidth = subImage.getCutHeight();
			subImageHeight = subImage.getCutWidth();
		} else {
			subImageWidth = subImage.getCutWidth();
			subImageHeight = subImage.getCutHeight();
		}
		
		if (RectVertexPositionEnum.RIGHT_TOP.equals(position)) { 
			//处理右上方
			inParentPoint.setX(parentImageWidth-lessHLineStartPoint.getX()-subImageWidth); 
		} else if (RectVertexPositionEnum.RIGHT_BOTTOM.equals(position)) {
			//处理右下方
			inParentPoint.setX(parentImageWidth-lessHLineStartPoint.getX()-subImageWidth);
			inParentPoint.setY(parentImageHeight-lessHLineStartPoint.getY()-subImageHeight);
		} else if (RectVertexPositionEnum.LEFT_BOTTOM.equals(position)) {
			//处理左下方 
			inParentPoint.setY(parentImageHeight-lessHLineStartPoint.getY()-subImageHeight);
		}
		
		return inParentPoint;
	}
}
