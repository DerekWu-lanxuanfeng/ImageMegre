package com.flame.tools.megreimage.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JInternalFrameTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6365372541463468192L;
	JDesktopPane desktopPane = null; // 桌面面板
	InternalFrame plnFrame = null; // 人事管理
	InternalFrame rlnFrame = null; // 帐物管理
	InternalFrame tlnFrame = null; // 待遇管理

	public JInternalFrameTest() {
		super();
		try {
			setTitle("人事管理");
			setBounds(100, 100, 350, 150);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// 创建桌面面板
			desktopPane = new JDesktopPane();
			desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE); // 设置内部窗体拖动模式
			getContentPane().add(desktopPane, BorderLayout.CENTER);
			// 桌面添加背景图片
			final JLabel backLabel = new JLabel();
			// URL resource = this.getClass().getResource("back.jpg"); //图片
			BufferedImage resource = ImageIO.read(new File("e://test//dd.png"));
			ImageIcon icon = new ImageIcon(resource); // 设置图片
			backLabel.setIcon(icon);
			backLabel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight()); // 设置边界
			desktopPane.add(backLabel, new Integer(Integer.MIN_VALUE)); // 添加到指定索引位置

			// 添加button
			final JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.NORTH);
			JButton jButton_p = new JButton("人事");
			jButton_p.addActionListener(new BAlistener(null, "人事")); // 监听事件
			JButton jButton_r = new JButton("帐物");
			jButton_r.addActionListener(new BAlistener(null, "帐物")); // 监听事件
			JButton jButton_t = new JButton("待遇");
			jButton_t.addActionListener(new BAlistener(null, "待遇")); // 监听事件
			panel.add(jButton_p);
			panel.add(jButton_r);
			panel.add(jButton_t);
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}

	private class BAlistener implements ActionListener {
		InternalFrame inFrame;
		String title;

		public BAlistener(InternalFrame inFrame, String title) {
			this.inFrame = inFrame;
			this.title = title;
		}

		public void actionPerformed(ActionEvent e) {
			if (inFrame == null || inFrame.isClosed()) {
				JInternalFrame[] allFrames = desktopPane.getAllFrames();
				int titleBarHight = 30 * allFrames.length;
				int x = 10 + titleBarHight, y = x;
				int width = 250, height = 180;
				inFrame = new InternalFrame(title);
				inFrame.setBounds(x, y, width, height);// 设置位置与大小
				inFrame.setVisible(true); // 可见
				desktopPane.add(inFrame); // 添加到桌面面板
			}
			try {
				inFrame.setSelected(true);
			} catch (PropertyVetoException propertyVetoE) {
				propertyVetoE.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JInternalFrameTest jInternalFrameTest = new JInternalFrameTest();
		jInternalFrameTest.setVisible(true);
	}

}

// 自定义一个InternalFrame
class InternalFrame extends JInternalFrame {
	public InternalFrame(String title) {
		super();
		setTitle(title); // 设置内部窗体标题
		setResizable(true); // 允许自由调整大小
		setClosable(true); // 提供关闭按钮
		setIconifiable(true); // 设置提供图标化按钮
		setMaximizable(true); // 设置提供最大化按钮
		URL resource = this.getClass().getResource("caption.png");
		ImageIcon icon = new ImageIcon(resource);
		setFrameIcon(icon);
	}
}
