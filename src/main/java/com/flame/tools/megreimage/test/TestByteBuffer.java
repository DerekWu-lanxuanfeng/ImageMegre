package com.flame.tools.megreimage.test;

import java.nio.ByteBuffer;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.test.TestByteBuffer.java
 * @Description: 测试缓冲区 
 * @Create: DerekWu  2016年5月4日 下午10:35:45 
 * @version: V1.0 
 */
public class TestByteBuffer {

	public static void main(String[] args) {
		ByteBuffer src = ByteBuffer.allocate(10);
		
		System.out.println(src.position());
		src.putShort((short)5000);
		System.out.println(src.position());
		src.putInt(123456789);
		System.out.println(src.position());
		src.putInt(987654321);
		src.position(0);
		System.out.println(src.position());
		System.out.println(src.getShort());
		System.out.println(src.getInt());
		System.out.println(src.getInt());
	}

}
