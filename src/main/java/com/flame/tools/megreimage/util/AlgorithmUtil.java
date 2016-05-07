package com.flame.tools.megreimage.util;

import java.util.ArrayList;
import java.util.List;

import com.flame.tools.megreimage.vo.MyPointVO;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.util.AlgorithmUtil.java
 * @Description: 算法工具类
 * @Create: DerekWu  2016年4月24日 上午9:05:52 
 * @version: V1.0 
 */
public class AlgorithmUtil {

	/**
	 * 获得一条直线上的所有点 ，布兰森汉姆(Bresenham)画直线 算法
	 * @param x1 起始x坐标
	 * @param y1 起始y坐标
	 * @param x2 结束x坐标
	 * @param y2 结束y坐标
	 */
	public static List<MyPointVO> getInLinePoints(int x1, int y1, int x2, int y2) {
		List<MyPointVO> pointArray = new ArrayList<MyPointVO>();
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
						//System.out.println("1/8 octant");
						//System.out.println(" x=" + x1 + "  y=" + y1);
						pointArray.add(new MyPointVO(x1, y1));
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
						//System.out.println("2/8 octant");
						//System.out.println(" x=" + x1 + "  y=" + y1);
						pointArray.add(new MyPointVO(x1, y1));
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
						//System.out.println("8/8 octant");
						//System.out.println(" x=" + x1 + "  y=" + y1);
						pointArray.add(new MyPointVO(x1, y1));
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
						//System.out.println("7/8 octant");
						//System.out.println(" x=" + x1 + "  y=" + y1);
						pointArray.add(new MyPointVO(x1, y1));
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
						//System.out.println("4/8 octant");
						//System.out.println(" x=" + x1 + "  y=" + y1);
						pointArray.add(new MyPointVO(x1, y1));
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
						//System.out.println("3/8 octant");
						//System.out.println(" x=" + x1 + "  y=" + y1);
						pointArray.add(new MyPointVO(x1, y1));
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
						//System.out.println("5/8 octant");
						//System.out.println(" x=" + x1 + "  y=" + y1);
						pointArray.add(new MyPointVO(x1, y1));
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
						//System.out.println("6/8 octant");
						//System.out.println(" x=" + x1 + "  y=" + y1);
						pointArray.add(new MyPointVO(x1, y1));
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
		return pointArray;
	}
	
	/**
	 * 2D平面坐标中获取一个点绕另一个点旋转多少角度后的点 (顺时针) 
	 * @param rotateX 需要旋转的x坐标
	 * @param rotateY 需要旋转的y坐标
	 * @param baseX 围绕旋转的原点的x坐标
	 * @param baseY 围绕旋转的原点的y坐标
	 * @param rotareAngel 旋转角度
	 * @return
	 */
	public static MyPointVO getRotatePoint(int rotateX, int rotateY, int baseX, int baseY, int rotareAngel) {
		/**
		可以用极坐标来理解圆方程极坐标为：x=r*cosθ；y=r*sinθ（圆心为原点）
		点（x1,y1)到（x2,y2)距离为r；则以（x2,y2)为圆心r为半径做圆,可知旋转θ角度后的x,y都在圆上
		点（x1,y1)对应圆方程为：
		x1-x2=r*cosθ1 ; y1-y2=r*sinθ1 (注意这里圆心为（x2,y2)）
		点（x,y）对应圆方程为：
		x-x2=r*cos(θ1+ θ) = r*cosθ1*cosθ-r*sinθ1*sinθ=(x1-x2)cosθ-(y1-y2)sinθ
		y-y2=r*sin(θ2 +θ) = r*sinθ1*cosθ+r*cosθ1*sinθ=(y1-y2)cosθ+(x1-x2)sinθ
		所以：
		x=(x1-x2)cosθ-(y1-y2)sinθ+x2
		y=(y1-y2)cosθ+(x1-x2)sinθ+y2
		*/
		double k = Math.toRadians(rotareAngel); 
		double x2 = (rotateX-baseX)*Math.cos(k) - (rotateY-baseY)*Math.sin(k) + baseX; 
		double y2 = (rotateY-baseY)*Math.cos(k) + (rotateX-baseX)*Math.sin(k) + baseY;  
		return new MyPointVO((int)Math.round(x2), (int)Math.round(y2)); 
	}
	
}
