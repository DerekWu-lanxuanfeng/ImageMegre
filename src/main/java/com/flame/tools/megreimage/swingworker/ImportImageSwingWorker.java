package com.flame.tools.megreimage.swingworker;

import java.io.File;
import java.util.List;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import com.flame.tools.megreimage.AppManager;
import com.flame.tools.megreimage.AppMediator;
import com.flame.tools.megreimage.consts.ImageStatusEnum;
import com.flame.tools.megreimage.consts.ProjectStatusEnum;
import com.flame.tools.megreimage.util.FileUtil;
import com.flame.tools.megreimage.util.StringUtil;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.ProjectInfoVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.swingworker.ImportImageSwingWorker.java
 * @Description: 导入图片的线程
 * @Create: DerekWu  2016年4月22日 下午2:38:43 
 * @version: V1.0 
 */
public class ImportImageSwingWorker extends SwingWorker<ProjectInfoVO, Integer> {


	/** 导入图片的项目 */
	private ProjectInfoVO projectInfoVO;
	
	/** 导入图片文件数组 */
	private File[] useDirFileArray;
	
	/** 导入图片的进度条  */
	private ProgressMonitor progressMonitor; 
	
	public ImportImageSwingWorker(ProjectInfoVO projectInfoVO, File[] useDirFileArray) {
		this.projectInfoVO = projectInfoVO;
		this.useDirFileArray = useDirFileArray; 
	}

	@Override
	protected ProjectInfoVO doInBackground() throws Exception {
		try {
			
			//开启进度条
			progressMonitor = new ProgressMonitor(AppMediator.getAppJFrame().getjFrame(), "正在导入图片纹理...", "当前进度：0%", 0, useDirFileArray.length); 
			
			int noExedNum = 0; //没有处理的个数 
			for (int i=0; i<useDirFileArray.length;i++) { 
				String fileMd5 = FileUtil.getFileMD5(useDirFileArray[i]); //获得文件md5码用来比对
				short imageName = AppManager.getImageShortName(useDirFileArray[i]); 
				ImageInfoVO imageInfoVO = AppManager.getImageInfoVOFromAny(projectInfoVO, imageName); 
				//是否是新的，新的则重新创建配置文件 
				boolean isNew = false;
				if (imageInfoVO == null) { 
					isNew = true;
				} else { 
					if (!imageInfoVO.getMd5Code().equals(fileMd5)) { 
						isNew = true;
					}
				}
				//新的则重建
				if (isNew) { 
					imageInfoVO = new ImageInfoVO();
					imageInfoVO.setImageName(imageName);
					imageInfoVO.setStatus(ImageStatusEnum.NO_EXE);
					imageInfoVO.setProjectName(projectInfoVO.getName());
					imageInfoVO.setMd5Code(fileMd5);
					//放入缓存
					AppManager.putImageInfoVOInCache(imageInfoVO); 
					//写入配置文件
					AppManager.writeOneImageConfig(imageInfoVO); 
				} 
				//统计未处理 
				if (ImageStatusEnum.NO_EXE.equals(imageInfoVO.getStatus())) {
					noExedNum++;
				}
				
				//Thread.sleep(200);
				
				//通知设置进度
				publish(i+1); 
				
			}
			//重新设置数量 
			projectInfoVO.setImageNum(useDirFileArray.length);
			projectInfoVO.setNoExedNum(noExedNum);
			projectInfoVO.setExedNum(useDirFileArray.length - noExedNum); 
			if (noExedNum > 0) {
				projectInfoVO.setStatus(ProjectStatusEnum.NOT_CUT); 
			}
			//写入项目配置文件
			AppManager.writeOneProjectConfig(projectInfoVO); 
			
			return projectInfoVO;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override 
	protected void process(List<Integer> chunks) {  
		for (int exeNum:chunks) {
			int rate = exeNum*100/useDirFileArray.length;
			progressMonitor.setNote(StringUtil.connectStr("当前进度：",rate,"%    ", exeNum,"/",useDirFileArray.length)); 
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
