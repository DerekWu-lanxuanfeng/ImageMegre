package com.flame.tools.megreimage.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MagreImage 
 * @File: com.flame.tools.megreimage.MyImage.java
 * @Description: 图片处理像素
 * @Create: DerekWu  2016年4月18日 上午9:39:07 
 * @version: V1.0 
 */
public class ImageExePixel {

	 /** 
     * @param args 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
    	//testPixel();
    	testMask();
    }

	/**
	 * @throws IOException
	 */
	private static void testPixel() throws IOException {
		BufferedImage src = ImageIO.read(new File("e://my.png"));  
		 int height = src.getHeight();  
		 int width  = src.getWidth();   
		 int[] rgb = new int [4];  
		 //去的(660,0)的像素值  
		 int pixel = src.getRGB(10, 0);  
		 System.out.println(pixel);  
//     第一个为alpha通道，接下来为R，G，B各值  
		 rgb[0] = (pixel & 0xff000000)>>24;//屏蔽低位，并移位到最低位  
		 rgb[1] = (pixel & 0xff0000) >> 16;  
		 rgb[2] = (pixel & 0xff00) >> 8;  
		 rgb[3] = (pixel & 0xff);  
		 System.out.println(rgb[0]+" "+rgb[1]+"  "+rgb[2]+"  "+rgb[3]);  
		 //每个像素的计算方法，每位在相应的位置进行或运算  
		 int p = (rgb[0]<<24)|(rgb[1]<<16)|(rgb[2]<<8)|(rgb[3]);  
		 System.out.println(p);  
		   
		 for(int i =0;i<width;i++){  
		     for(int j =0;j<height;j++){  
		         //int p = src.getRGB(i, j);  
		         pixel = src.getRGB(i, j);  
		         rgb[0] = (pixel & 0xff000000)>>24;//屏蔽低位，并移位到最低位  
		         rgb[1] = (pixel & 0xff0000) >> 16;  
		         rgb[2] = (pixel & 0xff00) >> 8;  
		         rgb[3] = (pixel & 0xff);  
		         //给像素进行赋值(rgb[1]/2)<<15  
		        src.setRGB(i, j,((rgb[0]<<24)|(rgb[1]<<16)|(rgb[2]<<8)|(rgb[3])));
		     }  
		     System.out.println();  
		 }
		 ImageIO.write(src, "PNG", new File("e://my2.png"));// 输出到文件流  
	}  
    
    
	private static void testMask() throws IOException {
		BufferedImage src = ImageIO.read(new File("e://test//0.png"));
		int width = src.getWidth();
		int height = src.getHeight();
		BufferedImage res = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixel = src.getRGB(i, j);
				int alpha = (pixel & 0xff000000) >> 24;// 屏蔽低位，并移位到最低位
				//res.setRGB(i, j, alpha); 
				res.setRGB(i, j, alpha);
			}
		}
		ImageIO.write(src, "jpg", new File("e://test//0_mask_0.jpg"));// 输出到文件流  
	}
}
