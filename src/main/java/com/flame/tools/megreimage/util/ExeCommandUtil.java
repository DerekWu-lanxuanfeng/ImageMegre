package com.flame.tools.megreimage.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.util.ExeCommandUtil.java
 * @Description: 执行命令工具 
 * @Create: DerekWu  2016年5月5日 下午5:47:55 
 * @version: V1.0 
 */
public class ExeCommandUtil { 

	/**
	 * 执行系统命令 
	 * @param commandStr
	 * @return 成功或者失败 
	 */
	public static boolean exeCommand(String commandStr) {
		long times = System.currentTimeMillis();
		System.out.println("start exe cmd:"+commandStr);
		Runtime run = Runtime.getRuntime();  
		try {
			Process p = run.exec(commandStr);// 启动另一个进程来执行命令   
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());  
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));  
            String lineStr;  
            while ((lineStr = inBr.readLine()) != null)   
                //获得命令执行后在控制台的输出信息  
                System.out.println(lineStr);// 打印输出信息  
            //检查命令是否执行失败。  
            if (p.waitFor() != 0) {   
                if (p.exitValue() == 1) {//p.exitValue()==0表示正常结束，1：非正常结束  
                    System.err.println("end exe cmd: execute failed!"); 
                    return false;
                }
            }   
            inBr.close();   
            in.close();   
		} catch (Exception e) { 
			e.printStackTrace();
		}
		System.out.println("end exe cmd: execute success, use times "+(System.currentTimeMillis()-times) + "ms");
		return true;
	}

}
