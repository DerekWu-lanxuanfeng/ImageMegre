package com.flame.tools.megreimage.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.test.MyTest.java
 * @Description: 测试类 
 * @Create: DerekWu  2016年4月19日 下午5:26:33 
 * @version: V1.0 
 */
public class MyTest {

	public static void main(String []args) {
//		testCmd(); 
//		DDALine(0,10,5,20);
//		Glib_Line(0,10,5,20);
		test1();
	}
	
	private static void testCmd() {
		String userDir = System.getProperty("user.dir");
		System.out.println(userDir);
		long times = System.currentTimeMillis();
		String cmd = "E:/JavaWorkspaces/MyEclipse2014/MegreImage/TextureMerger/TextureMerger.exe -p E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/test22222 -o E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/test22222/test258.json";
		Runtime run = Runtime.getRuntime(); 
		try {
			Process p = run.exec(cmd);// 启动另一个进程来执行命令  
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());  
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));  
            String lineStr;  
            while ((lineStr = inBr.readLine()) != null)   
                //获得命令执行后在控制台的输出信息  
                System.out.println(lineStr);// 打印输出信息  
            //检查命令是否执行失败。  
            if (p.waitFor() != 0) {   
                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束  
                    System.err.println("命令执行失败!");   
            }   
            inBr.close();   
            in.close();   
		} catch (Exception e) { 
			e.printStackTrace();
		}
		System.err.println("命令执行成功!使用时间："+(System.currentTimeMillis()-times)/1000 + "秒"); 
	}

	/**
	 * 
	 */
	private static void test1() {
		String userDir = System.getProperty("user.dir");
		System.out.println(userDir);
		System.out.println(userDir.substring(userDir.lastIndexOf(File.separator)+1));
		System.out.println("floatMax="+Float.MAX_VALUE);
		System.out.println("floatMin="+Float.MIN_VALUE);
		
		System.out.println("33:"+"33".matches("[0-9]+")); 
		System.out.println("1:"+"1".matches("[0-9]+")); 
		System.out.println("d:"+"d".matches("[0-9]+")); 
		System.out.println("333333:"+"333333".matches("[0-9]+")); 
		System.out.println("3yyuu:"+"3yyuu".matches("[0-9]+")); 
		System.out.println("y56:"+"3yyuu".matches("[0-9]+")); 
		
		System.out.println("Short.MAX_VALUE="+Short.MAX_VALUE); 
		
		String strTest1 = "sdf.png";
		String [] strArray = strTest1.split("\\.");
		
		System.out.println("strLength = " + strArray.length);
		
		String str = "1one123two456obc";
		String[] strs2 = str.split("o");
		for (int i = 0; i < strs2.length; i++)
			System.out.println("strs2[" + i + "] = " + strs2[i]);

		String value = "192.168.128.33";
		String[] names = value.split("\\.");
		for (int i = 0; i < names.length; i++)
			System.out.println(names[i]);

		String value2 = "ABCD|568|ER5|54P";
		String[] names2 = value2.split("\\|");
		for (int i = 0; i < names2.length; i++)
			System.out.println(names2[i]);
	}
	
	private static void DDALine(int x0, int y0, int x1, int y1) {
		float x, y, length, dx, dy;
		int i;
		if (Math.abs(x1 - x0) >= Math.abs(y1 - y0))
			length = Math.abs(x1 - x0);
		else
			length = Math.abs(y1 - y0);
		dx = (x1 - x0) / length;
		dy = (y1 - y0) / length;
		x = x0 + 0.5f;
		y = y0 + 0.5f;
		i = 1;
		while (i <= (int) length) {
			System.out.println("x=" + (int) x + ",y=" + (int) y);
			x = x + dx;
			y = y + dy;
			i++;
		}
	}
	
	
//	private static void Bresenhan_line(int x0,int y0,int x1,int y1)
//	{
//	    int x,y,dx,dy,e;
//	    dx = Math.abs(x1-x0);
//	    dy = Math.abs(y1-y0); 
//	    e = -dx;
//	    x = x0;
//	    y = y0;
//	    for(int i=0;i<=dx;i++)
//	    {
//	       //画点
//	    	System.out.println("Bresenhan_line  x="+x+" y="+y);
//	       ++x;
//	       e += 2*dy;
//	       if(e>=0)
//	       {
//	          y++;
//	          e -=2*dx;
//	       } 
//	    }
//	}


	// 交换整数 a 、b 的值
//	private static void swap_int(int a, int b) { 
//	    a ^= b;
//	    b ^= a;
//	    a ^= b;
//	} 
// 
//	// Bresenham's line algorithm
//	private static void draw_line(int x1, int y1, int x2, int y2) {
//	    // 参数 c 为颜色值
//	    int dx = Math.abs(x2 - x1),
//	        dy = Math.abs(y2 - y1),
//	        yy = 0;
//	 
//	    if (dx < dy) {
//	        yy = 1;
//	        swap_int(x1, y1);
//	        swap_int(x2, y2);
//	        swap_int(dx, dy);
//	    }
//	 
//	    int ix = (x2 - x1) > 0 ? 1 : -1,
//	         iy = (y2 - y1) > 0 ? 1 : -1,
//	         cx = x1,
//	         cy = y1,
//	         n2dy = dy * 2,
//	         n2dydx = (dy - dx) * 2,
//	         d = dy * 2 - dx;
//	 
//	    if (yy==1) { // 如果直线与 x 轴的夹角大于 45 度
//	        while (cx != x2) {
//	            if (d < 0) {
//	                d += n2dy;
//	            } else {
//	                cy += iy;
//	                d += n2dydx;
//	            }
//	            //putpixel(img, cy, cx, c);
//	            System.out.println(" x=" + cy + "  y=" + cx);
//	            cx += ix;
//	        }
//	    } else { // 如果直线与 x 轴的夹角小于 45 度
//	        while (cx != x2) {
//	            if (d < 0) {
//	                d += n2dy;
//	            } else {
//	                cy += iy;
//	                d += n2dydx;
//	            }
//	            //putpixel(img, cx, cy, c);
//	            System.out.println(" x=" + cx + "  y=" + cy);
//	            cx += ix;
//	        }
//	    }
//	}

	/**
	 * 布兰森汉姆(Bresenham)算法画线 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	private static void Glib_Line(int x1, int y1, int x2, int y2) {
		int dx, dy, e;
		dx = x2 - x1;
		dy = y2 - y1;

		if (dx >= 0) {
			if (dy >= 0) // dy>=0
			{
				if (dx >= dy) // 1/8 octant
				{
					e = dy - dx / 2;
					while (x1 <= x2) {
						// PutPixel(x1,y1,color);
						System.out.println(" x=" + x1 + "  y=" + y1);
						if (e > 0) {
							y1 += 1;
							e -= dx;
						}
						x1 += 1;
						e += dy;
					}
				} else // 2/8 octant
				{
					e = dx - dy / 2;
					while (y1 <= y2) {
						// PutPixel(x1,y1,color);
						System.out.println(" x=" + x1 + "  y=" + y1);
						if (e > 0) {
							x1 += 1;
							e -= dy;
						}
						y1 += 1;
						e += dx;
					}
				}
			} else // dy<0
			{
				dy = -dy; // dy=abs(dy)

				if (dx >= dy) // 8/8 octant
				{
					e = dy - dx / 2;
					while (x1 <= x2) {
						// PutPixel(x1,y1,color);
						System.out.println(" x=" + x1 + "  y=" + y1);
						if (e > 0) {
							y1 -= 1;
							e -= dx;
						}
						x1 += 1;
						e += dy;
					}
				} else // 7/8 octant
				{
					e = dx - dy / 2;
					while (y1 >= y2) {
						// PutPixel(x1,y1,color);
						System.out.println(" x=" + x1 + "  y=" + y1);
						if (e > 0) {
							x1 += 1;
							e -= dy;
						}
						y1 -= 1;
						e += dx;
					}
				}
			}
		} else // dx<0
		{
			dx = -dx; // dx=abs(dx)
			if (dy >= 0) // dy>=0
			{
				if (dx >= dy) // 4/8 octant
				{
					e = dy - dx / 2;
					while (x1 >= x2) {
						// PutPixel(x1,y1,color);
						System.out.println(" x=" + x1 + "  y=" + y1);
						if (e > 0) {
							y1 += 1;
							e -= dx;
						}
						x1 -= 1;
						e += dy;
					}
				} else // 3/8 octant
				{
					e = dx - dy / 2;
					while (y1 <= y2) {
						// PutPixel(x1,y1,color);
						System.out.println(" x=" + x1 + "  y=" + y1);
						if (e > 0) {
							x1 -= 1;
							e -= dy;
						}
						y1 += 1;
						e += dx;
					}
				}
			} else // dy<0
			{
				dy = -dy; // dy=abs(dy)

				if (dx >= dy) // 5/8 octant
				{
					e = dy - dx / 2;
					while (x1 >= x2) {
						// PutPixel(x1,y1,color);
						System.out.println(" x=" + x1 + "  y=" + y1);
						if (e > 0) {
							y1 -= 1;
							e -= dx;
						}
						x1 -= 1;
						e += dy;
					}
				} else // 6/8 octant
				{
					e = dx - dy / 2;
					while (y1 >= y2) {
						// PutPixel(x1,y1,color);
						System.out.println(" x=" + x1 + "  y=" + y1);
						if (e > 0) {
							x1 -= 1;
							e -= dy;
						}
						y1 -= 1;
						e += dx;
					}
				}
			}
		}
	}
}
