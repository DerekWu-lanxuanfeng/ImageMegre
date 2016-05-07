package com.flame.tools.megreimage.listeners;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.flame.tools.megreimage.AppManager;
import com.flame.tools.megreimage.AppMediator;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.listeners.NewProjectActionListener.java
 * @Description: 新建项目
 * @Create: DerekWu  2016年4月19日 下午9:14:45 
 * @version: V1.0 
 */
public class NewProjectActionListener implements ActionListener {
	
	/** 新建项目面板*/
	private JDialog newProjectDialog;
	
	/** 信息文本 */
	private JLabel infoLabel;
	
	/** 项目名字text */
	private JTextField projectNameText;
	
	/** 图片目录text */
	private JTextField imagePathText;
	
	/** 导出文件名text */
	private JTextField exportNameText;
	
	/** 文件选择面板 */
	private JFileChooser selectDirPlan;
	
	/** 确定按钮 */
	private JButton confirmButton;
	
	private static final int NEW_WIDTH = 500;
	private static final int NEW_HEIGHT = 300;

	@Override
	public void actionPerformed(ActionEvent e) { 
		this.initNewProjectDialog();
	}
	
	private void initNewProjectDialog() {
		if (newProjectDialog == null) { 
			newProjectDialog = new JDialog(AppMediator.getAppJFrame().getjFrame());  
			newProjectDialog.setTitle("新建合图项目"); 
			newProjectDialog.setSize(NEW_WIDTH, NEW_HEIGHT);  
			newProjectDialog.setLocationRelativeTo(AppMediator.getAppJFrame().getjFrame());
			newProjectDialog.setModal(true); 
			newProjectDialog.setLayout(null); 
			
			//信息文本
			infoLabel = new JLabel("请填写以下信息！ ", SwingConstants.CENTER);
//			infoLabel.setText("请填写以下信息！ "); 
//			infoLabel.setForeground(Color.BLUE);
			
			//填写合图项目名
			JLabel projectName = new JLabel();
			projectName.setText("  填写合图项目名： "); 
			//项目名字填写框
			projectNameText = new JTextField();
			
			//图片路径
			JLabel imagePath = new JLabel(); 
			imagePath.setText("选择图片所在目录： ");  
			//图片路径填写框
			imagePathText = new JTextField(); 
			//选择按钮
			JButton selectPathButton = new JButton("选择");

			
			//填写合图导出文件名：
			JLabel exportName = new JLabel(); 
			exportName.setText("填写合图的文件名： ");  
			//导出文件名填写框
			exportNameText = new JTextField(); 

			//确定新建
			confirmButton = new JButton("确定新建 ");
			
			//设置位置
			int labelWidth = 120, labelHeight = 30, textWidth = 240, textHeight = 30;
			
			infoLabel.setBounds(30, 10, 440, labelHeight); 
			projectName.setBounds(30, 60, labelWidth, labelHeight); 
			projectNameText.setBounds(160, 60, textWidth, textHeight); 
			imagePath.setBounds(30, 110, labelWidth, labelHeight); 
			imagePathText.setBounds(160, 110, textWidth, textHeight); 
			selectPathButton.setBounds(410, 110, 60, 26); 
			exportName.setBounds(30, 160, labelWidth, labelHeight); 
			exportNameText.setBounds(160, 160, textWidth, textHeight); 
			confirmButton.setBounds(200, 210, 100, 26); 
			
			newProjectDialog.add(infoLabel); 
			newProjectDialog.add(projectName); 
			newProjectDialog.add(projectNameText); 
			newProjectDialog.add(imagePath); 
			newProjectDialog.add(imagePathText); 
			newProjectDialog.add(selectPathButton); 
			newProjectDialog.add(exportName); 
			newProjectDialog.add(exportNameText); 
			newProjectDialog.add(confirmButton); 
			
			//目录选择
			selectDirPlan = new JFileChooser("E:");  
			//jfc.setCurrentDirectory(new File("d://"));// 文件选择器的初始目录定为d盘  
			selectPathButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectDirPlan.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 设定只能选择到文件夹   
//					selectDirPlan.setFileSelectionMode(JFileChooser.FILES_ONLY);// 设定只能选择到文件   
		            int state = selectDirPlan.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句  
		            if (state == JFileChooser.APPROVE_OPTION) {   
		            	File f = selectDirPlan.getSelectedFile();// f为选择到的目录  
		                imagePathText.setText(f.getAbsolutePath()); 
		            } else {   
		                return;  // 撤销则返回  
		            }  
		            //弹出对话框可以改变里面的参数具体得靠大家自己去看，时间很短  
//		            JOptionPane.showMessageDialog(null, "弹出对话框的实例，欢迎您-漆艾琳！", "提示", 2);  
				}
			});
			
			confirmButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if ("".equals(projectNameText.getText())) {
						infoLabel.setForeground(Color.RED);
						infoLabel.setText("请填写项目名字！");
						return;
					}
					if ("".equals(imagePathText.getText())) {
						infoLabel.setForeground(Color.RED);
						infoLabel.setText("请选择图片所在目录！");
						return;
					}
					if ("".equals(exportNameText.getText())) {
						infoLabel.setForeground(Color.RED);
						infoLabel.setText("请填写合图导出文件名！");
						return;
					}
					//判断选择的文件夹存在么
					String dirStr = imagePathText.getText();
					File dirFile = new File(dirStr);
					if (!dirFile.exists() || !dirFile.isDirectory()) {
						infoLabel.setForeground(Color.RED);
						infoLabel.setText("请选择正确的图片所在目录！");
						return;
					}
					
					//验证 0=可以创建，1=重复的项目名字，2=工作空间中已经存在和项目名相同的目录！
					int check = AppManager.checkProjectName(projectNameText.getText());
					if (check == 1) { 
						infoLabel.setForeground(Color.RED);
						infoLabel.setText("重复的项目名字！");
						return;
					} else if (check == 2) {
						infoLabel.setForeground(Color.RED);
						infoLabel.setText("工作空间中已经存在和项目名相同的目录！");
						return;
					} else if (check == 0) { 
						//创建项目 
						AppManager.createProject(projectNameText.getText(), imagePathText.getText(), exportNameText.getText());
						newProjectDialog.setVisible(false); 
					} 
				}
			});
			
		}
		infoLabel.setForeground(Color.BLUE); 
		infoLabel.setText("请填写以下信息！");
		projectNameText.setText(""); 
		imagePathText.setText(""); 
		exportNameText.setText(""); 
		newProjectDialog.setVisible(true); 
	}

}
