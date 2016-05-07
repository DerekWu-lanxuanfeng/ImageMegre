package com.flame.tools.megreimage.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.test.TestImagePanel.java
 * @Description: 测试图片显示面板
 * @Create: DerekWu  2016年4月22日 下午8:25:00 
 * @version: V1.0 
 */
public class TestImagePanel {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Test Image Panel");
        frame.setBackground(Color.GREEN);  
        JLabel lbl = new JLabel();
        BufferedImage image = ImageIO.read(new File("e://test//dd.png"));
        lbl.setBorder(BorderFactory.createEtchedBorder());
        lbl.setBounds(0, 0, image.getWidth(), image.getHeight()); 
        System.out.println("image is:"+image); 
        lbl.setIcon(new ImageIcon(image));
        frame.add(lbl);
        frame.setPreferredSize(new Dimension(500, 500)); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}