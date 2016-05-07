package com.flame.tools.megreimage.swingworker;

import java.util.List;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import com.flame.tools.megreimage.AppManager;
import com.flame.tools.megreimage.AppMediator;
import com.flame.tools.megreimage.consts.ImageStatusEnum;
import com.flame.tools.megreimage.consts.ProjectStatusEnum;
import com.flame.tools.megreimage.util.ImageUtil;
import com.flame.tools.megreimage.util.StringUtil;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.ProjectInfoVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.swingworker.BatchCutRectSwingWorker.java
 * @Description: 批量裁剪矩形图片
 * @Create: DerekWu  2016年4月22日 下午3:25:24 
 * @version: V1.0 
 */
public class BatchCutRectSwingWorker extends SwingWorker<ProjectInfoVO, Integer> {
	
	/** 裁剪图片的项目 */
	private ProjectInfoVO projectInfoVO;
	
	/** 裁剪的图片文件 */
	private List<ImageInfoVO> imageList;
	
	/** 进度条  */
	private ProgressMonitor progressMonitor; 

	/**
	 * 
	 */
	public BatchCutRectSwingWorker(ProjectInfoVO projectInfoVO, List<ImageInfoVO> imageList) { 
		this.projectInfoVO = projectInfoVO;
		this.imageList = imageList;
	}


	@Override
	protected ProjectInfoVO doInBackground() throws Exception {

		//开启进度条
		progressMonitor = new ProgressMonitor(AppMediator.getAppJFrame().getjFrame(), "正在进行矩形裁切运算...", "当前进度：0%", 0, imageList.size());
		
		//统计各种多边形的数量
		int fourNum = 0, fiveNum = 0, sixNum = 0, sevenNum = 0, eightNum = 0, exedNum = 0; 
		for (int i=0; i<imageList.size(); i++) {
			ImageInfoVO oneImageInfoVO = imageList.get(i);
			//只有还没有处理的图片才进行处理
			if (ImageStatusEnum.NO_EXE.equals(oneImageInfoVO.getStatus())) {
				//获得图片所在路径和存放路径 
				String imagePath = AppManager.getImagePath(projectInfoVO.getUseDirectory(), oneImageInfoVO.getImageName()); 
				String writePath = AppManager.getImageCutImagePath(oneImageInfoVO.getProjectName(), oneImageInfoVO.getImageName());
				//裁剪并写入磁盘，同时更新imageInfoVO 
				boolean exeSucc = ImageUtil.cutLessNoAlphaRectAndWriteToDisk(oneImageInfoVO, imagePath, writePath);
				if (!exeSucc) {
					//通知设置进度
					publish(i+1); 
					continue; 
				}
				//放入缓存
				AppManager.putImageInfoVOInCache(oneImageInfoVO); 
				//写入配置文件
				AppManager.writeOneImageConfig(oneImageInfoVO); 
			} 
			exedNum++;
			//统计多边形数量 
			int pointNum = oneImageInfoVO.getVertexNum(); 
			if (pointNum == 4) fourNum++;
			if (pointNum == 5) fiveNum++;
			if (pointNum == 6) sixNum++;
			if (pointNum == 7) sevenNum++;
			if (pointNum == 8) eightNum++;
			
			//Thread.sleep(200);
			
			//通知设置进度
			publish(i+1);  
			
		}
		//重新设置数量 
		projectInfoVO.setImageNum(imageList.size());
		projectInfoVO.setNoExedNum(imageList.size()-exedNum); 
		projectInfoVO.setExedNum(exedNum); 
		if (imageList.size() == exedNum) {
			projectInfoVO.setStatus(ProjectStatusEnum.CUT_RECT);  
		} else {
			projectInfoVO.setStatus(ProjectStatusEnum.NOT_CUT);  
		}
		projectInfoVO.setFourPolygonNum(fourNum); 
		projectInfoVO.setFivePolygonNum(fiveNum); 
		projectInfoVO.setSixPolygonNum(sixNum); 
		projectInfoVO.setSevenPolygonNum(sevenNum); 
		projectInfoVO.setEightPolygonNum(eightNum); 
		//写入项目配置文件
		AppManager.writeOneProjectConfig(projectInfoVO); 
		
		return projectInfoVO;
	}

	@Override 
	protected void process(List<Integer> chunks) { 
		for (int exeNum:chunks) {
			int rate = exeNum*100/imageList.size(); 
			progressMonitor.setNote(StringUtil.connectStr("当前进度：",rate,"%    ", exeNum,"/",imageList.size())); 
			progressMonitor.setProgress(exeNum); 
		}
    }
	
	@Override 
	protected void done() { 
		try {
			ProjectInfoVO projectInfoVO = get();
			AppMediator.oneProjectExeImportImage(projectInfoVO);
		} catch (Exception e) { 
			progressMonitor.close(); 
			e.printStackTrace();
		}
    }
	
}
