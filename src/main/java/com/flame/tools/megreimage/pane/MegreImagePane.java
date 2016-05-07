package com.flame.tools.megreimage.pane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.flame.tools.megreimage.AppManager;
import com.flame.tools.megreimage.AppMediator;
import com.flame.tools.megreimage.consts.ProjectStatusEnum;
import com.flame.tools.megreimage.swingworker.MegreAndPublishSwingWorker;
import com.flame.tools.megreimage.util.DateUtil;
import com.flame.tools.megreimage.util.StringUtil;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.ProjectInfoVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.pane.MegreImagePane.java
 * @Description: 合并图片面板 
 * @Create: DerekWu  2016年4月25日 下午4:54:36 
 * @version: V1.0 
 */
public class MegreImagePane extends JPanel { 

	private static final long serialVersionUID = -5728869003179733993L;
	
	/** 项目信息  */ 
	private ProjectInfoVO projectInfoVO;
	
	/** 项目名字  */
	private JLabel projectNameText; 
	/** 图片数量  */
	private JLabel imageNumText;
	/** 状态  */
	private JLabel statusText;
	/** 合图信息面板  */
	private JPanel megrePaneInfo;
	/** 选择宽度*/
	//private JComboBox<String> selectWidthComboBox;
	/** 选择高度 */
	//private JComboBox<String> selectHeightComboBox;
	
	/** webp-q */
	private JTextField webpQualityText;
	private static final int webpQualityDefault = 75;
	
	/** webp-alpha-q */
	private JTextField webpAlphaQualityText;
	private static final int webpAlphaQualityDefault = 20;
	
	/** pngquant-q-min */ 
	private JTextField pngquantQualityMinText;
	private static final int pngquantQualityMinDefault = 20;
	
	/** pngquant-q-max */ 
	private JTextField pngquantQualityMaxText;
	private static final int pngquantQualityMaxDefault = 30;
	
	/** 合图尺寸 */
	private JLabel megreSizeText;
	
	/** 合并时间 */
	private JLabel lastModifiedText;
	
	/** 已合并数量  */
	//private JLabel megreNumText;
	/** 未合并数量  */
	//private JLabel noMegreNumText;
	
	/** 合图显示区 */
	private JPanel megreImageViewPane;
	
	/** 当前显示图片所在项目  */
	private String megreImageProject;
	
	/** 强制更新显示图片  */
	private boolean isForceUpdateMegreImage = false;

	/**
	 * 构造函数
	 */
	public MegreImagePane() {
		this.setLayout(new BorderLayout()); 
		this.initTopPane(); 
		this.initMegreInfoArea();
	}
	
	private void initTopPane() {
		//最上面内容
		JPanel megreImagePaneTop = new JPanel();  
		megreImagePaneTop.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		megreImagePaneTop.setBorder(BorderFactory.createEtchedBorder());
		
		//项目名字
		JLabel projectName = new JLabel("项目名称：", JLabel.RIGHT); 
		megreImagePaneTop.add(projectName);
		projectNameText = new JLabel("未选择项目", JLabel.LEFT);
		projectNameText.setPreferredSize(new Dimension(100, 30)); 
		megreImagePaneTop.add(projectNameText);
		
		//图片数量 
		JLabel imageNum = new JLabel("图片数量：");  
		megreImagePaneTop.add(imageNum); 
		imageNumText = new JLabel("0 个");
		imageNumText.setPreferredSize(new Dimension(50, 30)); 
		megreImagePaneTop.add(imageNumText); 		
		
		//项目状态 
		JLabel status = new JLabel("项目状态：", JLabel.RIGHT);
		megreImagePaneTop.add(status);
		statusText = new JLabel("未选择项目", JLabel.LEFT); //或者 已经裁切成比矩形更多的多边形 
		statusText.setPreferredSize(new Dimension(150, 30)); 
		megreImagePaneTop.add(statusText);
		
		//选择合图宽度
//		JLabel selectWidthLabel = new JLabel("选择合图宽度：", JLabel.RIGHT);
//		megreImagePaneTop.add(selectWidthLabel); 
//		String[] selectItems = { "512", "1024", "2048", "4096"};   
//		selectWidthComboBox = new JComboBox<String>(selectItems);    
//		selectWidthComboBox.setPreferredSize(new Dimension(60, 26)); 
//		//selectWidthComboBox.setEditable(true);   
//	    megreImagePaneTop.add(selectWidthComboBox); 
	    
	    //选择高度 
//	    JLabel selectHeigthLabel = new JLabel("高度", JLabel.RIGHT);
//	    megreImagePaneTop.add(selectHeigthLabel); 
//	    selectHeightComboBox = new JComboBox<String>(selectItems);
//	    selectHeightComboBox.setPreferredSize(new Dimension(60, 26)); 
//		//selectHeightComboBox.setEditable(true);    
//	    megreImagePaneTop.add(selectHeightComboBox); 
		
		//pngquant-q
		JLabel pngquantQuality = new JLabel("pngquant-q");  
		megreImagePaneTop.add(pngquantQuality); 
		pngquantQualityMinText = new JTextField("20");  
		pngquantQualityMinText.setPreferredSize(new Dimension(30, 26)); 
		megreImagePaneTop.add(pngquantQualityMinText);  
		JLabel pngquantQuality2 = new JLabel("-"); 
		megreImagePaneTop.add(pngquantQuality2); 
		pngquantQualityMaxText = new JTextField("30");  
		pngquantQualityMaxText.setPreferredSize(new Dimension(30, 26)); 
		megreImagePaneTop.add(pngquantQualityMaxText); 
		
		//webp-q
		JLabel webpQuality = new JLabel("webp-q"); 
		megreImagePaneTop.add(webpQuality); 
		webpQualityText = new JTextField("100"); 
		webpQualityText.setPreferredSize(new Dimension(30, 26)); 
		megreImagePaneTop.add(webpQualityText);
		
		//webp-alpha-q
		JLabel webpAlphaQuality = new JLabel("webp-alpha-q"); 
		megreImagePaneTop.add(webpAlphaQuality); 
		webpAlphaQualityText = new JTextField("100"); 
		webpAlphaQualityText.setPreferredSize(new Dimension(30, 26)); 
		megreImagePaneTop.add(webpAlphaQualityText); 

	    //空开一点
		JLabel space = new JLabel();
		space.setPreferredSize(new Dimension(30, 30)); 
		megreImagePaneTop.add(space);
		
	    //重设参数
	    JButton exportButton = new JButton("重设参数");
	    megreImagePaneTop.add(exportButton); 
	    exportButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				//重设参数 
				resetParams();  
			}
	    });
	    
	    //合并发布按钮
	    JButton megreButton = new JButton("合并纹理并发布项目");
	    megreImagePaneTop.add(megreButton); 
	    megreButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				//合并发布
				megreAndPublish(); 
			}
	    });
	    
	    //空开一点
  		JLabel space2 = new JLabel();
  		space2.setPreferredSize(new Dimension(30, 30)); 
  		megreImagePaneTop.add(space2);
  		
  		//打开发布目录
	    JButton openDir = new JButton("打开发布目录");
	    megreImagePaneTop.add(openDir); 
	    openDir.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) { 
				//打开发布目录
				openPublishDir(); 
			}
	    });
	    
		//添加到面板
		this.add(megreImagePaneTop, BorderLayout.NORTH);
	}
	
	/**
	 * 打开发布目录
	 */
	private void openPublishDir() {
		String title = "提示信息";
		if (projectInfoVO == null) {
			String msg = "项目不存在！";
			JOptionPane.showMessageDialog(AppMediator.getAppJFrame().getjFrame(), msg, title, JOptionPane.OK_OPTION);
			return; 
		}
		
		
		String publshDirPath = AppManager.getPublishDirPath(projectInfoVO.getName());
		File publishDirFile = new File(publshDirPath);
		if (!publishDirFile.exists()) {
			String msg = "项目发布目录不存在！";
			JOptionPane.showMessageDialog(AppMediator.getAppJFrame().getjFrame(), msg, title, JOptionPane.OK_OPTION);
			return; 
		}
		
		try {
			Desktop.getDesktop().open(publishDirFile);
		} catch (IOException e1) { 
			e1.printStackTrace();
		}
	}
	
	private void initMegreInfoArea() {
		//中间内容
		megrePaneInfo = new JPanel(new BorderLayout()); 
		//megrePaneInfo.setBorder(BorderFactory.);  
		this.add(megrePaneInfo, BorderLayout.CENTER); 
		
		//中间内容的上部分内容为 合图描述信息区
		JPanel megreDescPane = new JPanel(); 
		megreDescPane.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		megreDescPane.setBorder(BorderFactory.createEtchedBorder()); 
		//将按钮区添加到中间内容的顶部
		megrePaneInfo.add(megreDescPane, BorderLayout.SOUTH); 
		
		//合图尺寸
		JLabel megreSize = new JLabel("合图尺寸："); 
		megreSize.setForeground(Color.RED);
		megreDescPane.add(megreSize); 
		megreSizeText = new JLabel("暂无");   
		megreSizeText.setForeground(Color.RED);
		megreSizeText.setPreferredSize(new Dimension(200, 12)); 
		megreDescPane.add(megreSizeText);  
		
		//合图时间 
		JLabel lastModified = new JLabel("合图时间："); 
		lastModified.setForeground(Color.RED); 
		megreDescPane.add(lastModified);  
		lastModifiedText = new JLabel("暂无");   
		lastModifiedText.setForeground(Color.RED); 
		megreDescPane.add(lastModifiedText);
		
		//中间内容的中间部分为 合显示区
		megreImageViewPane = new JPanel(); 
		megreImageViewPane.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		megreImageViewPane.setBorder(BorderFactory.createEtchedBorder()); 
		
		JScrollPane scrollpane = new JScrollPane(megreImageViewPane);
		
		megrePaneInfo.add(scrollpane, BorderLayout.CENTER); 
	} 
	
	/**
	 * 重设参数  
	 */
	private void resetParams() {
		this.webpQualityText.setText(""+webpQualityDefault);
		this.webpAlphaQualityText.setText(""+webpAlphaQualityDefault);
		this.pngquantQualityMinText.setText(""+pngquantQualityMinDefault);
		this.pngquantQualityMaxText.setText(""+pngquantQualityMaxDefault);
	}
	
	/**
	 * 合并并发布
	 */
	private void megreAndPublish() {
		
		if (!ProjectStatusEnum.POLYGON_EXE.equals(projectInfoVO.getStatus())) { 
			String msg = "当前项目 ["+projectInfoVO.getName()+"] 还没有进行“多边形组合运算”，不能合并 ！";
			String title = "提示信息";
			JOptionPane.showMessageDialog(AppMediator.getAppJFrame().getjFrame(), msg, title, JOptionPane.OK_OPTION);
			return; 
		}
		
		//判断合法性数据
		String msg = null;
		if ("".equals(webpQualityText.getText())) {
			msg = "请填写 webp格式压缩比率（webp-q）！";
		} else if (!webpQualityText.getText().matches("[0-9]+")) {
			msg = "webp格式压缩比率（webp-q）必须填写数字！";
		}
		
		if ("".equals(webpAlphaQualityText.getText())) {
			msg = "请填写 webp格式alpha压缩比率（webp-alpha-q）！";
		} else if (!webpAlphaQualityText.getText().matches("[0-9]+")) {
			msg = "webp格式alpha压缩比率（webp-alpha-q）必须填写数字！";
		}
		
		if ("".equals(pngquantQualityMinText.getText())) {
			msg = "请填写 pngquant压缩比率（pngquant-q-min）！";
		} else if (!pngquantQualityMinText.getText().matches("[0-9]+")) {
			msg = "pngquant压缩比率（pngquant-q-min）必须填写数字！";
		}
		
		if ("".equals(pngquantQualityMaxText.getText())) { 
			msg = "请填写 pngquant压缩比率（pngquant-q-max）！";
		} else if (!pngquantQualityMaxText.getText().matches("[0-9]+")) {
			msg = "pngquant压缩比率（pngquant-q-max）必须填写数字！";
		}
		
		if (msg!=null) {
			JOptionPane.showMessageDialog(AppMediator.getAppJFrame().getjFrame(), msg, "提示信息", JOptionPane.OK_OPTION);
			return;
		}
		
		msg = "注意：如果合图比较大，刚开始的时候会卡主！\n点击确定之后请不要进行其他操作，确定开始合并发布么？";
		int ret = JOptionPane.showConfirmDialog(AppMediator.getAppJFrame().getjFrame(), msg, "提示信息", JOptionPane.YES_NO_OPTION);
		if (ret != JOptionPane.YES_OPTION) {
			return; 
		}
		
		//webp-q 
		int webpQuality = Integer.parseInt(webpQualityText.getText());
		//webp-alpha-q 
		int webpAlphaQuality = Integer.parseInt(webpAlphaQualityText.getText());
		//pngquant-q-min 
		int pngquantQualityMin = Integer.parseInt(pngquantQualityMinText.getText());
		//pngquant-q-max
		int pngquantQualityMax = Integer.parseInt(pngquantQualityMaxText.getText());
		
		List<ImageInfoVO> imageList = AppManager.getImageInfoListByProjectName(projectInfoVO.getName());
		if (imageList.size() == 0) {
			msg = "当前项目 ["+projectInfoVO.getName()+"] 没有可合并的图片！";
			JOptionPane.showMessageDialog(AppMediator.getAppJFrame().getjFrame(), msg, "提示信息", JOptionPane.OK_OPTION);
			return; 
		}
		
		MegreAndPublishSwingWorker megreAndPublishSwingWorker = new MegreAndPublishSwingWorker(this.projectInfoVO, webpQuality, webpAlphaQuality, pngquantQualityMin, pngquantQualityMax, imageList);
		megreAndPublishSwingWorker.execute(); 
		
		//保存品质设置
		this.projectInfoVO.setWebpQuality(webpQuality);
		this.projectInfoVO.setWebpAlphaQuality(webpAlphaQuality);
		this.projectInfoVO.setPngquantQualityMin(pngquantQualityMin);
		this.projectInfoVO.setPngquantQualityMax(pngquantQualityMax);
		//写入项目
		AppManager.writeOneProjectConfig(this.projectInfoVO);
		//标识为强制更新图片 
		this.isForceUpdateMegreImage = true;
	}
	
	public void changeProjectInfoVO(ProjectInfoVO projectInfoVO) {
		//是否改变项目 
		//boolean projectIsChange = false;
		if (projectInfoVO != null) { 
//			if (this.projectInfoVO != null && projectInfoVO.getName().equals(this.projectInfoVO.getName())) {
//				projectIsChange = true; 
//			}
			this.projectInfoVO = projectInfoVO;
			
			projectNameText.setText(projectInfoVO.getName()); //项目名字 
			imageNumText.setText(""+projectInfoVO.getImageNum()); //图片数量  
			if (ProjectStatusEnum.NOT_CUT.equals(projectInfoVO.getStatus())) { 
				statusText.setForeground(Color.RED); 
				statusText.setText("还有图片未进行矩形裁剪，请点击“批量矩形裁切”");
			} else if (ProjectStatusEnum.CUT_RECT.equals(projectInfoVO.getStatus())) { 
				statusText.setForeground(Color.RED); 
				statusText.setText("已矩形裁剪，下一步操作，请点击“批量多边形运算”"); 
			} else if (ProjectStatusEnum.POLYGON_EXE.equals(projectInfoVO.getStatus())) { 
				statusText.setForeground(Color.BLUE);   
				statusText.setText("已进行多边形运算 ，可以合图");
			} 
			
			//设置压缩参数
			if (projectInfoVO.getWebpQuality() == 0) {
				this.webpQualityText.setText(""+webpQualityDefault); 
			} else {
				this.webpQualityText.setText(""+projectInfoVO.getWebpQuality()); 
			}
			if (projectInfoVO.getWebpAlphaQuality() == 0) {
				this.webpAlphaQualityText.setText(""+webpAlphaQualityDefault); 
			} else {
				this.webpAlphaQualityText.setText(""+projectInfoVO.getWebpAlphaQuality()); 
			}
			if (projectInfoVO.getPngquantQualityMin() == 0) {
				this.pngquantQualityMinText.setText(""+pngquantQualityMinDefault); 
			} else {
				this.pngquantQualityMinText.setText(""+projectInfoVO.getPngquantQualityMin()); 
			}
			if (projectInfoVO.getPngquantQualityMax() == 0) {
				this.pngquantQualityMaxText.setText(""+pngquantQualityMaxDefault); 
			} else {
				this.pngquantQualityMaxText.setText(""+projectInfoVO.getPngquantQualityMax()); 
			}		
		}
		
		//是否选中这个标签 
		boolean isSelectMagrePane = AppMediator.getAppJFrame().getTabbedPane().getSelectedIndex()==1; 
		if (isSelectMagrePane && this.projectInfoVO != null) {
			if (!this.projectInfoVO.getName().equals(this.megreImageProject) || this.isForceUpdateMegreImage) { 
				try {
					megreImageViewPane.removeAll();
					String publishSheetImagePath = AppManager.getPublishSheetImageHDPath(this.projectInfoVO); 
					//获得图片 
					File publishSheetImageFile = new File(publishSheetImagePath);
					if (publishSheetImageFile.exists()) { //存在则处理显示   
						BufferedImage publishSheetImage = ImageIO.read(publishSheetImageFile); 
						megreSizeText.setText(StringUtil.connectStr("(", publishSheetImage.getWidth(), "*", publishSheetImage.getHeight(),")"));
						//图片Lable  
						JLabel imageLable = new JLabel();
						ImageIcon cutViewImageIcon = new ImageIcon(publishSheetImage);  
						imageLable.setIcon(cutViewImageIcon);
						megreImageViewPane.add(imageLable);
						//最后修改时间 
						long lastModified = publishSheetImageFile.lastModified();
						String lastModifiedStr = DateUtil.timesFormatToSeconds(lastModified);
						lastModifiedText.setText(lastModifiedStr); 
					} else {
						megreSizeText.setText("暂无");   
						lastModifiedText.setText("暂无");
					} 
					megreImageViewPane.updateUI(); 
				} catch (Exception e) {
					e.printStackTrace();
				} 
				//更改图片对应项目 
				this.megreImageProject = this.projectInfoVO.getName();
				//更新完毕标识改变 
				this.isForceUpdateMegreImage = false;
			}
		}

	}
}
