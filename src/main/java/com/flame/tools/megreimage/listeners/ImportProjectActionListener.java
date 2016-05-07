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
 * @File: com.flame.tools.megreimage.listeners.ImportProjectActionListener.java
 * @Description: 导入项目
 * @Create: DerekWu  2016年4月19日 下午9:19:17 
 * @version: V1.0 
 */
public class ImportProjectActionListener implements ActionListener {

	/** 导入项目面板*/
	private JDialog importProjectDialog;
	
	/** 信息文本 */
	private JLabel infoLabel;
	
	/** 信息文本 */
	private JLabel infoLabe2;
	
	/** 消息文本 */
	private JLabel msgLabel;
	
	/** 项目目录text */
	private JTextField projectDirText;
	
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
		if (importProjectDialog == null) { 
			importProjectDialog = new JDialog(AppMediator.getAppJFrame().getjFrame());  
			importProjectDialog.setTitle("新建合图项目"); 
			importProjectDialog.setSize(NEW_WIDTH, NEW_HEIGHT);  
			importProjectDialog.setLocationRelativeTo(AppMediator.getAppJFrame().getjFrame());
			importProjectDialog.setModal(true); 
			importProjectDialog.setLayout(null); 
			
			//信息文本
			infoLabel = new JLabel("  ", SwingConstants.CENTER);
			infoLabe2 = new JLabel("  ", SwingConstants.CENTER);
			
			//消息文本
			msgLabel = new JLabel("  ", SwingConstants.CENTER);

			
			//选择项目路径
			JLabel projectPath = new JLabel(); 
			projectPath.setText("选择项目所在目录： ");  
			//项目路径填写框
			projectDirText = new JTextField(); 
			//选择按钮
			JButton selectPathButton = new JButton("选择");
			
			//确定导入
			confirmButton = new JButton("确定导入 ");
			
			//设置位置
			int labelWidth = 120, labelHeight = 30, textWidth = 240, textHeight = 30;
			
			infoLabel.setBounds(30, 10, 440, labelHeight); 
			infoLabe2.setBounds(30, 50, 440, labelHeight); 
			msgLabel.setBounds(30, 90, 440, labelHeight); 
			projectPath.setBounds(30, 140, labelWidth, labelHeight);  
			projectDirText.setBounds(160, 140, textWidth, textHeight); 
			selectPathButton.setBounds(410, 140, 60, 26); 
			confirmButton.setBounds(200, 210, 100, 26); 
			
			importProjectDialog.add(infoLabel); 
			importProjectDialog.add(infoLabe2); 
			importProjectDialog.add(msgLabel); 
			importProjectDialog.add(projectPath); 
			importProjectDialog.add(projectDirText); 
			importProjectDialog.add(selectPathButton); 
			importProjectDialog.add(confirmButton); 
			
			//目录选择
			selectDirPlan = new JFileChooser(AppManager.WORK_SPACES_DIR);  
			//jfc.setCurrentDirectory(new File("d://"));// 文件选择器的初始目录定为d盘  
			selectPathButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectDirPlan.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 设定只能选择到文件夹   
//					selectDirPlan.setFileSelectionMode(JFileChooser.FILES_ONLY);// 设定只能选择到文件   
		            int state = selectDirPlan.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句  
		            if (state == JFileChooser.APPROVE_OPTION) {   
		            	File f = selectDirPlan.getSelectedFile();// f为选择到的目录  
		            	projectDirText.setText(f.getAbsolutePath());  
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
					if ("".equals(projectDirText.getText())) {
						msgLabel.setForeground(Color.RED);
						msgLabel.setText("请选择项目所在目录！");
						return;
					}
					
					/**
				     * 导入项目，返回：
				     * 0=成功  
				     * 1=该项目已经在工作空间，无需再次导入 
				     * 2=您导入的项目所在目录不存在工作空间之内 
				     * 3=项目配置文件不存在 
				     * 4=项目配置的图片所在目录不存在 
				     */
					String dirStr = projectDirText.getText();
					int check = AppManager.importProject(dirStr.substring(dirStr.lastIndexOf(File.separator)+1));
				
					if (check == 1) { 
						msgLabel.setForeground(Color.RED);
						msgLabel.setText("该项目已经在工作空间，无需再次导入 ！");
						return;
					} else if (check == 2) {
						msgLabel.setForeground(Color.RED);
						msgLabel.setText("您导入的项目所在目录不存在工作空间之内 ！");
						return;
					} else if (check == 3) {
						msgLabel.setForeground(Color.RED);
						msgLabel.setText("项目配置文件不存在  ！");
						return;
					} else if (check == 4) {
						msgLabel.setForeground(Color.RED);
						msgLabel.setText("项目配置的图片所在目录不存在  ！"); 
						return;
					} else if (check == 0) { 
						//创建项目 
						importProjectDialog.setVisible(false); 
					} 
				}
			});
			
		}
		infoLabel.setForeground(Color.BLUE); 
		infoLabel.setText("导入项目必须是在工作目录中：");
		infoLabe2.setForeground(Color.BLUE); 
		infoLabe2.setText(AppManager.WORK_SPACES_DIR);
		msgLabel.setForeground(Color.BLUE); 
		msgLabel.setText("请选择需要导入项目所在的目录！"); 
		projectDirText.setText(""); 
		importProjectDialog.setVisible(true); 
	}

}
