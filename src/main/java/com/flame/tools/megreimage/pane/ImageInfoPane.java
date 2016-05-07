package com.flame.tools.megreimage.pane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.flame.tools.megreimage.AppManager;
import com.flame.tools.megreimage.util.StringUtil;
import com.flame.tools.megreimage.vo.ImageInfoVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.pane.ImageInfoPane.java
 * @Description: 图片信息面板 
 * @Create: DerekWu  2016年4月22日 下午4:21:32 
 * @version: V1.0 
 */
public class ImageInfoPane extends JPanel { 

	private static final long serialVersionUID = 6240870539131122599L;
	
	/** 图片名字 */
	private JLabel imageNameText;
	/** 项目名字 */
	private JLabel projectNameText;
	/** 裁切形状 */
	private JLabel pointNumText;
	/** 状态  */
	private JLabel statusText; 
	
	/** 中间部分，包含按钮区和图片区  */
	private JPanel imagePaneCenter;
	
	/** 原图Label */
	private JLabel baseImageLabel;
	/** 原图图像显示面板   */
	private JPanel baseImageSubPane;
	/** 裁切示意图图像显示面板  */
	private JPanel cutViewSubPane;
	/** 裁切矩形Label */
	private JLabel cutRectLabel;
	/** 裁切矩形图像显示面板  */
	private JPanel cutRectSubPane;
	/** 合并示意图图像显示面板  */
	private JPanel megreViewSubPane;
	

	public ImageInfoPane() {
		this.setLayout(new BorderLayout()); 
		this.initTopPane(); 
		this.initButtonArea();
		this.initImageArea(); 
	}
	
	private void initTopPane() {
		//最上面内容
		JPanel imagePaneTop = new JPanel(); 
		imagePaneTop.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		imagePaneTop.setBorder(BorderFactory.createEtchedBorder());
		
		//图片名字 
		JLabel imageName = new JLabel("图片名字：", JLabel.RIGHT);
		imagePaneTop.add(imageName);
		imageNameText = new JLabel("32767.png", JLabel.LEFT);
		imageNameText.setPreferredSize(new Dimension(80, 30)); 
		imagePaneTop.add(imageNameText);
		
		//项目名字
		JLabel projectName = new JLabel("项目名字：", JLabel.RIGHT);
		imagePaneTop.add(projectName);
		projectNameText = new JLabel("projectName", JLabel.LEFT);
		projectNameText.setPreferredSize(new Dimension(150, 30)); 
		imagePaneTop.add(projectNameText);
		
		//边数，即顶点数 
		JLabel pointNum = new JLabel("裁切形状：", JLabel.RIGHT);
		imagePaneTop.add(pointNum);
		pointNumText = new JLabel("8边形", JLabel.LEFT); //或者 未裁切 
		pointNumText.setPreferredSize(new Dimension(100, 30)); 
		imagePaneTop.add(pointNumText);
		
		//状态 
		JLabel status = new JLabel("图片状态：", JLabel.RIGHT);
		imagePaneTop.add(status);
		statusText = new JLabel("未裁切", JLabel.LEFT); //或者 已经裁切成比矩形更多的多边形 
		statusText.setPreferredSize(new Dimension(300, 30)); 
		imagePaneTop.add(statusText);
		
		//添加到面板
		this.add(imagePaneTop, BorderLayout.NORTH);
	}
	
	private void initButtonArea() {
		//中间内容
		imagePaneCenter = new JPanel(new BorderLayout()); 
		this.add(imagePaneCenter, BorderLayout.CENTER);
		
		//中间内容的上部分内容为 按钮区
		JPanel buttonPane = new JPanel(); 
		buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		buttonPane.setBorder(BorderFactory.createEtchedBorder());
		//将按钮区添加到中间内容的顶部
		imagePaneCenter.add(buttonPane, BorderLayout.NORTH);
		
		//特殊处理label 
		JLabel exeLabel = new JLabel("特殊处理操作：");
		buttonPane.add(exeLabel);
		
		//矩形运算按钮 
		JButton exeFourButton = new JButton("矩形运算");
		buttonPane.add(exeFourButton);
		
		//5边形运算按钮 
		JButton exeFiveButton = new JButton("5边形运算");
		buttonPane.add(exeFiveButton);
		
		//6边形运算按钮 
		JButton exeSixButton = new JButton("6边形运算");
		buttonPane.add(exeSixButton);
		
		//7边形运算按钮 
		JButton exeSevenButton = new JButton("7边形运算");
		buttonPane.add(exeSevenButton);
		
		//8边形运算按钮 
		JButton exeEightButton = new JButton("8边形运算");
		buttonPane.add(exeEightButton);
		
		//特殊处理label 
		JLabel descLabel = new JLabel("   这一排功能暂未实现");
		descLabel.setForeground(Color.RED);
		buttonPane.add(descLabel);
	} 
	
	private void initImageArea() {
		try {
			//图片面板，横向放3个图片
			JPanel imageAreaPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
			imagePaneCenter.add(imageAreaPane, BorderLayout.CENTER);
			
			//原图面板 
			JPanel baseImagePane = new JPanel(new BorderLayout()); 
			imageAreaPane.add(baseImagePane); //放入图片区域
			//原图title
			baseImageLabel = new JLabel("原始图片纹理(400*400)", JLabel.CENTER);
			baseImageLabel.setPreferredSize(new Dimension(420, 30)); 
			baseImageLabel.setBorder(BorderFactory.createEtchedBorder());
			baseImagePane.add(baseImageLabel, BorderLayout.NORTH); 
			//原图子面板
			baseImageSubPane = new JPanel();
			baseImageSubPane.setPreferredSize(new Dimension(420, 420)); 
			baseImageSubPane.setBorder(BorderFactory.createEtchedBorder());
			baseImagePane.add(baseImageSubPane, BorderLayout.CENTER);  
			
			//原图图像 
//			baseImage = new JLabel(); 
//			baseImage.setBorder(BorderFactory.createEtchedBorder());
//			BufferedImage image = ImageIO.read(new File("e://test//dd.png"));
//			ImageIcon baseImageIcon = new ImageIcon(image);  
//			baseImage.setIcon(baseImageIcon);
//			baseImageSubPane.add(baseImage, BorderLayout.CENTER);
			
			//裁切示意图
			JPanel cutViewPane = new JPanel(new BorderLayout()); 
			imageAreaPane.add(cutViewPane); //放入图片区域
			//裁切示意图title
			JLabel cutViewLabel = new JLabel("裁切示意图", JLabel.CENTER);
			cutViewLabel.setPreferredSize(new Dimension(420, 30)); 
			cutViewLabel.setBorder(BorderFactory.createEtchedBorder());
			cutViewPane.add(cutViewLabel, BorderLayout.NORTH); 
			//裁切示意图子面板
			cutViewSubPane = new JPanel();
			cutViewSubPane.setPreferredSize(new Dimension(420, 420)); 
			cutViewSubPane.setBorder(BorderFactory.createEtchedBorder());
			cutViewPane.add(cutViewSubPane, BorderLayout.CENTER);  
			
			//裁切矩形
			JPanel cutRectPane = new JPanel(new BorderLayout()); 
			imageAreaPane.add(cutRectPane); //放入图片区域
			//裁切示意图title
			cutRectLabel = new JLabel("裁切矩形(210*320)", JLabel.CENTER);
			cutRectLabel.setPreferredSize(new Dimension(420, 30)); 
			cutRectLabel.setBorder(BorderFactory.createEtchedBorder());
			cutRectPane.add(cutRectLabel, BorderLayout.NORTH); 
			//裁切示意图子面板
			cutRectSubPane = new JPanel();
			cutRectSubPane.setPreferredSize(new Dimension(420, 420)); 
			cutRectSubPane.setBorder(BorderFactory.createEtchedBorder());
			cutRectPane.add(cutRectSubPane, BorderLayout.CENTER);  
			
			//合并示意图
			JPanel megreViewPane = new JPanel(new BorderLayout()); 
			imageAreaPane.add(megreViewPane); //放入图片区域
			//合并示意图title
			JLabel megreViewLabel = new JLabel("合并示意图", JLabel.CENTER);
			megreViewLabel.setPreferredSize(new Dimension(420, 30)); 
			megreViewLabel.setBorder(BorderFactory.createEtchedBorder());
			megreViewPane.add(megreViewLabel, BorderLayout.NORTH); 
			//合并示意图子面板
			megreViewSubPane = new JPanel(); 
			megreViewSubPane.setPreferredSize(new Dimension(420, 420)); 
			megreViewSubPane.setBorder(BorderFactory.createEtchedBorder());
			megreViewPane.add(megreViewSubPane, BorderLayout.CENTER);  
			
		} catch (Exception e) { 
			e.printStackTrace();
		} 
		
	}
	
	public void chengeImageInfoVO(ImageInfoVO imageInfoVO) {
		imageNameText.setText(imageInfoVO.getImageName()+".png");
		projectNameText.setText(imageInfoVO.getProjectName());
		pointNumText.setText(imageInfoVO.getCutShapeName()); 
		statusText.setText(imageInfoVO.getStatusDesc()); 
		
		//获得原图 
		JLabel baseImage = new JLabel();
		baseImage.setBorder(BorderFactory.createEtchedBorder());
		BufferedImage baseBufferedImage = AppManager.getBaseImage(imageInfoVO);
		if (baseBufferedImage != null) {
			ImageIcon baseImageIcon = new ImageIcon(baseBufferedImage);  
			baseImage.setIcon(baseImageIcon);
			//设置原图尺寸
			baseImageLabel.setText(StringUtil.connectStr("原始图片纹理(", baseBufferedImage.getWidth(),"*",baseBufferedImage.getHeight(),")")); 
		} else {
			//设置原图尺寸
			baseImageLabel.setText("原始图片纹理"); 
			baseImage.setText("原图不存在"); 
		}
		baseImageSubPane.removeAll(); 
		baseImageSubPane.add(baseImage);
		
		
		//获得裁切示意图
		JLabel cutViewImage = new JLabel();
		cutViewImage.setBorder(BorderFactory.createEtchedBorder());
		BufferedImage cutViewBufferedImage = AppManager.getCutViewImage(imageInfoVO, baseBufferedImage);
		if (cutViewBufferedImage != null) {
			ImageIcon cutViewImageIcon = new ImageIcon(cutViewBufferedImage);  
			cutViewImage.setIcon(cutViewImageIcon);
		} else {
			cutViewImage.setText("还没有进行裁切"); 
		}
		cutViewSubPane.removeAll();
		cutViewSubPane.add(cutViewImage);
		
		//获得裁切矩形图
		JLabel cutRectImage = new JLabel();
		cutRectImage.setBorder(BorderFactory.createEtchedBorder());
		BufferedImage cutRectBufferedImage = AppManager.getCutRectImage(imageInfoVO);
		if (cutRectBufferedImage != null) {
			ImageIcon cutRectImageIcon = new ImageIcon(cutRectBufferedImage);  
			cutRectImage.setIcon(cutRectImageIcon);
			//设置裁切尺寸
			cutRectLabel.setText(StringUtil.connectStr("裁切矩形(", cutRectBufferedImage.getWidth(),"*",cutRectBufferedImage.getHeight(),")"));
		} else {
			//设置裁切尺
			cutRectLabel.setText("裁切矩形");
			cutRectImage.setText("还没有进行裁切"); 
		} 
		cutRectSubPane.removeAll(); 
		cutRectSubPane.add(cutRectImage);
		
		//获得合图示意图
		JLabel megreViewImage = new JLabel();
		megreViewImage.setBorder(BorderFactory.createEtchedBorder());
		BufferedImage megreViewBufferedImage = AppManager.getMegreViewImage(imageInfoVO);  
		if (megreViewBufferedImage != null) {
			ImageIcon megreViewImageIcon = new ImageIcon(megreViewBufferedImage);  
			megreViewImage.setIcon(megreViewImageIcon); 
		} else {
			megreViewImage.setText("还没有合图"); 
		}
		megreViewSubPane.removeAll();  
		megreViewSubPane.add(megreViewImage);
		
	}

}
