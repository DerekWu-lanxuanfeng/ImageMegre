package com.flame.tools.megreimage.test;

import com.flame.tools.megreimage.util.ExeCommandUtil;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.test.TestTexturePacker.java
 * @Description: 测试合图
 * @Create: DerekWu  2016年5月4日 下午4:51:16 
 * @version: V1.0 
 */
public class TestTexturePacker {

	public static void main(String[] args) { 
		
//		String cmd = "D:/Program Files/CodeAndWeb/TexturePacker/bin/TexturePacker.exe ";
//		String dataFormat = "--format json ";
//		String data = "--data testHstd.json ";
//		String outFile = "--sheet E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/megre/testHstd.png ";
//		String textureFormat = "--texture-format E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/megre ";
//		String dpi = "--dpi 72 ";
//		String pngOptLevel = "--png-opt-level 1 "; 
//		String imageDir = "--texturepath E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/megre ";
		
		
		String imageDir = "E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/megre/";
		String publishPath = "E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/publish/"; 
		String commandStr = genCommandStr(imageDir, "json", publishPath, "testHstd-png.json", "testHstd.png", "png", "RGBA8888");
		ExeCommandUtil.exeCommand(commandStr);   
		
		commandStr = genCommandStr(imageDir, "json", publishPath, "testHstd-webp.json", "testHstd.webp", "webp", "RGBA8888");
		ExeCommandUtil.exeCommand(commandStr); 
		
		commandStr = genCommandStr(imageDir, "json", publishPath, "testHstd-jpg.json", "testHstd.jpg", "jpg", "RGB888");
		ExeCommandUtil.exeCommand(commandStr); 
		
		commandStr = genCommandStr(imageDir, "json", publishPath, "testHstd-jpg-mask.json", "testHstd-mask.jpg", "jpg", "ALPHA"); 
		ExeCommandUtil.exeCommand(commandStr); 
	}
	
	/**
	 * 生成命令字符串 
	 * @param imageDir 图片所在目录，该目录下的图片将被合并
	 * @param publishDataFormat 发布数据格式  json
	 * @param publishPath 发布路径   
	 * @param publishConfName 发布名字   
	 * @param publishSheetName 发布名字   
	 * @param textureFormat 发布格式  png jpg webp
	 * @param pixelformat 像素格式  RGBA8888 RGB888
	 * @return
	 */
	private static String genCommandStr(String imageDir, String publishDataFormat, String publishPath, String publishConfName, String publishSheetName, String textureFormat, String pixelformat) {
		
		StringBuilder sb = new StringBuilder();
		//执行命令
		//String cmd = "D:/Program Files/CodeAndWeb/TexturePacker/bin/TexturePacker.exe ";
		sb.append("\"D:/Program Files/CodeAndWeb/TexturePacker/bin/TexturePacker.exe\" ");
		
		//发布的描述文件数据格式
		//String dataFormat = "--format JSON ";
		sb.append("--format ");
		sb.append(publishDataFormat);
		sb.append(" ");
		
		//发布的描述文件名字
		//String data = "--data testHstd{v}.json ";
		sb.append("--data ");
		sb.append("\"");
		sb.append(publishPath);
		sb.append(publishConfName);
		sb.append("\"");
//		if ("webp".equals(textureFormat)) {
//			sb.append("-webp"); 
//		} else if ("jpg".equals(textureFormat)) {
//			sb.append("-jpg"); 
//		} else if ("png".equals(textureFormat)) {
//			sb.append("-png"); 
//		}
//		sb.append(".json ");
		
		//发布的纹理格式 
		//String textureFormat = "--texture-format PNG-32 ";
		sb.append(" --texture-format ");
		sb.append(textureFormat);
		sb.append(" "); 
		
		//纹理发布路径
		//String texturePath = "--texturepath E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/publish "; 
//		sb.append("--texturepath "); 
//		sb.append(publishPath);
//		sb.append(" ");
		
		//发布的雪碧图名字
		//String sheetName = "--sheet testHstd{v}.png ";
		sb.append("--sheet "); 
		sb.append(publishSheetName);
//		if ("webp".equals(textureFormat)) {
//			sb.append(".webp ");
//		} else if ("jpg".equals(textureFormat)) {
//			sb.append(".jpg ");
//		} else if ("png".equals(textureFormat)) {
//			sb.append(".png ");
//		}
		
		//dpi
		//String dpi = "--dpi 72 ";
		sb.append(" --dpi 72 "); 
		
		//png_opt_level
		//String pngOptLevel = "--png-opt-level 1 ";  
		sb.append("--png-opt-level 1 ");  
		
		//jpg压缩比率
		//String jpgQuality = "--jpg-quality 70 ";
		sb.append("--jpg-quality 70 ");  
		
		//webp压缩比率
		//String webpQuality = "--webp-quality 70 "; 
		sb.append("--webp-quality 70 ");   
		
		//像素格式 opt 
		//String opt = "--opt RGBA8888 "; 
		sb.append("--opt ");  
		sb.append(pixelformat);
		sb.append(" ");
		
		//最大尺寸限制 
		//String maxSize = "--max-size 4096 ";
		sb.append("--max-size 4096 ");  
		
		//雪碧图尺寸 2的N次方限制   POT  或者  任意尺寸 AnySize
		//String sizeConstraints = "--size-constraints POT ";
		sb.append("--size-constraints POT ");  
		
		//一次合并两张图，应用相同布局 
		//String variant = "--variant 1:-hd 0.5:sd --force-identical-layout "; 
		//sb.append("--variant 1:-hd --variant 0.5:-sd --force-identical-layout ");  
		
		//缩放
		//String scale = "--scale 1 --scale-mode Smooth ";
		sb.append("--scale 1 --scale-mode Smooth "); 
		
		//算法，效果最好的
		//String algorithm = "--algorithm MaxRects "; 
		sb.append("--algorithm MaxRects "); 
		
		//启发式算法,效果最好的
		//String heuristics = "--maxrects-heuristics Best ";
		sb.append("--maxrects-heuristics Best "); 
		
		//控制多少时间来寻找最小的纹理
		//String packMode = "--pack-mode Best ";
		sb.append("--pack-mode Best "); 
		
		//允许旋转 --enable-rotation / --disable-rotation
		//String rotation = "--enable-rotation ";
		sb.append("--enable-rotation "); 
		
		//挤出拉升
		//String extrude = "--extrude 0 ";
		sb.append("--extrude 0 "); 
		
		//边框填充 不填充 
		//String borderPadding = "--border-padding 0 ";
		sb.append("--border-padding 0 "); 
		
		//空隙填充 1像素
		//String shapePadding = "--shape-padding 1 ";
		sb.append("--shape-padding 1 "); 
		
		//合并图片所在目录 
		//String imageDir = "E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/megre ";
		sb.append("\"");
		sb.append(imageDir); 
		sb.append("\"");
		
		return sb.toString();
	}

}
