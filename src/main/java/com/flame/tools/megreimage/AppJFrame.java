package com.flame.tools.megreimage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.flame.tools.megreimage.pane.ImageInfoPane;
import com.flame.tools.megreimage.pane.MegreImagePane;
import com.flame.tools.megreimage.pane.ProjectInfoPane;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.ProjectInfoVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MagreImage 
 * @File: com.flame.tools.megreimage.AppJFrame.java
 * @Description: MYJFrame
 * @Create: DerekWu  2016年4月18日 下午9:36:05 
 * @version: V1.0 
 */
public class AppJFrame {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;
	
	/** 主面板 */
	private JFrame jFrame;
	
	/** 新建合图项目  */
	private JMenuItem newProjectMenuItem;
	
	/** 导入合图项目  */
	private JMenuItem importProjectMenuItem;
	
	/** 左面板 */
    private JPanel leftJPanel;
    /** 右面板 */
    private JPanel rightJPanel;
    
	/** 左边树结构 */
	private JTree tree;
	private DefaultMutableTreeNode root; 
	
	/** 标签页控件 */
	private JTabbedPane tabbedPane;
	
	/** 基本信息面板 */
	private JPanel infoPane;
	
	/** 合并图片面板  */
	private MegreImagePane megreImagePane;
	
	/** 项目信息 */
	private ProjectInfoPane projectInfoPane;
	
	/** 图片信息面板 */
	private ImageInfoPane imageInfoPane; 
	
	public AppJFrame() {
		jFrame = new JFrame();
		jFrame.setTitle("OpenGL纹理合并工具"); 
		jFrame.setSize(WIDTH, HEIGHT);
		jFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.initMenuBar();
		this.initMain(); 
		//this.initInfoPanel(); 
	} 
	
	public void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		jFrame.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("文件");
//		JMenu helpMenu = new JMenu("帮助");
		
		menuBar.add(fileMenu);
//		menuBar.add(helpMenu);
		
		newProjectMenuItem = new JMenuItem(" 新建合图项目");
		importProjectMenuItem = new JMenuItem(" 导入合图项目");
		
		fileMenu.add(newProjectMenuItem);
		fileMenu.add(importProjectMenuItem); 
		
//		JMenuItem aboutMenuItem = new JMenuItem(" 关于");
//		helpMenu.add(aboutMenuItem);
	}
	
	/**
	 * 初始化主面板，包括左右布局
	 */
	public void initMain() {
		
		//左侧数 
		root=new DefaultMutableTreeNode("根目录");	
	    tree = new JTree(root);
	    tree.setLayout(new BorderLayout());
	    
	    //左面板
		leftJPanel = new JPanel();
		leftJPanel.setLayout(new BorderLayout());
		
		//右面板
		rightJPanel = new JPanel();
		rightJPanel.setLayout(new BorderLayout());

		//分割面板，来装左右面板 
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		splitPane.setPreferredSize(new Dimension(200, 500));
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(leftJPanel);
		splitPane.setRightComponent(rightJPanel);
		splitPane.setDividerSize(10);
		splitPane.setDividerLocation(250);
		jFrame.setContentPane(splitPane);
		
		//滑动面板来装树
		JScrollPane scrollpane = new JScrollPane(tree);
//		scrollpane.setLayout(new BorderLayout());
	    leftJPanel.add(scrollpane);
	    
	    //标签面板
	    tabbedPane = new JTabbedPane();
	    tabbedPane.setLayout(new BorderLayout());
	    
	    //信息面板
	    infoPane = new JPanel();
	    infoPane.setLayout(new BorderLayout());
	    
	    //合并面板
	    megreImagePane = new MegreImagePane(); 
	    
	    tabbedPane.addTab("infoPane", infoPane);
	    tabbedPane.setEnabledAt(0, true); 
	    tabbedPane.setTitleAt(0, "信息"); 
	    
	    tabbedPane.addTab("megrePane", megreImagePane);
	    tabbedPane.setEnabledAt(1, true); 
	    tabbedPane.setTitleAt(1, "合并纹理"); 
	    
	    tabbedPane.setTabPlacement(SwingConstants.TOP);
	    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	    
	    rightJPanel.add(tabbedPane, BorderLayout.CENTER);
	    
	    tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
//				String name = tabbedPane.getSelectedComponent().getName();  
				if (tabbedPane.getSelectedComponent().equals(megreImagePane)) { 
					selectMegrePane(null); 
				}
			}
	    });
	    
	    JLabel oneLable = new JLabel();
	    oneLable.setText("  ©2016   深圳市烈焰时代科级有限公司. 版权所有."); 
	    //oneLable.setPreferredSize(new Dimension(50, 50));
	    
	    rightJPanel.add(oneLable, BorderLayout.SOUTH);
	}
	
	/**
	 * 改变项目对象 
	 * @param projectInfoVO
	 */
	public void chengeProjectVO(ProjectInfoVO projectInfoVO) {
		if (projectInfoPane == null) { 
			projectInfoPane = new ProjectInfoPane();
			projectInfoPane.init();
		} 
		projectInfoPane.changeProjectInfoVO(projectInfoVO); 
		megreImagePane.changeProjectInfoVO(projectInfoVO); 
		infoPane.removeAll();
		infoPane.add(projectInfoPane.getProjectPane(), BorderLayout.CENTER);
		infoPane.updateUI();
	}
	
	/**
	 * 改变项目对象 
	 * @param projectInfoVO
	 */
	public void chengeImageInfoVO(ImageInfoVO imageInfoVO) {
		if (imageInfoPane == null) { 
			imageInfoPane = new ImageInfoPane(); 
		} 
		imageInfoPane.chengeImageInfoVO(imageInfoVO); 
		ProjectInfoVO projectInfoVO = AppManager.getProjectInfoVO(imageInfoVO.getProjectName()); 
		megreImagePane.changeProjectInfoVO(projectInfoVO); 
		infoPane.removeAll();
		infoPane.add(imageInfoPane, BorderLayout.CENTER); 
		infoPane.updateUI();
	}
	
	/**
	 * 选中合并界面 
	 * @param projectInfoVO
	 */
	public void selectMegrePane(ProjectInfoVO projectInfoVO) {  
		megreImagePane.changeProjectInfoVO(projectInfoVO);   
	}

	public JFrame getjFrame() {
		return jFrame;
	}

	public JTree getTree() {
		return tree;
	}

	public DefaultMutableTreeNode getRoot() {
		return root;
	}

	public JMenuItem getNewProjectMenuItem() {
		return newProjectMenuItem;
	}

	public JMenuItem getImportProjectMenuItem() {
		return importProjectMenuItem;
	}
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public JPanel getInfoPane() {
		return infoPane;
	}

}
