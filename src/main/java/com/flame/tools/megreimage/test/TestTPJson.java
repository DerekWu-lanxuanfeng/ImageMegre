package com.flame.tools.megreimage.test;

import java.util.Map.Entry;

import com.flame.tools.megreimage.util.FileUtil;
import com.flame.tools.megreimage.util.GsonUtil;
import com.flame.tools.megreimage.vo.TPJsonObjVO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.test.TestTPJson.java
 * @Description: 测试tp配置文件json格式
 * "31.png":
	{
		"frame": {"x":273,"y":182,"w":95,"h":251},
		"rotated": true,
		"trimmed": false,
		"spriteSourceSize": {"x":0,"y":0,"w":95,"h":251},
		"sourceSize": {"w":95,"h":251},
		"pivot": {"x":0.5,"y":0.5}
	}
 * @Create: DerekWu  2016年5月6日 上午10:15:36 
 * @version: V1.0 
 */
public class TestTPJson {

	public static void main(String[] args) {
		try { 
			//读取配置文件 
			String publishJsonConfigPath = "E:/JavaWorkspaces/MyEclipse2014/MegreImage/workspaces/testHstd/publish/testHstd.json";
			String jsonConfigStr = FileUtil.getFileText(publishJsonConfigPath); 
			//解析请求参数 
			JsonObject paramJsonObj = GsonUtil.jsonParser.parse(jsonConfigStr).getAsJsonObject();
			JsonObject framesObj = paramJsonObj.getAsJsonObject("frames");
			for (Entry<String, JsonElement> oneEntry:framesObj.entrySet()) {
				String imageStrName = oneEntry.getKey();
				JsonObject imageInfo = oneEntry.getValue().getAsJsonObject();
				JsonObject imageFrame = imageInfo.getAsJsonObject("frame");
				JsonObject imageSize =  imageInfo.getAsJsonObject("sourceSize");
				
				short imageName = Short.parseShort(imageStrName.substring(0, imageStrName.indexOf("."))); 
				int x = imageFrame.get("x").getAsInt();
				int y = imageFrame.get("y").getAsInt();
				int width = imageSize.get("w").getAsInt();
				int height = imageFrame.get("h").getAsInt();
				boolean rotated = imageInfo.get("rotated").getAsBoolean();
				
				TPJsonObjVO oneTPJsonObjVO = new TPJsonObjVO(imageName, x, y, width, height, rotated);
				System.out.println(oneTPJsonObjVO);  
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

}
