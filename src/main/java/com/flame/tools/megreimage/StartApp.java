package com.flame.tools.megreimage;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MagreImage 
 * @File: com.flame.tools.megreimage.StartApp.java
 * @Description: 应用启动类
 * @Create: DerekWu  2016年4月18日 下午9:34:59 
 * @version: V1.0 
 */
public class StartApp {

	public static void main(String[] args) {  
		AppMediator appMediator = new AppMediator();   
		AppManager.initApp();   
		appMediator.initData(); 
	}
}
