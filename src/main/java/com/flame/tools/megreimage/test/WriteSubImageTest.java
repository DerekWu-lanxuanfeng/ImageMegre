package com.flame.tools.megreimage.test;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.flame.tools.megreimage.util.ImageUtil;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.test.WriteSubImageTest.java
 * @Description: 斜土小图片的测试  
 * @Create: DerekWu  2016年4月28日 下午4:45:50 
 * @version: V1.0 
 */
public class WriteSubImageTest { 

	public static void main(String[] args) { 
		try {
			BufferedImage image = ImageIO.read(new File("e://test//dddd.png")); 
			BufferedImage subImage = ImageIO.read(new File("e://test//654_cut.png")); 
			
			BufferedImage newBuffered = ImageUtil.writeSubImage(image, subImage, 5, 5, false);  
//			ImageUtil.writeSubImage(image, subImage, 200, 200, false); 
			
			ImageIO.write(newBuffered, "png", new File("e://test//big_add_sub.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写入子图片(已经作废)
	 * @param image
	 * @param subImage 
	 * @param x 写入位置
	 * @param y 写入位置 
	 * @param isRotate = true 为顺时针90度旋转 
	 */
//	public static void writeSubImage(BufferedImage image, BufferedImage subImage, int x, int y, boolean isRotate) { 
//		boolean isOk = false;
//		Graphics2D g2 = image.createGraphics();  
//		if (isRotate) {
//			int imageCenterX = image.getWidth()/2; 
//			int imageCenterY = image.getHeight()/2; 
//			g2.rotate(Math.toRadians(90), imageCenterX, imageCenterY);  
////			System.out.println("imageCenterX="+imageCenterX);
////			System.out.println("imageCenterY="+imageCenterY);
////			System.out.println("x="+x);
////			System.out.println("y="+y);
////			System.out.println(" lx="+(imageCenterX+y-imageCenterY));
////			System.out.println(" ly="+(imageCenterY +x-imageCenterX-subImage.getHeight())); 
//			
//			MyPointVO rotatePoint = AlgorithmUtil.getRotatePoint(x+subImage.getHeight(), y, imageCenterX, imageCenterY, -90);
//			//System.out.println(rotatePoint); 
//			isOk = g2.drawImage(subImage, rotatePoint.getX(), rotatePoint.getY(), null);   
//		} else {
//			isOk = g2.drawImage(subImage, x, y, null); 
//		}
//		System.out.println("isOk="+isOk); 
//		g2.dispose();
//	}

	

}
