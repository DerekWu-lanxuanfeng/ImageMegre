package com.flame.tools.megreimage.pane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.flame.tools.megreimage.AppManager;
import com.flame.tools.megreimage.AppMediator;
import com.flame.tools.megreimage.consts.ProjectStatusEnum;
import com.flame.tools.megreimage.util.StringUtil;
import com.flame.tools.megreimage.vo.ProjectInfoVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.ProjectInfoPane.java
 * @Description: 项目信息面板
 * @Create: DerekWu  2016年4月19日 上午10:59:36 
 * @version: V1.0 
 */
public class ProjectInfoPane {

	/** 基本信息面板 */
	private JPanel projectPane;
	/** 项目信息对象 */
	private ProjectInfoVO projectInfoVO;
	
	/** 项目信息top面板  */
	private JPanel projectPaneTop;
	/** 当前项目名 */
	private JLabel currProjectName;
	/** 删除按钮  */
	private JButton deleteButton;
	/** 导入纹理  */
	private JButton importButton;
	/** 批量多边形运算  */
	private JButton batchExeButton;
	/** 项目信息center面板  */
	private JPanel projectPaneCenter;
	
	/** 项目状态  */
	private JLabel statusInfo;
	/** 使用目录 */
	private JLabel useDirectoryInfo;
	/** 合图文件名 */
	private JLabel fileNameInfo;
	
	/** 合图大小  */
	private JLabel megreSizeInfo;
	/** 图片总数  */
	private JLabel imageNumInfo;
	/** 已运算数量  */
	private JLabel exeNumInfo;
	/** 未运算数量  */
	private JLabel noExeNumInfo;
	
	/** 8边形数量 */
	private JLabel eightNumInfo;
	/** 7变形数量  */
	private JLabel sevenNumInfo;
	/** 6变形数量  */
	private JLabel sixNumInfo;
	/** 5变形数量  */
	private JLabel fiveNumInfo;
	/** 4边形数量  */
	private JLabel fourNumInfo;
	
	/** 设置提示 */
	private JLabel setInfo;
	
	/** 多边形宽高限制 */
	private JTextField widthAndHeigthText;
	
	/** 裁切面积限制 */
	private JTextField cutAreaText;
	
	/** 裁切边长限制 */
	private JTextField cutBorderLengthText;
	
	public void init() {
		projectPane = new JPanel(); 
		projectPane.setLayout(new BorderLayout()); 
		//初始化顶部面板  
		this.initTopPane(); 
		//初始化顶部面板 
		this.initCenterPane(); 
		//初始化底部面板
		this.initBottomPane();
	}

	/**
	 * 初始化顶部面板 
	 */
	private void initTopPane() {
		/** 顶部面板  */
		projectPaneTop = new JPanel();
		FlowLayout oneFlowLayout = new FlowLayout();
		oneFlowLayout.setAlignment(FlowLayout.LEFT);
		projectPaneTop.setLayout(oneFlowLayout); 
		projectPaneTop.setBorder(BorderFactory.createEtchedBorder());
		
		
		JLabel typeLable = new JLabel();
		typeLable.setText("项目名称："); 
		projectPaneTop.add(typeLable);
		
		currProjectName = new JLabel();
		currProjectName.setText("防御和兵种");
		currProjectName.setPreferredSize(new Dimension(200, 30)); 
		projectPaneTop.add(currProjectName);
		
		//删除项目
		deleteButton = new JButton("删除项目");
		projectPaneTop.add(deleteButton);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "删除后的项目不在项目列表出现，但是不会删除具体目录!\n您还可以通过[文件]==>[导入合图项目] 重新导入！\n您确定要删除吗？";
				String title = "您确定要删除删除该项目吗？";
				int ret = JOptionPane.showConfirmDialog(AppMediator.getAppJFrame().getjFrame(), msg, title, JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					//删除项目 
					AppMediator.deleteProject(projectInfoVO); 
				}
			}
		});
		
		//导入纹理 
		importButton = new JButton("导入图片纹理");
		projectPaneTop.add(importButton);
		importButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppManager.importImage(projectInfoVO); 
			}
		});
		
		//矩形裁切运算
		batchExeButton = new JButton("批量矩形裁切运算");
		projectPaneTop.add(batchExeButton);
		batchExeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppManager.batchCutRect(projectInfoVO); 
			}
		});
		
		JButton batchPolygonButton = new JButton("批量多边形组合运算");
		projectPaneTop.add(batchPolygonButton);
		batchPolygonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppManager.batchPolygonExe(projectInfoVO); 
			}
		});
		
		projectPane.add(projectPaneTop, BorderLayout.NORTH);
	}
	
	/**
	 * 初始化中间面板 
	 */
	private void initCenterPane() { 
		projectPaneCenter = new JPanel(); 
		FlowLayout oneFlowLayout = new FlowLayout();
		oneFlowLayout.setAlignment(FlowLayout.LEFT);
		projectPaneCenter.setLayout(oneFlowLayout); 
		projectPaneCenter.setBorder(BorderFactory.createEtchedBorder());
		projectPaneCenter.setPreferredSize(new Dimension(600, 260)); 
		
		JPanel oneGridBagLayoutPane = new JPanel(); 
		GridBagLayout layout = new GridBagLayout(); 
		oneGridBagLayoutPane.setLayout(layout);   
		
		//项目状态 
		JLabel status = new JLabel();
		status.setText("项目状态： "); 
		oneGridBagLayoutPane.add(status); 
		statusInfo = new JLabel();
		statusInfo.setText("还有图片未进行矩形裁剪，请点击“批量矩形裁切”"); 
		oneGridBagLayoutPane.add(statusInfo);
				
				
		//使用目录
		JLabel useDirectory = new JLabel();
		useDirectory.setText("图片所在目录： "); 
		oneGridBagLayoutPane.add(useDirectory);
		useDirectoryInfo = new JLabel();
		useDirectoryInfo.setText("D:/jjjjjjj"); 
		oneGridBagLayoutPane.add(useDirectoryInfo);
		
		//合图文件名
		JLabel fileName = new JLabel();
		fileName.setText("合图文件名： "); 
		oneGridBagLayoutPane.add(fileName);
		fileNameInfo = new JLabel(); 
		fileNameInfo.setText("ddd"); 
		oneGridBagLayoutPane.add(fileNameInfo);
		
		//换行
		JLabel lineBreak1 = new JLabel(" ");
		oneGridBagLayoutPane.add(lineBreak1);
		
		//合图大小 
		JLabel megreSize = new JLabel(); 
		megreSize.setText("合图大小： "); 
		oneGridBagLayoutPane.add(megreSize); 
		megreSizeInfo = new JLabel();
		megreSizeInfo.setText("在合并纹理时确定，这里不做规定");  
		oneGridBagLayoutPane.add(megreSizeInfo); 
		
//		JLabel megreSize = new JLabel();
//		megreSize.setText("合图大小： "); 
//		oneGridBagLayoutPane.add(megreSize);
//		JPanel megreSizeSelectPane = new JPanel();
//		FlowLayout megreLayout = new FlowLayout();
//		megreLayout.setAlignment(FlowLayout.LEFT);
//		megreSizeSelectPane.setLayout(megreLayout);  
//		oneGridBagLayoutPane.add(megreSizeSelectPane);
//		
//		ButtonGroup group = new ButtonGroup();
//		JRadioButton radio256 = new JRadioButton("256*256");
//		group.add(radio256);
//		megreSizeSelectPane.add(radio256);
//		JRadioButton radio512 = new JRadioButton("512*512"); 
//		group.add(radio512);
//		megreSizeSelectPane.add(radio512);
//		JRadioButton radio1024 = new JRadioButton("1024*1024");
//		group.add(radio1024);
//		megreSizeSelectPane.add(radio1024);
//		JRadioButton radio2048 = new JRadioButton("2048*2048", true);
//		group.add(radio2048);
//		megreSizeSelectPane.add(radio2048);
//		JRadioButton radio4096 = new JRadioButton("4096*4096");
//		group.add(radio4096);
//		megreSizeSelectPane.add(radio4096); 

		
		//图片数量 
		JLabel imageNum = new JLabel();
		imageNum.setText("图片总数量： "); 
		oneGridBagLayoutPane.add(imageNum);
		imageNumInfo = new JLabel(); 
		imageNumInfo.setText("1000个 "); 
		oneGridBagLayoutPane.add(imageNumInfo);
		
		//已运算图片
		JLabel exeNum = new JLabel();
		exeNum.setText("已运算数量： "); 
		oneGridBagLayoutPane.add(exeNum);
		exeNumInfo = new JLabel(); 
		exeNumInfo.setText("1000个 "); 
		oneGridBagLayoutPane.add(exeNumInfo);
		
		//未运算图片
		JLabel noExeNum = new JLabel();
		noExeNum.setText("未运算数量： "); 
		oneGridBagLayoutPane.add(noExeNum);
		noExeNumInfo = new JLabel(); 
		noExeNumInfo.setText("1000个 "); 
		oneGridBagLayoutPane.add(noExeNumInfo);
		
		//换行
		JLabel lineBreak2 = new JLabel(" ");
		oneGridBagLayoutPane.add(lineBreak2);
		
		//8边形数量
		JLabel eightNum = new JLabel();
		eightNum.setText("8边形数量： "); 
		oneGridBagLayoutPane.add(eightNum);
		eightNumInfo = new JLabel(); 
		eightNumInfo.setText("1000个 "); 
		oneGridBagLayoutPane.add(eightNumInfo);
		
		//7边形数量
		JLabel sevenNum = new JLabel();
		sevenNum.setText("7边形数量： "); 
		oneGridBagLayoutPane.add(sevenNum);
		sevenNumInfo = new JLabel(); 
		sevenNumInfo.setText("1000个 "); 
		oneGridBagLayoutPane.add(sevenNumInfo);
		
		//6边形数量
		JLabel sixNum = new JLabel();
		sixNum.setText("6边形数量： "); 
		oneGridBagLayoutPane.add(sixNum);
		sixNumInfo = new JLabel(); 
		sixNumInfo.setText("1000个 "); 
		oneGridBagLayoutPane.add(sixNumInfo);
		
		//5边形数量
		JLabel fiveNum = new JLabel();
		fiveNum.setText("5边形数量： "); 
		oneGridBagLayoutPane.add(fiveNum);
		fiveNumInfo = new JLabel(); 
		fiveNumInfo.setText("1000个 "); 
		oneGridBagLayoutPane.add(fiveNumInfo);
		
		//4边形数量
		JLabel fourNum = new JLabel();
		fourNum.setText("4边形数量： "); 
		oneGridBagLayoutPane.add(fourNum);
		fourNumInfo = new JLabel(); 
		fourNumInfo.setText("1000个 "); 
		oneGridBagLayoutPane.add(fourNumInfo);

		GridBagConstraints s= new GridBagConstraints();//定义一个GridBagConstraints，
        //是用来控制添加进的组件的显示位置
        s.fill = GridBagConstraints.NONE; 
        //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
        //NONE：不调整组件大小。
        //HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
        //VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
        //BOTH：使组件完全填满其显示区域。
        
        s.gridwidth = 1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        s.weighty = 0;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(status, s);//设置组件
        
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(statusInfo, s);
        
		s.gridwidth = 1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        s.weighty = 0;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(useDirectory, s);//设置组件
        
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(useDirectoryInfo, s);
        
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(fileName, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(fileNameInfo, s);
        
        s.gridwidth = 0;
        s.weightx = 0;
        s.weighty = 0;
        layout.setConstraints(lineBreak1, s);
        
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(megreSize, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(megreSizeInfo, s);
        
//        s.gridwidth = 1;
//        s.weightx = 0;
//        s.weighty = 0;
//        s.anchor = GridBagConstraints.EAST;
//        layout.setConstraints(megreSize, s);
//        
//        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
//        s.weightx = 0;
//        s.weighty=0;
//        s.anchor = GridBagConstraints.WEST;
//        layout.setConstraints(megreSizeSelectPane, s);
        
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(imageNum, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(imageNumInfo, s);
        
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(exeNum, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(exeNumInfo, s);
        
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(noExeNum, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(noExeNumInfo, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        layout.setConstraints(lineBreak2, s);
        
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(eightNum, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(eightNumInfo, s);
        
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(sevenNum, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(sevenNumInfo, s);
        
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(sixNum, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(sixNumInfo, s);
        
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(fiveNum, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(fiveNumInfo, s);
        
        s.gridwidth = 1;
        s.weightx = 0;
        s.weighty = 0;
        s.anchor = GridBagConstraints.EAST;
        layout.setConstraints(fourNum, s);
        
        s.gridwidth=0;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;
        s.weighty=0;
        s.anchor = GridBagConstraints.WEST;
        layout.setConstraints(fourNumInfo, s);
 
		projectPaneCenter.add(oneGridBagLayoutPane);
		projectPane.add(projectPaneCenter, BorderLayout.CENTER);
	}
	
	private void initBottomPane () {
		JPanel projectPaneBottom = new JPanel();
		projectPaneBottom.setLayout(null); 
		projectPaneBottom.setBorder(BorderFactory.createTitledBorder("多边形运算设置"));
		projectPaneBottom.setPreferredSize(new Dimension(460, 220)); 
		
		//提示信息 
		setInfo = new JLabel("填写信息后点击“保存”按钮进行设置！",JLabel.CENTER);
		setInfo.setForeground(Color.BLUE);  
		setInfo.setBounds(10, 10, 380, 30);  
		projectPaneBottom.add(setInfo); 
		
		//图片宽高限制
		JLabel widthAndHeight = new JLabel();
		widthAndHeight.setText("图片宽高限制： "); 
		widthAndHeight.setBounds(10, 50, 100, 30);
		projectPaneBottom.add(widthAndHeight);
		
		widthAndHeigthText = new JTextField(); 
		widthAndHeigthText.setText("50"); 
		widthAndHeigthText.setBounds(110, 50, 50, 30);
		projectPaneBottom.add(widthAndHeigthText); 
		
		JLabel widthAndHeightDesc = new JLabel();
		widthAndHeightDesc.setText(" 图片宽或高小于这个数则不进行多边形运算"); 
		widthAndHeightDesc.setBounds(180, 50, 260, 30);
		projectPaneBottom.add(widthAndHeightDesc); 
		
		//裁切面积限制 
		JLabel cutArea = new JLabel();
		cutArea.setText("裁切面积设置： "); 
		cutArea.setBounds(10, 90, 100, 30);
		projectPaneBottom.add(cutArea);
		
		cutAreaText = new JTextField(); 
		cutAreaText.setText("800"); 
		cutAreaText.setBounds(110, 90, 50, 30);
		projectPaneBottom.add(cutAreaText); 
		
		JLabel cutAreaDesc = new JLabel();
		cutAreaDesc.setText(" 裁切下来的三角形面积小于这个数则不处理"); 
		cutAreaDesc.setBounds(180, 90, 260, 30);
		projectPaneBottom.add(cutAreaDesc); 
		
		//裁切边长限制
		JLabel cutBorderLength = new JLabel();
		cutBorderLength.setText("裁切边长限制： "); 
		cutBorderLength.setBounds(10, 130, 100, 30);
		projectPaneBottom.add(cutBorderLength);
		
		cutBorderLengthText = new JTextField(); 
		cutBorderLengthText.setText("20"); 
		cutBorderLengthText.setBounds(110, 130, 50, 30);
		projectPaneBottom.add(cutBorderLengthText); 
		
		JLabel cutBorderLengthTextDesc = new JLabel();
		cutBorderLengthTextDesc.setText(" 裁切下来的三角形有直角边的长小于这个数则不处理"); 
		cutBorderLengthTextDesc.setBounds(180, 130, 260, 30);
		projectPaneBottom.add(cutBorderLengthTextDesc); 
		
		//保存按钮
		JButton saveButton = new JButton("保存");
		saveButton.setBounds(150, 170, 60, 30);
		projectPaneBottom.add(saveButton); 
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("".equals(widthAndHeigthText.getText())) {
					setInfo.setForeground(Color.RED);
					setInfo.setText("请填写图片宽高限制！");
					return;
				} else if (!widthAndHeigthText.getText().matches("[0-9]+")) {
					setInfo.setForeground(Color.RED);
					setInfo.setText("图片宽高限制必须填写数字！");
					return;
				}
				
				if ("".equals(cutAreaText.getText())) {
					setInfo.setForeground(Color.RED);
					setInfo.setText("请选择裁切面积限制！");
					return;
				} else if (!cutAreaText.getText().matches("[0-9]+")) {
					setInfo.setForeground(Color.RED);
					setInfo.setText("裁切面积限制必须填写数字！");
					return;
				}
				
				if ("".equals(cutBorderLengthText.getText())) {
					setInfo.setForeground(Color.RED);
					setInfo.setText("请填写裁切边长限制！");
					return;
				} else if (!cutBorderLengthText.getText().matches("[0-9]+")) {
					setInfo.setForeground(Color.RED);
					setInfo.setText("裁切边长限制必须填写数字！");
					return;
				}
				
				projectInfoVO.setPolygonImageSizeLimit(Integer.parseInt(widthAndHeigthText.getText()));
				projectInfoVO.setPolygonCutAreaLimit(Integer.parseInt(cutAreaText.getText()));
				projectInfoVO.setPolygonCutBorderLengthLimit(Integer.parseInt(cutBorderLengthText.getText()));
				
				AppManager.writeOneProjectConfig(projectInfoVO);
				setInfo.setForeground(Color.BLUE);
				setInfo.setText("保存成功！");
			}
		});
		
		projectPaneCenter.add(projectPaneBottom); 
	}
	
	public void changeProjectInfoVO(ProjectInfoVO projectInfoVO) { 
		this.projectInfoVO = projectInfoVO;
		if (ProjectStatusEnum.NOT_CUT.equals(projectInfoVO.getStatus())) {
			statusInfo.setForeground(Color.RED); 
			statusInfo.setText("还有图片未进行矩形裁剪，请点击“批量矩形裁切”");
		} else if (ProjectStatusEnum.CUT_RECT.equals(projectInfoVO.getStatus())) {
			statusInfo.setForeground(Color.RED); 
			statusInfo.setText("已矩形裁剪，下一步操作，请点击“批量多边形运算”");
		} else if (ProjectStatusEnum.POLYGON_EXE.equals(projectInfoVO.getStatus())) {
			statusInfo.setForeground(Color.BLUE);   
			statusInfo.setText("已进行多边形运算 ，可以合图");
		} 

		setInfo.setText("填写信息后点击“保存”按钮进行设置！"); 
		currProjectName.setText(projectInfoVO.getName()); //项目名称 
		useDirectoryInfo.setText(projectInfoVO.getUseDirectory()); //使用目录
		fileNameInfo.setText(projectInfoVO.getFileName());//合图文件名 
		
		if (projectInfoVO.getMegreImageWidth() > 0) {
			megreSizeInfo.setText(StringUtil.connectStr("(", projectInfoVO.getMegreImageWidth(), "*", projectInfoVO.getMegreImageHeight(), ")")); //合图尺寸
		} else {
			megreSizeInfo.setText("在合并纹理时确定，这里不做规定");  
		}
		imageNumInfo.setText(projectInfoVO.getImageNum() + "个"); //图片总数 
		exeNumInfo.setText(projectInfoVO.getExedNum() + "个");//已运算数量 
		noExeNumInfo.setText(projectInfoVO.getNoExedNum() + "个");//未运算数量 
		
		eightNumInfo.setText(projectInfoVO.getEightPolygonNum() + "个");//8边形数量
		sevenNumInfo.setText(projectInfoVO.getSevenPolygonNum() + "个");//7边形数量
		sixNumInfo.setText(projectInfoVO.getSixPolygonNum() + "个");//6边形数量
		fiveNumInfo.setText(projectInfoVO.getFivePolygonNum() + "个");//5边形数量
		fourNumInfo.setText(projectInfoVO.getFourPolygonNum() + "个");//4边形数量

		widthAndHeigthText.setText(""+projectInfoVO.getPolygonImageSizeLimit()); 
		cutAreaText.setText(""+projectInfoVO.getPolygonCutAreaLimit()); 
		cutBorderLengthText.setText(""+projectInfoVO.getPolygonCutBorderLengthLimit()); 
		
	}

	public JPanel getProjectPane() {
		return projectPane;
	}

	public void setProjectPane(JPanel projectPane) {
		this.projectPane = projectPane;
	}

	public ProjectInfoVO getProjectInfoVO() {
		return projectInfoVO;
	}

	public void setProjectInfoVO(ProjectInfoVO projectInfoVO) {
		this.projectInfoVO = projectInfoVO;
	}

}
