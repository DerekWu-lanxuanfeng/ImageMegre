package com.flame.tools.megreimage.listeners;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.flame.tools.megreimage.AppMediator;
import com.flame.tools.megreimage.vo.ImageInfoVO;
import com.flame.tools.megreimage.vo.ProjectInfoVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.listeners.MyTreeSelectionListener.java
 * @Description: 树鼠标监听器
 * @Create: DerekWu  2016年4月19日 下午4:20:11 
 * @version: V1.0 
 */
public class MyTreeSelectionListener implements TreeSelectionListener {
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {

		JTree tree = (JTree)e.getSource();
		// JTree的getRowForLocation()方法会返回节点的列索引值。例如本例中，“本机磁盘(D:)”的列索引值为4,此索引值
		// 会随着其他数据夹的打开或收起而变支，但“资源管理器”的列索引值恒为0.
//		int rowLocation = tree.getRowForLocation(e.getX(), e.getY());

		/*
		 * JTree的getPathForRow()方法会取得从root
		 * node到点选节点的一条path,此path为一条直线，如程序运行的图示若你点选“本机磁盘(E:)”,则Tree
		 * Path为"资源管理器"-->"我的电脑"-->"本机磁盘(E:)",因此利用TreePath
		 * 的getLastPathComponent()方法就可以取得所点选的节点.
		 */

//		TreePath treepath = tree.getPathForRow(rowLocation); 
//		TreePath treepath = tree.getSelectionPath();
//		if (treepath == null) return;
		
		DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

//		String nodeName = treenode.toString();
//		System.out.println("---:"+nodeName); 	
		if (treenode == null) return;
		Object userObject = treenode.getUserObject(); 
		if (userObject instanceof ProjectInfoVO) { 
			//选中项目
			AppMediator.selectProjectInTree((ProjectInfoVO)userObject);
		} else if (userObject instanceof ImageInfoVO) { 
			//选中树 
			AppMediator.selectImageInTree((ImageInfoVO)userObject);
		}
	
	}

}
