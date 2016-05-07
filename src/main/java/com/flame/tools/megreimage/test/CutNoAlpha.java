package com.flame.tools.megreimage.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.flame.tools.megreimage.vo.MyPointVO;
import com.flame.tools.megreimage.vo.MyRectVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MagreImage 
 * @File: com.flame.tools.megreimage.CutNoAlpha.java
 * @Description: 裁剪透明区域
 * @Create: DerekWu  2016年4月18日 上午11:53:42 
 * @version: V1.0 
 */
public class CutNoAlpha {

	/** 
     * @param args 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
     BufferedImage src = ImageIO.read(new File("e://test//dd.png"));  
     int height = src.getHeight();  
     int width  = src.getWidth();   

     //中心点
     MyPointVO centerPoint = new MyPointVO(width/2, height/2);
     MyPointVO leftMinPoint = null, rightMinPoint = null, topMinPoint = null, bottomMinPoint = null;
     
     leftFor:
     for(int i =0;i<width;i++){  
         for(int j =0;j<height;j++){  
             int pixel = src.getRGB(i, j);  
             int alpha = (pixel & 0xff000000)>>24;//屏蔽低位，并移位到最低位  
         	 if (alpha > 0) {
         		leftMinPoint = new MyPointVO(i,j); 
         		break leftFor;
         	 }
         }  
     }  
     
     topFor:
     for(int i =0;i<height;i++){  
         for(int j =0;j<width;j++){  
             int pixel = src.getRGB(j, i);  
             int alpha = (pixel & 0xff000000)>>24;//屏蔽低位，并移位到最低位  
         	 if (alpha > 0) {
         		topMinPoint = new MyPointVO(j,i);
         		break topFor;
         	 }
         }  
     }  
     
     rightFor:
     for(int i =width-1;i>=0;i--){  
         for(int j =0;j<height;j++){  
             int pixel = src.getRGB(i, j);  
             int alpha = (pixel & 0xff000000)>>24;//屏蔽低位，并移位到最低位  
         	 if (alpha > 0) {
         		rightMinPoint = new MyPointVO(i,j);
         		break rightFor;
         	 }
         }  
     }  
     
     bottomFor:
     for(int i =height-1;i>=0;i--){  
         for(int j =0;j<width;j++){  
             int pixel = src.getRGB(j, i);  
             int alpha = (pixel & 0xff000000)>>24;//屏蔽低位，并移位到最低位  
         	 if (alpha > 0) {
         		bottomMinPoint = new MyPointVO(j,i);
         		break bottomFor;
         	 }
         }  
     }  
     
     MyRectVO myRect = new MyRectVO(centerPoint, leftMinPoint, rightMinPoint, topMinPoint, bottomMinPoint);
     System.out.println(myRect.toString());
     
     
     BufferedImage newBufferedImage = src.getSubimage(myRect.getLeftTopPoint().getX(), myRect.getLeftTopPoint().getY(), myRect.getWidth(), myRect.getHeight());
     ImageIO.write(newBufferedImage, "png", new File("e://test//dd0.png"));
     
     Graphics2D g2 = src.createGraphics();
     g2.setColor(Color.GREEN); 
     g2.fillRect(myRect.getBaseCenterPoint().getX()-1, myRect.getBaseCenterPoint().getY()-1, 3, 3); 
     g2.setColor(Color.RED);
     g2.drawRect(myRect.getLeftTopPoint().getX(), myRect.getLeftTopPoint().getY(), myRect.getWidth(), myRect.getHeight());
     
     ImageIO.write(src, "png", new File("e://test//dd1.png"));
  
     
     //ImageIO.write(src, "PNG", new File("e://my2.png"));// 输出到文件流  
  
    }  

}
