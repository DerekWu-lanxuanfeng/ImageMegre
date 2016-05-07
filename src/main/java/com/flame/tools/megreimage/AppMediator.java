package com.flame.tools.megreimage;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.flame.tools.megreimage.listeners.ImportProjectActionListener;
import com.flame.tools.megreimage.listeners.MyTreeSelectionListener;
import com.flame.tools.megreimage.listeners.NewProjectActionListener;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.ProjectInfoVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MagreImage 
 * @File: com.flame.tools.megreimage.AppMediator.java
 * @Description: 调停者
 * @Create: DerekWu  2016年4月18日 下午10:24:26 
 * @version: V1.0 
 */
public class AppMediator { 

	private static AppJFrame appJFrame;  
	
	public AppMediator() {
		AppJFrame appJFrame = new AppJFrame(); 
		AppMediator.appJFrame = appJFrame;  
		appJFrame.getjFrame().setVisible(true);
	}
	
	public static AppJFrame getAppJFrame() {
		return appJFrame;
	}
	
	public void initData() {
		//初始化menu菜单
		this.initMenuItem();
		this.initTreeData();
	}
	
	private void initMenuItem() {
		//注册监听点击事件
		appJFrame.getNewProjectMenuItem().addActionListener(new NewProjectActionListener());  
		appJFrame.getImportProjectMenuItem().addActionListener(new ImportProjectActionListener()); 
	}
	
	private void initTreeData() {
		DefaultMutableTreeNode root = appJFrame.getRoot();
		
		List<ProjectInfoVO> projectList = AppManager.getProjectList();
		for (ProjectInfoVO oneProject:projectList) { 
			DefaultMutableTreeNode oneProjectNode = new DefaultMutableTreeNode(oneProject);
			root.add(oneProjectNode); 
		} 
		
		appJFrame.getTree().addTreeSelectionListener(new MyTreeSelectionListener()); 
		appJFrame.getTree().updateUI(); 
	}

	/**
	 * 添加项目到树 
	 * @param projectInfoVO
	 */
	public static void addProjectToTree(ProjectInfoVO projectInfoVO) {
		DefaultMutableTreeNode root = appJFrame.getRoot();
		DefaultMutableTreeNode oneProjectNode = new DefaultMutableTreeNode(projectInfoVO);
		root.add(oneProjectNode); 
		//选中新创建的项目 
		TreePath treePath = new TreePath(oneProjectNode.getPath());
		appJFrame.getTree().setSelectionPath(treePath); 
		appJFrame.getTree().updateUI(); 
	}
	
	/**
	 * 选中树中的项目
	 * @param projectInfoVO
	 */
	public static void selectProjectInTree(ProjectInfoVO projectInfoVO) {
		appJFrame.chengeProjectVO(projectInfoVO); 
	}
	
	/**
	 * 一个项目执行导入图片纹理之后 
	 * @param projectInfoVO
	 */
	public static void oneProjectExeImportImage(ProjectInfoVO projectInfoVO) {
		//获得项目图片列表 
		List<ImageInfoVO> imageList = AppManager.getImageInfoListByProjectName(projectInfoVO.getName());
		//先删除所有，再重新添加 
		DefaultMutableTreeNode root = appJFrame.getRoot();
		int projectNum = root.getChildCount();
		DefaultMutableTreeNode currProjectNode = null;
		for (int i=0; i<projectNum; ++i) {
			DefaultMutableTreeNode oneNode = (DefaultMutableTreeNode)root.getChildAt(i);
			if (projectInfoVO.equals(oneNode.getUserObject())) { 
				currProjectNode = oneNode;
				break;
			}
		}
		currProjectNode.removeAllChildren(); 
		for (ImageInfoVO oneImageInfoVO:imageList) {
			DefaultMutableTreeNode oneProjectNode = new DefaultMutableTreeNode(oneImageInfoVO);
			currProjectNode.add(oneProjectNode); 
		}
		//选中新创建的项目 
		TreePath treePath = new TreePath(currProjectNode.getPath()); 
		appJFrame.getTree().setSelectionPath(treePath); 
		appJFrame.chengeProjectVO(projectInfoVO); 
		appJFrame.getTree().updateUI(); 
	}
	
	/**
	 * 删除项目
	 * @param projectInfoVO
	 */
	public static void deleteProject(ProjectInfoVO projectInfoVO) { 
		//删除存储
		AppManager.deleteProjectData(projectInfoVO);
		DefaultMutableTreeNode root = appJFrame.getRoot();
		int projectNum = root.getChildCount();
		DefaultMutableTreeNode currProjectNode = null;
		for (int i=0; i<projectNum; ++i) {
			DefaultMutableTreeNode oneNode = (DefaultMutableTreeNode)root.getChildAt(i);
			if (projectInfoVO.equals(oneNode.getUserObject())) { 
				currProjectNode = oneNode;
				break;
			}
		}
		
		if (currProjectNode != null) {
			root.remove(currProjectNode); 
		}
		
		if (root.getChildCount() > 0) { 
			//选中第一个节点 
			DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode)root.getFirstChild();
			TreePath treePath = new TreePath(firstNode.getPath());
			appJFrame.getTree().setSelectionPath(treePath); 
		} else {
			appJFrame.getInfoPane().removeAll(); 
			appJFrame.getInfoPane().updateUI(); 
		}
		
		appJFrame.getTree().updateUI(); 
	}
	
	/**
	 * 选中数中的图片 
	 * @param imageInfoVO
	 */
	public static void selectImageInTree(ImageInfoVO imageInfoVO) {  
		appJFrame.chengeImageInfoVO(imageInfoVO);  
	}
	
	/**
	 * 选中数中的图片 
	 * @param imageInfoVO
	 */
	public static void megreAndPublishOver(ProjectInfoVO projectInfoVO) {  
		appJFrame.selectMegrePane(projectInfoVO);  
	}
	
}
