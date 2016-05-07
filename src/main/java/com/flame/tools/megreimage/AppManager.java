package com.flame.tools.megreimage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.flame.tools.megreimage.consts.ImageStatusEnum;
import com.flame.tools.megreimage.consts.ProjectStatusEnum;
import com.flame.tools.megreimage.consts.RectVertexPositionEnum;
import com.flame.tools.megreimage.swingworker.BatchCutRectSwingWorker;
import com.flame.tools.megreimage.swingworker.BatchPolygonExeSwingWorker;
import com.flame.tools.megreimage.swingworker.ImportImageSwingWorker;
import com.flame.tools.megreimage.util.AlgorithmUtil;
import com.flame.tools.megreimage.util.FileUtil;
import com.flame.tools.megreimage.util.GsonUtil;
import com.flame.tools.megreimage.util.ImageUtil;
import com.flame.tools.megreimage.util.StringUtil;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.MyPointVO;
import com.flame.tools.megreimage.vo.MyUVInfoVO;
import com.flame.tools.megreimage.vo.MyVertexVO;
import com.flame.tools.megreimage.vo.ProjectInfoVO;
import com.flame.tools.megreimage.vo.TPJsonObjVO;
import com.flame.tools.megreimage.vo.VertexImage;
import com.google.gson.reflect.TypeToken;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.AppManager.java
 * @Description: 管理者
 * @Create: DerekWu  2016年4月19日 下午5:21:39 
 * @version: V1.0 
 */
public class AppManager {

	private static Map<String, ProjectInfoVO> projectMap = new HashMap<String, ProjectInfoVO>();
	
	/** 项目名字列表  */
	private static List<String> projectList = new ArrayList<String>();
	
	/** 图片纹理Map */
	private static Map<String, Map<Short,ImageInfoVO>> imageMap = new HashMap<String, Map<Short,ImageInfoVO>>();

	/** 跟目录  */
	private static final String ROOT_DIR = System.getProperty("user.dir"); 
	
	/** 工作目录  */
	public static final String WORK_SPACES_DIR = ROOT_DIR + File.separator + "workspaces" + File.separator;
	
	/** 总配置文件  */
	private static final String APP_CONFIG = WORK_SPACES_DIR + "appConfig.json";
	
	/** 项目配置文件名  */
	private static final String PROJECT_CONFIG_NAME = "projectConfig.json";
	
	/** 图片多边形组合目录名，就是合图使用图片放置目录  */
	private static final String MEGRE_IMAGE_DIR = "megre";
	
	/** 项目发布路径  */
	private static final String PUBLISH_DIR = "publish";
			
	public static void initApp() {
		try {
			//打印系统配置参数 
			System.out.println("print config params:"); 
			System.out.println("TexturePacker_Cmd = "+ AppConfig.getProperty("TexturePacker_Cmd")); 
			System.out.println("ImageMagick_Cmd = "+ AppConfig.getProperty("ImageMagick_Cmd")); 
			System.out.println("pngquant_Cmd = "+ AppConfig.getProperty("pngquant_Cmd")); 
			System.out.println("cwebp_Cmd = "+ AppConfig.getProperty("cwebp_Cmd")); 
			
			//工作目录 ，没有则创建 
			File wsDir = new File(WORK_SPACES_DIR); 
			if (!wsDir.exists()) {
				wsDir.mkdir();
			} 
			
			//工作空间配置文件 
			File appConf = new File(APP_CONFIG);  
			if (appConf.exists()) { //存在则读取信息 到 projectList  
				String fileText = FileUtil.getFileText(APP_CONFIG); 
				Type type = new TypeToken<ArrayList<String>>() {}.getType(); 
				List<String> projectInConfList = GsonUtil.gson.fromJson(fileText, type); 
				//处理项目列表
				exeProjectList(projectInConfList); 
			}
			
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	}
	
	private static void exeProjectList(List<String> projectInConfList) { 
		for (String oneProjectName:projectInConfList) { 
			exeOneProject(oneProjectName); 
		}
	}
	
	/**
	 * 写如项目list配置文件
	 * @throws Exception
	 */
	private static void writeProjectListToAppConfig()  {  
		FileUtil.writeText(APP_CONFIG, GsonUtil.gson.toJson(projectList));
	}
	
	/**
	 * 写入项目配置文件
	 * @param projectInfoVO
	 */
	public static void writeOneProjectConfig(ProjectInfoVO projectInfoVO) {
		String projectConfigFilePath = WORK_SPACES_DIR + projectInfoVO.getName() + File.separator + PROJECT_CONFIG_NAME;
		FileUtil.writeText(projectConfigFilePath, GsonUtil.gson.toJson(projectInfoVO));  
	}
	
	
	private static void exeOneProject(String projectName) {
		//如果项目目录不存在，则抛弃 
		String projectDirPath = WORK_SPACES_DIR + projectName + File.separator;
		File projectDir = new File(projectDirPath);
		if (!projectDir.exists()) {
			return;
		}
		//如果项目配置文件不存在，也抛弃
		String projectConfigFilePath = projectDirPath + PROJECT_CONFIG_NAME + File.separator;
		File projectFile = new File(projectConfigFilePath);
		if (!projectFile.exists()) {
			return;
		}
		
		try {
			String fileText = FileUtil.getFileText(projectConfigFilePath);
			ProjectInfoVO oneProjectInfoVO = GsonUtil.gson.fromJson(fileText, ProjectInfoVO.class); 
			//添加到项目列表
			projectList.add(projectName); 
			//添加到项目map
			projectMap.put(projectName, oneProjectInfoVO); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static List<ProjectInfoVO> getProjectList() {
		List<ProjectInfoVO> retList = new ArrayList<ProjectInfoVO>();
		for (String oneProjectName:projectList) {
			ProjectInfoVO oneProjectInfoVO = projectMap.get(oneProjectName);
			if (oneProjectInfoVO != null) {
				retList.add(oneProjectInfoVO);
			}
		}
		return retList;
	}
	
	/**
	 * 验证项目名字 
	 * @param projectName
	 * @return 0=可以创建，1=重复的项目名字，2=工作空间中已经存在和项目名相同的目录！
	 */
	public static int checkProjectName(String projectName) {
		//验证是否存在 
		if (projectMap.containsKey(projectName)) {
			return 1;
		} 
		//验证项目目录 
		String projectDirPath = WORK_SPACES_DIR + projectName + File.separator;
		File projectDir = new File(projectDirPath);
		if (projectDir.exists()) {
			return 2;
		} 
		return 0;
	}
	
	
	/**
	 * 创建项目
	 * @param projectName
	 * @param imageDir
	 * @param exportFileName
	 */
	public static void createProject(String projectName, String imageDir, String exportFileName) {
		//初始化一个项目默认的size 
		ProjectInfoVO newProjectInfoVO = new ProjectInfoVO();
		newProjectInfoVO.setName(projectName);
		newProjectInfoVO.setStatus(ProjectStatusEnum.NOT_CUT);  
		newProjectInfoVO.setUseDirectory(imageDir);
		newProjectInfoVO.setFileName(exportFileName); 
		newProjectInfoVO.setMegreImageWidth(0);
		newProjectInfoVO.setMegreImageHeight(0);
		//图片总数量，这里的图片必须是数字图片名字
		List<Short> shortFileNameList = getFileShortNameList(imageDir);
		newProjectInfoVO.setImageNum(shortFileNameList.size());
		newProjectInfoVO.setNoExedNum(shortFileNameList.size());
		//设置多边形默认设置
		newProjectInfoVO.setPolygonImageSizeLimit(40);  
		newProjectInfoVO.setPolygonCutAreaLimit(648); 
		newProjectInfoVO.setPolygonCutBorderLengthLimit(18); 
		
		//添加到项目列表，并写入文件 
		projectList.add(projectName);
		projectMap.put(projectName, newProjectInfoVO); 
		writeProjectListToAppConfig();
		
		//创建项目目录 
		String projectDirPath = StringUtil.connectStr(WORK_SPACES_DIR, projectName, File.separator);
		File projectDir = new File(projectDirPath);
		projectDir.mkdir(); 
		//创建项目合并图片存放路径 
		String megreImagePath = getMegreImageDirPath(projectName); 
		File megreImageDir = new File(megreImagePath);
		megreImageDir.mkdir(); 
		//创建项目发布路径 
		String publishPath = getPublishDirPath(projectName);  
		File publishDir = new File(publishPath);
		publishDir.mkdir(); 
		
		//写入项目配置文件 
		writeOneProjectConfig(newProjectInfoVO);
		
		//通知新项目创建了 
		AppMediator.addProjectToTree(newProjectInfoVO);
	}
	
	/** 
     * 获取一个文件夹下的所有文件 要求：后缀名为png , 文件名为数字，并且小于 Short.MAX_VALUE 32767
     * @param dirPath 
     * @return 
     */  
    public static List<Short> getFileShortNameList(String dirPath) {  
        List<Short> shortFileNameList = new ArrayList<Short>();   
        // 内部匿名类，用来过滤文件类型  
        File[] fileList = getFilesByDir(dirPath);
        for (int i = 0; i < fileList.length; i++) {  
        	shortFileNameList.add(getImageShortName(fileList[i])); 
        }   
        return shortFileNameList;  
    }  
    
    /**
     * 获得图片的短名字
     * @param imageFile
     * @return
     */
    public static short getImageShortName(File imageFile) {
    	return Short.parseShort(imageFile.getName().split("\\.")[0]);
    }
    
    /** 
     * 获取一个文件夹下的所有文件 要求：后缀名为png , 文件名为数字，并且小于 Short.MAX_VALUE 32767 
     * @param dirPath 
     * @return 
     */  
    public static File[] getFilesByDir(String dirPath) {
    	File fileDir = new File(dirPath); 
    	if (!fileDir.isDirectory()) {  
            return new File[0];
        } else {
        	return fileDir.listFiles(new FileFilter() {  
                @Override
				public boolean accept(File file) {  
                    if (file.isFile()) {  
                    	String fileName = file.getName();
                    	if (fileName.indexOf(".") > 0 && fileName.indexOf("png") > 1) { //必须是png文件
                    		String[] fileSplitStr = fileName.split("\\.");
                    		if (fileSplitStr.length == 2 && fileSplitStr[0].matches("[0-9]+") && fileSplitStr[0].length()<6) { 
                    			if (Integer.parseInt(fileSplitStr[0]) <= Short.MAX_VALUE) {
                    				return true;  
                    			}
                    		}
                    	}
                    } 
                    return false;
                }  
            });  
        }
    }
    
    /**
     * 导入项目
     * @param projectName
     * @return 
     * 0=成功  
     * 1=该项目已经在工作空间，无需再次导入 
     * 2=您导入的项目所在目录不存在工作空间之内 
     * 3=项目配置文件不存在 
     * 4=项目配置的图片所在目录不存在 
     */
	public static int importProject(String projectName) {
		//验证是否存在
		if (projectMap.containsKey(projectName)) {
			return 1;
		} 
		//验证项目目录 
		String projectDirPath = WORK_SPACES_DIR + projectName + File.separator;
		File projectDir = new File(projectDirPath);
		if (!projectDir.exists() || !projectDir.isDirectory()) {
			return 2;
		} 
		//验证配置文件 
		String projectConfigFilePath = projectDirPath + PROJECT_CONFIG_NAME;
		File projectConfigFile = new File(projectConfigFilePath);
		if (!projectConfigFile.exists() || !projectConfigFile.isFile()) {
			return 3;
		} 
		ProjectInfoVO projectInfoVO = null;
		try {
			String fileText = FileUtil.getFileText(projectConfigFilePath); 
			projectInfoVO = GsonUtil.gson.fromJson(fileText, ProjectInfoVO.class);
			String useDirectoryPath = projectInfoVO.getUseDirectory();
			File useDirectory = new File(useDirectoryPath);
			if (!useDirectory.exists() || !useDirectory.isDirectory()) {
				return 4;
			}
		} catch (Exception e) {
			return 4;
		} 
		
		//添加到项目列表，并写入文件 
		projectList.add(projectName);
		projectMap.put(projectName, projectInfoVO); 
		writeProjectListToAppConfig();
		
		//通知新项目创建了 
		AppMediator.addProjectToTree(projectInfoVO); 
		
		//反正成功 
		return 0;
	}
    
    /**
     * 删除项目数据
     * @param projectInfoVO
     */
    public static void deleteProjectData(ProjectInfoVO projectInfoVO) {
    	projectMap.remove(projectInfoVO.getName());
    	projectList.remove(projectInfoVO.getName());
    	//写文件
    	writeProjectListToAppConfig();
    }
    
    /**
     * 获得ProjectInfoVO
     * @param projectName
     * @return
     */
    public static ProjectInfoVO getProjectInfoVO(String projectName) {
    	return projectMap.get(projectName);
    }
    
    /**
     * 导入图片纹理 
     * @param projectInfoVO
     */
    public static void importImage(ProjectInfoVO projectInfoVO) {
    	resetProjectImageMap(projectInfoVO.getName()); 
		//重新计算图片数量 
		File[] useDirFileArray = getFilesByDir(projectInfoVO.getUseDirectory());
		if (useDirFileArray.length == 0) { 
			String msg = "当前项目的图片所在目录下没有合格的png文件！\n目录："+projectInfoVO.getUseDirectory();
			String title = "提示信息";
			JOptionPane.showMessageDialog(AppMediator.getAppJFrame().getjFrame(), msg, title, JOptionPane.OK_OPTION); 
			return; 
		}
		
		ImportImageSwingWorker importImageWorker = new ImportImageSwingWorker(projectInfoVO, useDirFileArray);
		importImageWorker.execute(); 
    } 
    
    /**
     * 查找ImageInfoVO
     * @param projectInfoVO
     * @param imageName
     * @return
     */
    public static ImageInfoVO getImageInfoVOFromAny(ProjectInfoVO projectInfoVO, short imageName) {
    	ImageInfoVO oneImageInfoVO = null;
    	try {
    		oneImageInfoVO = getImageInfoVOFromCache(projectInfoVO.getName(), imageName);
    		if (oneImageInfoVO == null) {
    			String imageConfigPath = getImageConfigPath(projectInfoVO.getName(), imageName);
    			File oneImageConfigFile = new File(imageConfigPath);
    			if (oneImageConfigFile.exists() && oneImageConfigFile.isFile()) {
    				String fileText = FileUtil.getFileText(imageConfigPath);
    				oneImageInfoVO = GsonUtil.gson.fromJson(fileText, ImageInfoVO.class); 
    				if (oneImageInfoVO != null) {
    					oneImageInfoVO.setProjectName(projectInfoVO.getName());
    					putImageInfoVOInCache(oneImageInfoVO); 
        			}
    			}
    		}
		} catch (Exception e) { 
			e.printStackTrace();
		}
    	return oneImageInfoVO;
    }
    
    /**
     * 获得图片配置路径
     * @param projectName
     * @param imageName
     * @return
     */
    private static String getImageConfigPath(String projectName, short imageName) { 
    	return StringUtil.connectStr(WORK_SPACES_DIR, projectName, File.separator, imageName, "_info.json"); 
    }
    
    /**
     * 获得图片所在路径
     * @param projectName
     * @param imageName
     * @return
     */
    public static String getImagePath(String userDir, short imageName) { 
    	return StringUtil.connectStr(userDir, File.separator, imageName, ".png"); 
    }
    
    /**
     * 获得图片裁切后的存放路径
     * @param projectName
     * @param imageName
     * @return
     */
    public static String getImageCutImagePath(String projectName, short imageName) { 
    	return StringUtil.connectStr(WORK_SPACES_DIR, projectName, File.separator, imageName, "_cut.png"); 
    }
    
    /**
     * 获得图片合并存放路径
     * @param projectName
     * @param imageName
     * @return
     */
    public static String getImageMegreImagePath(String projectName, short imageName) { 
    	return StringUtil.connectStr(WORK_SPACES_DIR, projectName, File.separator, MEGRE_IMAGE_DIR, File.separator, imageName, ".png"); 
    }
    
    /**
     * 获得项目合图路径
     * @param projectName
     * @return
     */
    public static String getMegreImageDirPath(String projectName) { 
    	return StringUtil.connectStr(WORK_SPACES_DIR, projectName, File.separator, MEGRE_IMAGE_DIR, File.separator); 
    }
    
    /**
     * 获得项目发布路径 
     * @param projectName
     * @return
     */
    public static String getPublishDirPath(String projectName) { 
    	return StringUtil.connectStr(WORK_SPACES_DIR, projectName, File.separator, PUBLISH_DIR, File.separator); 
    }
    
    /**
     * 获得项目发布雪碧图路径
     * @param projectInfoVO
     * @return
     */
    public static String getPublishSheetImagePath(ProjectInfoVO projectInfoVO) { 
    	String publishPath = getPublishDirPath(projectInfoVO.getName());
    	return StringUtil.connectStr(publishPath, projectInfoVO.getFileName(), ".png");  
    }
    
    /**
     * 获得项目发布雪碧图压缩后的高清图路径
     * @param projectInfoVO
     * @return
     */
    public static String getPublishSheetImageHDPath(ProjectInfoVO projectInfoVO) { 
    	String publishPath = getPublishDirPath(projectInfoVO.getName());
    	return StringUtil.connectStr(publishPath, projectInfoVO.getFileName(), "_hd.png");  
    }
    
    /**
     * 获得项目发布json配置路径
     * @param projectInfoVO
     * @return
     */
    public static String getPublishJsonConfigPath(ProjectInfoVO projectInfoVO) {  
    	String publishPath = getPublishDirPath(projectInfoVO.getName());
    	return StringUtil.connectStr(publishPath, projectInfoVO.getFileName(), ".json");   
    }
    
    /**
     * 获得项目发布uv配置文件路径，uv配置文件是一个二进制文件，后缀名.aorf  烈焰时代 （age of roaring flame） 
     * @param projectInfoVO
     * @return
     */
    public static String getPublishUVConfigPath(ProjectInfoVO projectInfoVO) {  
    	String publishPath = getPublishDirPath(projectInfoVO.getName());
    	return StringUtil.connectStr(publishPath, projectInfoVO.getFileName(), ".aorf");   
    }
    
    /**
     * 获得项目发布uv配置文件路径的txt版本，用于测试 
     * @param projectInfoVO
     * @return
     */
    public static String getPublishUVConfigTextPath(ProjectInfoVO projectInfoVO) {  
    	String publishPath = getPublishDirPath(projectInfoVO.getName());
    	return StringUtil.connectStr(publishPath, projectInfoVO.getFileName(), "_aorf.txt");   
    }
    
    /**
     * 获得项目发布小雪碧图路径
     * @param projectInfoVO
     * @return
     */
    public static String getPublishSmallSheetImagePath(ProjectInfoVO projectInfoVO) {  
    	String publishPath = getPublishDirPath(projectInfoVO.getName());
    	return StringUtil.connectStr(publishPath, projectInfoVO.getFileName(), "_s.png");  
    }
    
    /**
     * 获得项目发布雪碧图webp路径
     * @param projectInfoVO
     * @return
     */
    public static String getPublishSheetWebpPath(ProjectInfoVO projectInfoVO) { 
    	String publishPath = getPublishDirPath(projectInfoVO.getName());
    	return StringUtil.connectStr(publishPath, projectInfoVO.getFileName(), "_hd.webp");  
    }
    
    /**
     * 获得项目发布小雪碧图webp路径
     * @param projectInfoVO
     * @return
     */
    public static String getPublishSmallSheetWebpPath(ProjectInfoVO projectInfoVO) { 
    	String publishPath = getPublishDirPath(projectInfoVO.getName());
    	return StringUtil.connectStr(publishPath, projectInfoVO.getFileName(), "_sd.webp");  
    }
    
    /**
     * 缓存中获得ImageInfo
     * @param projectName
     * @param imageName
     * @return
     */
    public static ImageInfoVO getImageInfoVOFromCache(String projectName, short imageName) {
    	Map<Short, ImageInfoVO> projectImageMap = getProjectImageMap(projectName); 
    	return projectImageMap.get(imageName); 
    }
    
    private static Map<Short, ImageInfoVO> getProjectImageMap(String projectName) {
    	Map<Short, ImageInfoVO> projectImageMap = imageMap.get(projectName);
    	if (projectImageMap == null) {
    		projectImageMap = new HashMap<Short, ImageInfoVO>();
    		imageMap.put(projectName, projectImageMap);
    	}
    	return projectImageMap;
    }
    
    private static void resetProjectImageMap(String projectName) {
    	Map<Short, ImageInfoVO> projectImageMap = new HashMap<Short, ImageInfoVO>();
    	imageMap.put(projectName, projectImageMap);
    } 
    
    /**
     * ImageInfo放入缓存 
     * @param projectName
     * @param imageInfoVO
     */
    public static void putImageInfoVOInCache(ImageInfoVO imageInfoVO) {
    	Map<Short, ImageInfoVO> projectImageMap = getProjectImageMap(imageInfoVO.getProjectName()); 
    	projectImageMap.put(imageInfoVO.getImageName(), imageInfoVO);
    }
	
    /**
	 * 写入image配置文件
	 * @param projectInfoVO
	 */
	public static void writeOneImageConfig(ImageInfoVO imageInfoVO) {
		String imageConfigPath = getImageConfigPath(imageInfoVO.getProjectName(), imageInfoVO.getImageName());
		FileUtil.writeText(imageConfigPath, GsonUtil.gson.toJson(imageInfoVO));   
	}
	
	/**
	 * 通过项目名获得image列表，排序是已处理，顶点数，面积大优先
	 * @param projectName
	 * @return
	 */
	public static List<ImageInfoVO> getImageInfoListByProjectName(String projectName) {
		List<ImageInfoVO> imageList = new ArrayList<ImageInfoVO>();
		Map<Short, ImageInfoVO> projectImageMap = getProjectImageMap(projectName); 
		for (Map.Entry<Short, ImageInfoVO> imageEntry:projectImageMap.entrySet()) {
			imageList.add(imageEntry.getValue());
		}
		Collections.sort(imageList, new Comparator<ImageInfoVO>() {
			@Override
			public int compare(ImageInfoVO o1, ImageInfoVO o2) {
				if (o1.getStatus().equals(o2.getStatus())) { 
					if (o1.getVertexNum() == o2.getVertexNum()) { 
						return o2.getCutArea()-o1.getCutArea();
					} else {
						return o2.getVertexNum()-o1.getVertexNum();  
					}
                } else {
                    return o2.getStatus().getCode()-o1.getStatus().getCode(); 
                }
			}
		}); 
		return imageList;
	}
	
	/**
	 * 批量矩形运算 
	 * @param projectInfoVO
	 */
	public static void batchCutRect(ProjectInfoVO projectInfoVO) {
		List<ImageInfoVO> imageList = getImageInfoListByProjectName(projectInfoVO.getName());
		if (imageList.size() == 0) {
			String msg = "当前项目 ["+projectInfoVO.getName()+"] 没有可裁切的图片！";
			String title = "提示信息";
			JOptionPane.showMessageDialog(AppMediator.getAppJFrame().getjFrame(), msg, title, JOptionPane.OK_OPTION);
			return; 
		}
		
		BatchCutRectSwingWorker batchCutRectSwingWorker = new BatchCutRectSwingWorker(projectInfoVO, imageList);
		batchCutRectSwingWorker.execute(); 
	}
	
	/**
	 * 批量多边形运算 
	 * @param projectInfoVO
	 */
	public static void batchPolygonExe(ProjectInfoVO projectInfoVO) {
		if (ProjectStatusEnum.NOT_CUT.equals(projectInfoVO.getStatus())) { 
			String msg = "当前项目 ["+projectInfoVO.getName()+"] 还没有进行“批量矩形裁切”，不能进行批量多边形运算！";
			String title = "提示信息";
			JOptionPane.showMessageDialog(AppMediator.getAppJFrame().getjFrame(), msg, title, JOptionPane.OK_OPTION);
			return; 
		}
		
		List<ImageInfoVO> imageList = getImageInfoListByProjectName(projectInfoVO.getName());
		if (imageList.size() == 0) {
			String msg = "当前项目 ["+projectInfoVO.getName()+"] 没有可进行多边形运算的图片！";
			String title = "提示信息";
			JOptionPane.showMessageDialog(AppMediator.getAppJFrame().getjFrame(), msg, title, JOptionPane.OK_OPTION);
			return; 
		}
		//System.out.println("333333---------"); 
		BatchPolygonExeSwingWorker batchPolygonExeSwingWorker = new BatchPolygonExeSwingWorker(projectInfoVO, imageList);
		batchPolygonExeSwingWorker.execute(); 
	}
	
	/**
	 * 获得原图 
	 * @param imageInfoVO
	 * @return
	 */
	public static BufferedImage getBaseImage(ImageInfoVO imageInfoVO) {
		ProjectInfoVO oneProjectInfoVO = getProjectInfoVO(imageInfoVO.getProjectName());
		if (oneProjectInfoVO == null) return null;
		String imagePath = getImagePath(oneProjectInfoVO.getUseDirectory(), imageInfoVO.getImageName());
		try {
			File imageFile = new File(imagePath);
			if (!imageFile.exists()) return null;
			return ImageIO.read(imageFile);
		} catch (IOException e) {  
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得裁剪矩形图
	 * @param imageInfoVO
	 * @return
	 */
	public static BufferedImage getCutRectImage(ImageInfoVO imageInfoVO) {
		try {
			if (ImageStatusEnum.NO_EXE.equals(imageInfoVO.getStatus())) return null;  
			String cutRectImagePath = getImageCutImagePath(imageInfoVO.getProjectName(), imageInfoVO.getImageName());
			File cutRectImageFile = new File(cutRectImagePath);
			if (!cutRectImageFile.exists()) return null;
			return ImageIO.read(cutRectImageFile); 
		} catch (IOException e) {  
			e.printStackTrace(); 
		}
		return null;
	}
	
	/**
	 * 获得裁剪示意图
	 * @param imageInfoVO
	 * @return
	 */
	public static BufferedImage getCutViewImage(ImageInfoVO imageInfoVO, BufferedImage baseImage) {
		try {
			if (ImageStatusEnum.NO_EXE.equals(imageInfoVO.getStatus())) return null;  
			//创建一个新的图片 
			BufferedImage cutViewImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = cutViewImage.createGraphics();
			g2.drawImage(baseImage, null, null);
			
			//获得原图中心点 
			MyPointVO baseImageCenter = new MyPointVO(baseImage.getWidth()/2, baseImage.getHeight()/2);
			
			//绘制矩形区域 示意框 
			List<MyPointVO> cutPoints = imageInfoVO.getRectMyPointVOList();
			if (cutPoints != null && cutPoints.size() > 0) { 
				Polygon onePolygon = new Polygon(); 
				for (MyPointVO oneMyPointVO:cutPoints) {
					oneMyPointVO.addPoint(baseImageCenter); 
					onePolygon.addPoint(oneMyPointVO.getX(), oneMyPointVO.getY());
				}
				g2.setColor(Color.RED); //红色框 
				g2.drawPolygon(onePolygon); 
			}

			//绘制多边形区域示意框
			List<MyPointVO> cutPolygonPoints = imageInfoVO.getPolygonMyPointVOList();
			if (cutPolygonPoints != null && cutPolygonPoints.size() > 0) {	
				Polygon onePolygon = new Polygon(); 
				for (MyPointVO oneMyPointVO:cutPolygonPoints) { 
					oneMyPointVO.addPoint(baseImageCenter); 
					onePolygon.addPoint(oneMyPointVO.getX(), oneMyPointVO.getY());
				}
				g2.setColor(Color.BLUE); //蓝色框 
				g2.drawPolygon(onePolygon); 
			}
			g2.dispose(); 
			
			return cutViewImage;  
			
		} catch (Exception e) {  
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得合并示意图
	 * @param imageInfoVO
	 * @return
	 */
	public static BufferedImage getMegreViewImage(ImageInfoVO imageInfoVO) {
		try {
			if (ImageStatusEnum.NO_EXE.equals(imageInfoVO.getStatus())) return null;  
			
			//显示的图片名，如果是子图片则显示父图片 
			short viewImageName; 
			//定义父级图片 
			ImageInfoVO parentImageInfoVO = null;
			if (imageInfoVO.getParentImageName() > 0) { 
				viewImageName = imageInfoVO.getParentImageName();  
				parentImageInfoVO = getImageInfoVOFromCache(imageInfoVO.getProjectName(), imageInfoVO.getParentImageName());
				if (parentImageInfoVO.getParentImageName() > 0) { 
					viewImageName = parentImageInfoVO.getParentImageName(); 
				}
			} else {
				viewImageName = imageInfoVO.getImageName();
			}
			
			String megreImagePath = getImageMegreImagePath(imageInfoVO.getProjectName(), viewImageName); 
			File megreImageFile = new File(megreImagePath);
			if (!megreImageFile.exists()) return null;
			BufferedImage megreImage = ImageIO.read(megreImageFile);  
			
			//创建一个新的图片,将合并图片放入  
			BufferedImage megreViewImage = new BufferedImage(megreImage.getWidth(), megreImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = megreViewImage.createGraphics();
			g2.drawImage(megreImage, null, null);
			
			List<MyPointVO> megreUsePoints = genImageRealPoints(imageInfoVO, parentImageInfoVO); 
			
			//System.out.println("==================="+addParentPoint); 
			
			//绘制多边形区域示意框
			Polygon onePolygon = new Polygon();  
			for (MyPointVO oneMyPointVO:megreUsePoints) {
				//System.out.println("point=="+oneMyPointVO); 
				onePolygon.addPoint(oneMyPointVO.getX(), oneMyPointVO.getY()); 
			}
			g2.setColor(Color.BLUE); //蓝色框 
			g2.drawPolygon(onePolygon); 
			g2.dispose(); 
			
			return megreViewImage;   
			
		} catch (Exception e) {  
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生成uv坐标 
	 * @param imageInfoVO
	 * @param uvMaxWidth
	 * @param uvMaxHeight
	 * @param imageJsonMap
	 * @throws Exception
	 */
	public static void genImageUvInfo(ImageInfoVO imageInfoVO, int uvMaxWidth, int uvMaxHeight, Map<Short, TPJsonObjVO> imageJsonMap) throws Exception {
		//显示的图片名，如果是子图片则显示父图片 
		short viewImageName; 
		//定义父级图片 
		ImageInfoVO parentImageInfoVO = null;
		if (imageInfoVO.getParentImageName() > 0) { 
			viewImageName = imageInfoVO.getParentImageName();  
			parentImageInfoVO = getImageInfoVOFromCache(imageInfoVO.getProjectName(), imageInfoVO.getParentImageName());
			if (parentImageInfoVO.getParentImageName() > 0) { 
				viewImageName = parentImageInfoVO.getParentImageName(); 
			}
		} else {
			viewImageName = imageInfoVO.getImageName();
		}
		
		TPJsonObjVO exeImageJsonObj = imageJsonMap.get(viewImageName);
		if (exeImageJsonObj == null) {
			throw new Exception("image "+ viewImageName + " is not in json config....");
		}
		
		//获得合图使用坐标 
		List<MyPointVO> megreUsePoints = genImageRealPoints(imageInfoVO, parentImageInfoVO);
		
		//增加显示位置的坐标
		MyPointVO addPoint = new MyPointVO(exeImageJsonObj.getX(), exeImageJsonObj.getY());
		for (MyPointVO oneMyPointVO:megreUsePoints) { 
			oneMyPointVO.addPoint(addPoint); 
		}
		
		//如果旋转了还要转换为旋转后的坐标 
		if (exeImageJsonObj.isRotated()) {
			for (MyPointVO oneMyPointVO:megreUsePoints) {
				MyPointVO newRatorePoint = AlgorithmUtil.getRotatePoint(oneMyPointVO.getX(), oneMyPointVO.getY(), addPoint.getX(), addPoint.getY(), 90); 
				oneMyPointVO.setX(newRatorePoint.getX());
				oneMyPointVO.setY(newRatorePoint.getY());
			}
			//再右移图片高度 
			for (MyPointVO oneMyPointVO:megreUsePoints) {
				oneMyPointVO.setX(oneMyPointVO.getX()+exeImageJsonObj.getHeight()-1);    
			}
		}
		
		//计算uv坐标
		List<MyUVInfoVO> uvInfos = new ArrayList<MyUVInfoVO>();
		for (MyPointVO oneMyPointVO:megreUsePoints) {
			float s = (oneMyPointVO.getX()+0.5f)/uvMaxWidth; 
			float t = 1f-(oneMyPointVO.getY()+0.5f)/uvMaxHeight; //反转y坐标 
			MyUVInfoVO oneMyUVInfoVO = new MyUVInfoVO(s, t);
			uvInfos.add(oneMyUVInfoVO); 
		}
		
		//获得图片对应的uv坐标 
		List<MyUVInfoVO> imageUVInfos = imageInfoVO.getMegreUseMyUVinfoVOList();
		if (uvInfos.size() != imageUVInfos.size()) {
			throw new Exception("image uv nums !=  point nums.......");
		}
		
		//重设Uv
		for (int i=0; i<uvInfos.size(); i++) { 
			MyUVInfoVO newUv = uvInfos.get(i);
			MyUVInfoVO oldUv = imageUVInfos.get(i);
			oldUv.setS(newUv.getS());
			oldUv.setT(newUv.getT()); 
		} 
		
	}

	/**
	 * @param imageInfoVO
	 * @param parentImageInfoVO
	 * @return
	 */
	private static List<MyPointVO> genImageRealPoints(ImageInfoVO imageInfoVO, ImageInfoVO parentImageInfoVO) {
		//增加父级图片中的坐标
		MyPointVO addParentPoint = null; 
		//顶点图片对象，只有子图片才有
		VertexImage oneVertexImage = getPointInParent(imageInfoVO); 
		if (oneVertexImage != null) {
			addParentPoint = oneVertexImage.getInParentPoint(); 
		}
		
		//增加在父级图片在他父级图片中的坐标
		MyPointVO addParentParentPoint = null; 
		VertexImage oneParentVertexImage = null;
		if (parentImageInfoVO != null) {
			//顶点图片对象，只有子图片才有
			oneParentVertexImage = getPointInParent(parentImageInfoVO); 
			if (oneParentVertexImage != null) {
				addParentParentPoint = oneParentVertexImage.getInParentPoint(); 
			}
		}
		
		
		//MyPointVO baseImageCenter = new MyPointVO(megreImage.getWidth()/2, megreImage.getHeight()/2); 
		//获得裁切的中心点 
		//MyPointVO cutCenterPoint = imageInfoVO.getCutCenterPoint();
		//加上所在父级
		
		//合图使用坐标 
		List<MyPointVO> megreUsePoints = imageInfoVO.getMegreUseMyPointVOList();  
		
		//准备绘制的顶点集合,已经废弃  
//			List<MyPointVO> drawPointList = new ArrayList<MyPointVO>();
//			for (MyPointVO oneMyPointVO:megreUsePoint) {
//				MyPointVO realPoint = MyPointVO.addPoint(oneMyPointVO, cutCenterPoint);
//				drawPointList.add(realPoint);  
//			} 
		
		//如果有选装，则转动,已经废弃  
//			if (imageInfoVO.getCutAngel() > 0) {
//				for (MyPointVO oneMyPointVO:megreUsePoints) {
//					MyPointVO newRatorePoint = AlgorithmUtil.getRotatePoint(oneMyPointVO.getX(), oneMyPointVO.getY(), cutCenterPoint.getX(), cutCenterPoint.getY(), imageInfoVO.getCutAngel());
//					oneMyPointVO.setX(newRatorePoint.getX());
//					oneMyPointVO.setY(newRatorePoint.getY());
//				}
//			}
		
		//如果有父级，则处理父级坐标,先加，后旋转 
		if (addParentPoint != null) {
			for (MyPointVO oneMyPointVO:megreUsePoints) {
				oneMyPointVO.addPoint(addParentPoint);  
			}
			//如果有旋转,则先旋转，再右移图片高度 
			if (oneVertexImage.isRotare()) { 
				for (MyPointVO oneMyPointVO:megreUsePoints) {
					MyPointVO newRatorePoint = AlgorithmUtil.getRotatePoint(oneMyPointVO.getX(), oneMyPointVO.getY(), addParentPoint.getX(), addParentPoint.getY(), 90); 
					oneMyPointVO.setX(newRatorePoint.getX());
					oneMyPointVO.setY(newRatorePoint.getY());
				}
				//再右移图片高度 
				for (MyPointVO oneMyPointVO:megreUsePoints) {
					oneMyPointVO.setX(oneMyPointVO.getX()+imageInfoVO.getCutHeight()-1);   
				}
			}
		} 
		
		//如果父级还有有父级，则处理父级坐标,先加，后旋转 
		if (addParentParentPoint != null) {
			for (MyPointVO oneMyPointVO:megreUsePoints) { 
				oneMyPointVO.addPoint(addParentParentPoint);  
			}
			//如果有旋转,则先旋转，再右移图片高度 
			if (oneParentVertexImage.isRotare()) { 
				for (MyPointVO oneMyPointVO:megreUsePoints) {
					MyPointVO newRatorePoint = AlgorithmUtil.getRotatePoint(oneMyPointVO.getX(), oneMyPointVO.getY(), addParentParentPoint.getX(), addParentParentPoint.getY(), 90); 
					oneMyPointVO.setX(newRatorePoint.getX());
					oneMyPointVO.setY(newRatorePoint.getY());
				}
				//再右移图片高度 
				for (MyPointVO oneMyPointVO:megreUsePoints) {
					oneMyPointVO.setX(oneMyPointVO.getX()+parentImageInfoVO.getCutHeight()-1);   
				}
			}
		}
		return megreUsePoints;
	}
	
	/**
	 * 获得在父级中的坐标，没有则返回0，0 
	 * @param imageInfoVO
	 * @return
	 */
	private static VertexImage getPointInParent(ImageInfoVO imageInfoVO) { 
		if (imageInfoVO.getParentImageName() > 0) { 
			ImageInfoVO parentImageInfo = getImageInfoVOFromCache(imageInfoVO.getProjectName(), imageInfoVO.getParentImageName());
			RectVertexPositionEnum inParentPosition = imageInfoVO.getParentPosition();
			return parentImageInfo.getOneSubImage(imageInfoVO.getImageName(), inParentPosition); 
		} else {
			return null;
		}
	}
	
	/**
	 * 通过项目名获得image顶点裁切的三角形列表，排序是可放矩形面积大,斜率大优先 
	 * @param projectName
	 * @return
	 */
	public static List<MyVertexVO> getImageVertexInfoListByProjectName(String projectName) {
		List<MyVertexVO> imageVertexList = new ArrayList<MyVertexVO>();
		Map<Short, ImageInfoVO> projectImageMap = getProjectImageMap(projectName); 
		for (Map.Entry<Short, ImageInfoVO> imageEntry:projectImageMap.entrySet()) { 
			imageVertexList.addAll(imageEntry.getValue().getPolygonVertexList()); 
		}
		//清空锁包含的图片,要开始重新计算 
		for (MyVertexVO oneMyVertexVO:imageVertexList) { 
			oneMyVertexVO.setSubImages(null); 
		}
		Collections.sort(imageVertexList, new Comparator<MyVertexVO>() { 
			@Override
			public int compare(MyVertexVO o1, MyVertexVO o2) {
				if (o1.getContainMaxRectArea() == o2.getContainMaxRectArea()) { 
					return o2.getSlope() > o1.getSlope()?1:-1;  
                } else {
                    return o2.getContainMaxRectArea() - o1.getContainMaxRectArea(); 
                }
			}
			
		});
		return imageVertexList;
	}
	
	/**
	 * 通过项目名获得不超过指定面积的image列表，排序是可放矩形面积大,斜率大优先 
	 * @param projectName
	 * @param areaMax
	 * @return
	 */
	public static List<ImageInfoVO> getImageInfoListByProjectName(String projectName, int areaMax) {
		List<ImageInfoVO> imageList = new ArrayList<ImageInfoVO>();
		Map<Short, ImageInfoVO> projectImageMap = getProjectImageMap(projectName); 
		for (Map.Entry<Short, ImageInfoVO> imageEntry:projectImageMap.entrySet()) { 
			ImageInfoVO oneImageInfoVO = imageEntry.getValue();
			//去除父级图片，要重新运算了
			oneImageInfoVO.setParentImageName((short)0);  
			if (oneImageInfoVO.getCutArea() <= areaMax) {
				imageList.add(oneImageInfoVO);  
			}
		}
		Collections.sort(imageList, new Comparator<ImageInfoVO>() { 
			@Override
			public int compare(ImageInfoVO o1, ImageInfoVO o2) {
				if (o1.getCutArea() == o2.getCutArea()) { 
					return o2.getCutSlope() > o1.getCutSlope()?1:-1;   
                } else {
                    return o2.getCutArea() - o1.getCutArea(); 
                }
			}
			
		});
		return imageList;
	}
	
	/**
	 * 将一个图片写入合图目录 
	 * @param oneImageInfoVO
	 */
	public static void writeImageToMegreDir(ImageInfoVO oneImageInfoVO) {
		try {
			//图片裁剪后存放路径
			String imageCutPath = getImageCutImagePath(oneImageInfoVO.getProjectName(), oneImageInfoVO.getImageName());
			//图片合并图片后存放路径 
			String imageMegrePath = getImageMegreImagePath(oneImageInfoVO.getProjectName(), oneImageInfoVO.getImageName());
			BufferedImage src = ImageIO.read(new File(imageCutPath)); 
			ImageIO.write(src, "png", new File(imageMegrePath)); 
		} catch (IOException e) { 
			e.printStackTrace();
		} 
	}
	
	/**
	 * 将一个父级图片写入合图目录 
	 * @param oneImageInfoVO 
	 * @param allSubImageList
	 */
	public static void writeParentImageToMegreDir(ImageInfoVO oneImageInfoVO, List<VertexImage> allSubImageList) {
		try {
			//图片裁剪后存放路径
			String imageCutPath = getImageCutImagePath(oneImageInfoVO.getProjectName(), oneImageInfoVO.getImageName());
			//图片合并图片后存放路径 
			String imageMegrePath = getImageMegreImagePath(oneImageInfoVO.getProjectName(), oneImageInfoVO.getImageName());
			//父图片 
			BufferedImage parentImage = ImageIO.read(new File(imageCutPath)); 
			for (VertexImage oneVertexImage:allSubImageList) { 
				//子图片裁剪后存放路径
				String subImageCutPath = getImageCutImagePath(oneImageInfoVO.getProjectName(), oneVertexImage.getImageName());
				//子图片 
				BufferedImage subImage = ImageIO.read(new File(subImageCutPath));  
				
				//字图片中是否还有子图片，有则先处理子图片  
				ImageInfoVO subImageInfoVO = getImageInfoVOFromCache(oneImageInfoVO.getProjectName(), oneVertexImage.getImageName());
				List<VertexImage> subImageVertexImageList = subImageInfoVO.getAllSubImageList();
				for (VertexImage subImageVertex:subImageVertexImageList) {
					//子图片中子图片裁剪后存放路径
					String subSubImageCutPath = getImageCutImagePath(oneImageInfoVO.getProjectName(), subImageVertex.getImageName());
					//子图片 
					BufferedImage subSubImage = ImageIO.read(new File(subSubImageCutPath));  
					subImage = ImageUtil.writeSubImage(subImage, subSubImage, subImageVertex.getInParentPoint().getX(), subImageVertex.getInParentPoint().getY(), subImageVertex.isRotare());
				}
				//System.out.println("---"+oneVertexImage.getImageName() +" x="+oneVertexImage.getInParentPoint().getX()+" y="+oneVertexImage.getInParentPoint().getY()+" isRotare="+oneVertexImage.isRotare()); 
				//写入子图片  
				parentImage = ImageUtil.writeSubImage(parentImage, subImage, oneVertexImage.getInParentPoint().getX(), oneVertexImage.getInParentPoint().getY(), oneVertexImage.isRotare());
			}
			ImageIO.write(parentImage, "png", new File(imageMegrePath));
		} catch (IOException e) { 
			e.printStackTrace();
		} 
	}
	
	/**
	 * 清空合图目录
	 * @param projectName
	 */
	public static void clearMegreImageDir(String projectName) {
		String megreImageDirPath = getMegreImageDirPath(projectName);
		File megreImageDir = new File(megreImageDirPath); 
		if (megreImageDir.exists()) {
			File[] files = megreImageDir.listFiles();
			for (File oneFile:files) { 
				oneFile.delete();
			}
		} else {
			megreImageDir.mkdir();
		}
	}
	
	
}
