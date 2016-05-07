package com.flame.tools.megreimage.swingworker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import com.flame.tools.megreimage.AppConfig;
import com.flame.tools.megreimage.AppManager;
import com.flame.tools.megreimage.AppMediator;
import com.flame.tools.megreimage.util.ExeCommandUtil;
import com.flame.tools.megreimage.util.FileUtil;
import com.flame.tools.megreimage.util.GsonUtil;
import com.flame.tools.megreimage.util.StringUtil;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.MyPointVO;
import com.flame.tools.megreimage.vo.MyUVInfoVO;
import com.flame.tools.megreimage.vo.ProjectInfoVO;
import com.flame.tools.megreimage.vo.TPJsonObjVO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.swingworker.MegreAndPublishSwingWorker.java
 * @Description: 合并图片 和 发布项目 
 * @Create: DerekWu  2016年4月26日 下午8:32:17 
 * @version: V1.0 
 */
public class MegreAndPublishSwingWorker extends SwingWorker<ProjectInfoVO, Integer>{ 
	
	/** 项目 */
	private ProjectInfoVO projectInfoVO;

	/** webp-q */
	private int webpQuality;
	
	/** webp-alpha-q */
	private int webpAlphaQuality;
	
	/** pngquant-q-min */ 
	private int pngquantQualityMin;
	
	/** pngquant-q-max */ 
	private int pngquantQualityMax;
	
	/** 图片文件 */
	private List<ImageInfoVO> imageList;
	
	/** 进度条  */
	private ProgressMonitor progressMonitor; 
	
	/** 进度描述 */
	private String noteDesc;
	
	/** 进度总数  */
	private int progressMaximum;

	/**
	 * @param projectInfoVO
	 * @param webpQuality
	 * @param webpAlphaQuality
	 * @param pngquantQualityMin
	 * @param pngquantQualityMax
	 */
	public MegreAndPublishSwingWorker(ProjectInfoVO projectInfoVO, int webpQuality, int webpAlphaQuality, int pngquantQualityMin, int pngquantQualityMax, List<ImageInfoVO> imageList) {
		this.projectInfoVO = projectInfoVO;
		this.webpQuality = webpQuality;
		this.webpAlphaQuality = webpAlphaQuality;
		this.pngquantQualityMin = pngquantQualityMin;
		this.pngquantQualityMax = pngquantQualityMax;
		this.imageList = imageList;
	}

	@Override
	protected ProjectInfoVO doInBackground() throws Exception { 
		this.progressMaximum = imageList.size()+1; 
		//开启进度条
		progressMonitor = new ProgressMonitor(AppMediator.getAppJFrame().getjFrame(), "正在合并发布...", "合并纹理中...", 0, progressMaximum); 
		
		progressMonitor.setMillisToDecideToPopup(100);
		progressMonitor.setMillisToPopup(10);
		
		//用TexturePacker合成雪碧图
		this.noteDesc = "合成雪碧图：";
		publish(1); 
		String imageDir = AppManager.getMegreImageDirPath(this.projectInfoVO.getName()); 
		String publishPath = AppManager.getPublishDirPath(this.projectInfoVO.getName());
		File publishDir = new File(publishPath); 
		if (!publishDir.exists()) publishDir.mkdir();
		String publishConfName = this.projectInfoVO.getFileName()+".json";  
		String publishSheetName = this.projectInfoVO.getFileName()+".png";   
		String texturePackerCommandStr = this.genTexturePackerCommandStr(imageDir, "json", publishPath, publishConfName, publishSheetName, "png", "RGBA8888");
		ExeCommandUtil.exeCommand(texturePackerCommandStr);
		
		//用ImageMagick生成小雪碧图 
		this.noteDesc = "生成小雪碧图：";
		publish(2); 
		//获得图片路径
		String publishSheetImagePath = AppManager.getPublishSheetImagePath(projectInfoVO);
		String publishSmallSheetImagePath = AppManager.getPublishSmallSheetImagePath(projectInfoVO);
		//获得图片 
		BufferedImage publishSheetImage = ImageIO.read(new File(publishSheetImagePath)); 
		String imageMagickResizeStr = this.genImageMagickResizeCommandStr(publishSheetImagePath, publishSmallSheetImagePath, publishSheetImage.getWidth()/2, publishSheetImage.getHeight()/2);
		ExeCommandUtil.exeCommand(imageMagickResizeStr); 
		
		//用pngauant压缩图片  
		//压缩大雪碧图
		this.noteDesc = "压缩雪碧图：";
		publish(3); 
		String pngquantCompressBigCommandStr = this.genPngquantCompressCommandStr(publishSheetImagePath, "_hd.png", this.pngquantQualityMin, this.pngquantQualityMax);
		ExeCommandUtil.exeCommand(pngquantCompressBigCommandStr); 
		//压缩小雪碧图
		this.noteDesc = "压缩小雪碧图：";
		publish(4);
		String pngquantCompressSmallCommandStr = this.genPngquantCompressCommandStr(publishSmallSheetImagePath, "d.png", this.pngquantQualityMin, this.pngquantQualityMax);
		ExeCommandUtil.exeCommand(pngquantCompressSmallCommandStr);  
		
		//用cwebp压缩图片为webp格式
		//cwebp压缩大雪碧图
		this.noteDesc = "雪碧图webp：";
		publish(5); 
		//获得输出路径
		String publishSheetWebpPath = AppManager.getPublishSheetWebpPath(projectInfoVO);
		String cwebpCompressBigCommandStr = this.genCwebpCompressCommandStr(publishSheetImagePath, publishSheetWebpPath, this.webpQuality, this.webpAlphaQuality);
		ExeCommandUtil.exeCommand(cwebpCompressBigCommandStr);  
		//cwebp压缩大雪碧图
		this.noteDesc = "小雪碧图webp：";
		publish(6); 
		//获得输出路径
		String publishSmallSheetWebpPath = AppManager.getPublishSmallSheetWebpPath(projectInfoVO);
		String cwebpCompressSmallCommandStr = this.genCwebpCompressCommandStr(publishSmallSheetImagePath, publishSmallSheetWebpPath, this.webpQuality, this.webpAlphaQuality);		
		ExeCommandUtil.exeCommand(cwebpCompressSmallCommandStr); 
		
		//开始计算图片uv坐标 
		this.noteDesc = "正在计算uv坐标：";
		//解析雪碧图配置文件
		Map<Short, TPJsonObjVO> imageJsonMap = this.parserSheetJsonConfig(projectInfoVO);
		System.out.println("imageJsonMap size="+imageJsonMap.size()); 
		if (imageJsonMap != null) {
			for (int i=0; i<imageList.size(); i++) { 
				ImageInfoVO oneImageInfoVO = imageList.get(i);
				AppManager.genImageUvInfo(oneImageInfoVO, publishSheetImage.getWidth(), publishSheetImage.getHeight(), imageJsonMap);  
				//放入缓存
				AppManager.putImageInfoVOInCache(oneImageInfoVO);  
				//写入配置文件
				AppManager.writeOneImageConfig(oneImageInfoVO);  
				publish(i+1); 
			}
		} 
		
		//开始导出uv配置
		this.noteDesc = "开始导出uv配置："; 
		//uv配置文件路径
		String publishUVConfigPath = AppManager.getPublishUVConfigPath(projectInfoVO);
		File publishUVConfigFile = new File(publishUVConfigPath);
		if (publishUVConfigFile.exists()) {
			publishUVConfigFile.delete();
		}
		FileOutputStream fos = new FileOutputStream(publishUVConfigFile);
		FileChannel fileChannel = fos.getChannel(); 
		
		//uv配置文件txt路径
		String publishUVConfigTxtPath = AppManager.getPublishUVConfigTextPath(projectInfoVO);
		File publishUVConfigTxtFile = new File(publishUVConfigTxtPath);
		if (publishUVConfigTxtFile.exists()) {
			publishUVConfigTxtFile.delete();
		}
		FileOutputStream txtFos = new FileOutputStream(publishUVConfigTxtFile);
		FileChannel txtFileChannel = txtFos.getChannel(); 
		
		//写入图片数量
		ByteBuffer imageSize = ByteBuffer.allocate(4);  
		imageSize.putInt(imageList.size());
		imageSize.flip(); 
		fileChannel.write(imageSize); 
		//txt写入
		txtFileChannel.write(ByteBuffer.wrap((imageList.size()+":\n").getBytes()));
		
		for (int i=0; i<imageList.size(); i++) { 
			ImageInfoVO oneImageInfoVO = imageList.get(i);
			//System.out.println("oneImageInfoVO="+oneImageInfoVO);  
			//顶点坐标，中心点转化为0,0时的坐标 
			List<MyPointVO> points = oneImageInfoVO.getMegreUseRealMyPointVOList();
			//顶点对应uv坐标 
			List<MyUVInfoVO> uvInfos = oneImageInfoVO.getMegreUseMyUVinfoVOList();
			if (points.size() != uvInfos.size()) {
				fileChannel.close(); 
				fos.close();
				txtFileChannel.close();
				txtFos.close();
				throw new Exception("image "+oneImageInfoVO.getImageName()+" points size != uvInfos size..........");
			} 
			//获得buffer长度
			byte bufferLength = this.getByteBufferLengthByPointNum(points.size());
			ByteBuffer imageBuffer = ByteBuffer.allocate(bufferLength);  
			imageBuffer.putShort(oneImageInfoVO.getImageName()); //写入图片名2字节
			imageBuffer.put((byte)points.size()); //写入顶点个数 1字节 
			//txt准备
			StringBuilder txtSb = new StringBuilder();
			txtSb.append(oneImageInfoVO.getImageName()).append(":");
			txtSb.append(points.size()).append(":");
			
			for (int j=0; j<points.size(); j++) { 
				MyPointVO oneMyPointVO = points.get(j);
				MyUVInfoVO oneMyUVInfoVO = uvInfos.get(j);
				imageBuffer.putShort((short)oneMyPointVO.getX()); //写入顶点x坐标2字节 
				imageBuffer.putShort((short)oneMyPointVO.getY()); //写入顶点y坐标2字节 
				imageBuffer.putFloat(oneMyUVInfoVO.getS()); //写入顶点x对应uc的s坐标4字节 
				imageBuffer.putFloat(oneMyUVInfoVO.getT()); //写入顶点y对应uc的t坐标4字节 
				//txt准备
				txtSb.append("(").append("x=").append(oneMyPointVO.getX()).append(",");
				txtSb.append("y=").append(oneMyPointVO.getY()).append(")");
				txtSb.append("(").append("s=").append(oneMyUVInfoVO.getS()).append(",");
				txtSb.append("t=").append(oneMyUVInfoVO.getT()).append(")");
			}
			//写入配置
			imageBuffer.flip(); 
			fileChannel.write(imageBuffer);  
			
			//写入txt配置
			txtSb.append("\n");
			txtFileChannel.write(ByteBuffer.wrap(txtSb.toString().getBytes())); 
			
			publish(i+1);
		}
		//关闭
		fileChannel.close(); 
		fos.close();
		txtFileChannel.close();
		txtFos.close();
		
		return projectInfoVO; 
	}
	
	@Override 
	protected void process(List<Integer> chunks) {  
		for (int exeNum:chunks) {  
			//int rate = exeNum*100/imageList.size();  
			//progressMonitor.setNote(StringUtil.connectStr(noteDesc, exeNum));  
			//int rate = exeNum*100/progressMaximum;  
			progressMonitor.setNote(StringUtil.connectStr(noteDesc, "    ", exeNum, "/", progressMaximum));  
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
	 * 获得导出图片的ByteArray长度
	 * 格式：图片名字：2字节
			图片顶点数：1字节
			[每个顶点数据：x2字节+y2字节，
			每个顶点uc坐标：s4字节+t4字节]
	 * @param pointNum
	 * @return
	 */
	private byte getByteBufferLengthByPointNum(int pointNum) {
		 if (pointNum == 5) { 
			return 63;
		} else if (pointNum == 6) {
			return 75;
		} else if (pointNum == 7) {
			return 87;
		} else if (pointNum == 8) {
			return 99;
		} else { //pointNum == 4
			return 51;
		} 
	}
	
	/**
	 * 解析雪碧图配置文件
	 * @param projectInfoVO
	 * @return
	 */
	private Map<Short, TPJsonObjVO> parserSheetJsonConfig(ProjectInfoVO projectInfoVO) {
		try {
			Map<Short, TPJsonObjVO> imageJsonMap = new HashMap<Short, TPJsonObjVO>();
			//读取配置文件 
			String publishJsonConfigPath = AppManager.getPublishJsonConfigPath(projectInfoVO); 
			String jsonConfigStr = FileUtil.getFileText(publishJsonConfigPath); 
			//解析请求参数 
			JsonObject paramJsonObj = GsonUtil.jsonParser.parse(jsonConfigStr).getAsJsonObject();
			JsonObject framesObj = paramJsonObj.getAsJsonObject("frames");
			for (Entry<String, JsonElement> oneEntry:framesObj.entrySet()) {
				String imageStrName = oneEntry.getKey();
				JsonObject imageInfo = oneEntry.getValue().getAsJsonObject();
				JsonObject imageFrame = imageInfo.getAsJsonObject("frame");
				JsonObject imageSize =  imageInfo.getAsJsonObject("sourceSize");
				
				short imageName = Short.parseShort(imageStrName.substring(0, imageStrName.indexOf("."))); 
				int x = imageFrame.get("x").getAsInt();
				int y = imageFrame.get("y").getAsInt();
				int width = imageSize.get("w").getAsInt();
				int height = imageFrame.get("h").getAsInt();
				boolean rotated = imageInfo.get("rotated").getAsBoolean();
				
				TPJsonObjVO oneTPJsonObjVO = new TPJsonObjVO(imageName, x, y, width, height, rotated);
				imageJsonMap.put(oneTPJsonObjVO.getImageName(), oneTPJsonObjVO); 
			}
			return imageJsonMap;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 生成命令字符串 
	 * @param imageDir 图片所在目录，该目录下的图片将被合并
	 * @param publishDataFormat 发布数据格式  json
	 * @param publishPath 发布路径   
	 * @param publishConfName 发布配置文件名字   
	 * @param publishSheetName 发布图片名字   
	 * @param textureFormat 发布格式  png jpg webp
	 * @param pixelformat 像素格式  RGBA8888 RGB888
	 * @return
	 */
	private String genTexturePackerCommandStr(String imageDir, String publishDataFormat, String publishPath, String publishConfName, String publishSheetName, String textureFormat, String pixelformat) {
		
		StringBuilder sb = new StringBuilder();
		//执行命令
		//String cmd = "D:/Program Files/CodeAndWeb/TexturePacker/bin/TexturePacker.exe ";
		//sb.append("\"D:/Program Files/CodeAndWeb/TexturePacker/bin/TexturePacker.exe\" ");
		sb.append(AppConfig.getProperty("TexturePacker_Cmd"));   
		
		//发布的描述文件数据格式
		//String dataFormat = "--format JSON ";
		sb.append(" --format ");
		sb.append(publishDataFormat);

		//发布的描述文件名字
		//String data = "--data testHstd{v}.json ";
		sb.append(" --data ");
		sb.append("\"");
		sb.append(publishPath.replace("\\", "/")); 
		sb.append(publishConfName);
		sb.append("\"");
//				if ("webp".equals(textureFormat)) {
//					sb.append("-webp"); 
//				} else if ("jpg".equals(textureFormat)) {
//					sb.append("-jpg"); 
//				} else if ("png".equals(textureFormat)) {
//					sb.append("-png"); 
//				}
//				sb.append(".json ");
		
		//发布的纹理格式 
		//String textureFormat = "--texture-format PNG-32 ";
		sb.append(" --texture-format ");
		sb.append(textureFormat);
		
		//纹理发布路径
		//String texturePath = "--texturepath E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/publish "; 
//				sb.append("--texturepath "); 
//				sb.append(publishPath);
//				sb.append(" ");
		
		//发布的雪碧图名字
		//String sheetName = "--sheet testHstd{v}.png ";
		sb.append(" --sheet "); 
		sb.append(publishSheetName);
//				if ("webp".equals(textureFormat)) {
//					sb.append(".webp ");
//				} else if ("jpg".equals(textureFormat)) {
//					sb.append(".jpg ");
//				} else if ("png".equals(textureFormat)) {
//					sb.append(".png ");
//				}
		
		//dpi
		//String dpi = "--dpi 72 ";
		sb.append(" --dpi 72"); 
		
		//png_opt_level
		//String pngOptLevel = "--png-opt-level 1 ";  
		sb.append(" --png-opt-level 1");  
		
		//jpg压缩比率
		//String jpgQuality = "--jpg-quality 70 ";
		sb.append(" --jpg-quality 70");  
		
		//webp压缩比率
		//String webpQuality = "--webp-quality 70 "; 
		sb.append(" --webp-quality 70");   
		
		//像素格式 opt 
		//String opt = "--opt RGBA8888 "; 
		sb.append(" --opt ");  
		sb.append(pixelformat);
		
		//最大尺寸限制 
		//String maxSize = "--max-size 8192 ";
		sb.append(" --max-size 8192");  
		
		//雪碧图尺寸 2的N次方限制   POT  或者  任意尺寸 AnySize
		//String sizeConstraints = "--size-constraints POT ";
		sb.append(" --size-constraints POT");  
		
		//一次合并两张图，应用相同布局 
		//String variant = "--variant 1:-hd 0.5:sd --force-identical-layout "; 
		//sb.append("--variant 1:-hd --variant 0.5:-sd --force-identical-layout ");  
		
		//缩放
		//String scale = "--scale 1 --scale-mode Smooth ";
		sb.append(" --scale 1 --scale-mode Smooth"); 
		
		//算法，效果最好的
		//String algorithm = "--algorithm MaxRects "; 
		sb.append(" --algorithm MaxRects"); 
		
		//启发式算法,效果最好的
		//String heuristics = "--maxrects-heuristics Best ";
		sb.append(" --maxrects-heuristics Best"); 
		
		//控制多少时间来寻找最小的纹理
		//String packMode = "--pack-mode Best ";
		sb.append(" --pack-mode Best"); 
		
		//允许旋转 --enable-rotation / --disable-rotation
		//String rotation = "--enable-rotation ";
		sb.append(" --enable-rotation"); 
		
		//挤出拉升
		//String extrude = "--extrude 0 ";
		sb.append(" --extrude 0"); 
		
		//边框填充 不填充 
		//String borderPadding = "--border-padding 0 ";
		sb.append(" --border-padding 0"); 
		
		//空隙填充 1像素
		//String shapePadding = "--shape-padding 1 ";
		sb.append(" --shape-padding 1"); 
		
		//合并图片所在目录 
		//String imageDir = "E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/megre ";
		sb.append(" \"");
		sb.append(imageDir.replace("\\", "/"));  
		sb.append("\"");
		
		return sb.toString();
	}
	
	/**
	 * 生成ImageMagick改变尺寸命令字符串
	 * "D:/Program Files/ImageMagick-6.9.3-Q16/convert.exe" -strip -resize 512x1024 "E:/testHstd.png" "E:/testHstd_bak3.png"
	 * @param imagePath 图片路径
	 * @param outImagePath 输出路径
	 * @param width 宽
	 * @param height 高
	 * @return
	 */
	private String genImageMagickResizeCommandStr(String imagePath, String outImagePath, int width, int height) {
		StringBuilder sb = new StringBuilder();
		sb.append(AppConfig.getProperty("ImageMagick_Cmd"));   
		
		sb.append(" -strip");
		
		sb.append(" -resize ");
		sb.append(width);
		sb.append("x");
		sb.append(height);
		
		sb.append(" \"");
		sb.append(imagePath.replace("\\", "/"));
		sb.append("\"");
		
		sb.append(" \"");
		sb.append(outImagePath.replace("\\", "/"));
		sb.append("\"");
		
		return sb.toString();
	}
	
	/**
	 * 生成pngquant压缩命令字符串 
	 * "D:/Program Files/pngquant/pngquant.exe" -f --quality 20-30 --ext _1.png "E:/testHstd.png"
	 * @param imagePath 图片路径
	 * @param ext 压缩图片后缀名
	 * @param minQuality 最低品质
	 * @param maxQuality 最高品质 
	 * @return
	 */
	private String genPngquantCompressCommandStr(String imagePath, String ext, int minQuality, int maxQuality) { 
		StringBuilder sb = new StringBuilder();
		sb.append(AppConfig.getProperty("pngquant_Cmd"));  
		sb.append(" -f"); //覆盖
		
		sb.append(" --quality ");
		sb.append(minQuality);
		sb.append("-");
		sb.append(maxQuality);
		
		sb.append(" --ext ");
		sb.append(ext);
		
		sb.append(" \"");
		sb.append(imagePath.replace("\\", "/")); 
		sb.append("\"");
		
		return sb.toString();
	}
	
	/**
	 * 生成cwebp压缩命令字符串 
	 * "D:\Program Files\libwebp-0.5.0-windows-x64-no-wic\bin\cwebp.exe" -q 70 -alpha_q 20 "E:\test\0.png" -o "E:\test\0.webp"
	 * @param imagePath 图片路径
	 * @param outImagePath 输出图片路径
	 * @param quality 品质
	 * @param alphaQuality alpha品质
	 * @return
	 */
	private String genCwebpCompressCommandStr(String imagePath, String outImagePath, int quality, int alphaQuality) {
		StringBuilder sb = new StringBuilder();
		//"D:/Program Files/pngquant/pngquant.exe" -f --quality 20-30 --ext _1.png "E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/publish/testHstd.png"
		sb.append(AppConfig.getProperty("cwebp_Cmd"));  
		sb.append(" -q "); 
		sb.append(quality);
		
		sb.append(" -alpha_q ");
		sb.append(alphaQuality);

		sb.append(" \"");
		sb.append(imagePath.replace("\\", "/")); 
		sb.append("\"");
		
		sb.append(" -o");  
		sb.append(" \"");
		sb.append(outImagePath.replace("\\", "/")); 
		sb.append("\"");
		
		return sb.toString();
	}

}
