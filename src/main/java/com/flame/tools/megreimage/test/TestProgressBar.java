package com.flame.tools.megreimage.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.test.TestProgressBar.java
 * @Description: 进度条测试类
 * @Create: DerekWu  2016年4月22日 上午9:54:53 
 * @version: V1.0 
 */
public class TestProgressBar implements ActionListener, ChangeListener
{
    JFrame f = null;
    JProgressBar progressbar;
    JLabel label;
    Timer timer;
    JButton b;
    public TestProgressBar()                           
    {
        f = new JFrame("progressbar Example");
        Container contentPane = f.getContentPane();
        label = new JLabel(" ",SwingConstants.CENTER);
        progressbar = new JProgressBar();//创建一个进度条
        progressbar.setOrientation(SwingConstants.HORIZONTAL);//设置其方向为水平方向
        progressbar.setMinimum(0);//最小刻度0
        progressbar.setMaximum(100);//最大刻度100
        progressbar.setValue(0);
        progressbar.setStringPainted(true);
        progressbar.addChangeListener(this);//添加进度条变化事件
        progressbar.setPreferredSize(new Dimension(200,30));
        JPanel panel = new JPanel();
        b = new JButton("Start");
        b.addActionListener(this);//为按钮添加动作事件
        panel.add(b);
        timer = new Timer(50,this);//创建一个事件组件对象
        contentPane.add(panel,BorderLayout.NORTH);
        contentPane.add(progressbar,BorderLayout.CENTER);
        contentPane.add(label,BorderLayout.SOUTH);
        f.pack();
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    public static void main(String[] args)
    {
        new TestProgressBar();
    }
    @Override
	public void actionPerformed(ActionEvent e)
    {
///当单击按钮，则计时开始
        if(e.getSource() == b)
        {
            timer.start();
        }
///当单击事件组件，则进度条开始变化
        if(e.getSource() == timer)
        {
            int value = progressbar.getValue();
            if( value < 100)
            {
                value++;//进度条往前运动
                progressbar.setValue(value);
            }
            else
            {
                timer.stop();
                progressbar.setValue(0);
            }
        }
    }
    @Override
	public void stateChanged(ChangeEvent e1)
    {
        int value = progressbar.getValue();
        ///当进度条运行时，就将其进度显示在标签中
        if(e1.getSource() == progressbar)
        {
            label.setText("目前已完成进度："+Integer.toString(value)+" %"); 
        }
    }
}

