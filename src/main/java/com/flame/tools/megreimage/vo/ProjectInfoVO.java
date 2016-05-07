package com.flame.tools.megreimage.vo;

import com.flame.tools.megreimage.consts.ProjectStatusEnum;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: MegreImage 
 * @File: com.flame.tools.megreimage.vo.ProjectInfoVO.java
 * @Description: 项目信息vo 
 * @Create: DerekWu  2016年4月19日 下午4:32:09 
 * @version: V1.0 
 */
public class ProjectInfoVO {

	/** 项目名字  */
	private String name;
	
	/** 项目状态  */
	private ProjectStatusEnum status;
	
	/** 使用目录 */
	private String useDirectory;
	
	/** 合图文件名  */
	private String fileName;
	
	/** 合图宽度 */
	private int megreImageWidth;
	
	/** 合图高度  */
	private int megreImageHeight;
	
	/** 图片总数量  */
	private int imageNum;
	
	/** 已处理图片数量  */
	private int exedNum;
	
	/** 未处理图片数量  */
	private int noExedNum;
	
	/** 已处理图片4边形数量  */
	private int fourPolygonNum;
	
	/** 已处理图片5边形数量  */
	private int fivePolygonNum;
	
	/** 已处理图片6边形数量  */
	private int sixPolygonNum;
	
	/** 已处理图片7边形数量  */
	private int sevenPolygonNum;
	
	/** 已处理图片8边形数量  */
	private int eightPolygonNum;
	
	/** 多边形运算 图片宽高限制  */
	private int polygonImageSizeLimit;
	
	/** 多边形运算   裁切面积限制*/
	private int polygonCutAreaLimit; 
	
	/** 多边形运算   裁切边长限制  */
	private int polygonCutBorderLengthLimit;
	
	/** webp-q */
	private int webpQuality;
	
	/** webp-alpha-q */
	private int webpAlphaQuality;
	
	/** pngquant-q-min */ 
	private int pngquantQualityMin;
	
	/** pngquant-q-max */ 
	private int pngquantQualityMax;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProjectStatusEnum getStatus() {
		return status;
	}

	public void setStatus(ProjectStatusEnum status) {
		this.status = status;
	}

	public String getUseDirectory() {
		return useDirectory;
	}

	public void setUseDirectory(String useDirectory) {
		this.useDirectory = useDirectory;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getMegreImageWidth() {
		return megreImageWidth;
	}

	public void setMegreImageWidth(int megreImageWidth) {
		this.megreImageWidth = megreImageWidth;
	}

	public int getMegreImageHeight() {
		return megreImageHeight;
	}

	public void setMegreImageHeight(int megreImageHeight) {
		this.megreImageHeight = megreImageHeight;
	}

	public int getImageNum() {
		return imageNum;
	}

	public void setImageNum(int imageNum) {
		this.imageNum = imageNum;
	}

	public int getExedNum() {
		return exedNum;
	}

	public void setExedNum(int exedNum) {
		this.exedNum = exedNum;
	}

	public int getNoExedNum() {
		return noExedNum;
	}

	public void setNoExedNum(int noExedNum) {
		this.noExedNum = noExedNum;
	}

	public int getFourPolygonNum() {
		return fourPolygonNum;
	}

	public void setFourPolygonNum(int fourPolygonNum) {
		this.fourPolygonNum = fourPolygonNum;
	}

	public int getFivePolygonNum() {
		return fivePolygonNum;
	}

	public void setFivePolygonNum(int fivePolygonNum) {
		this.fivePolygonNum = fivePolygonNum;
	}

	public int getSixPolygonNum() {
		return sixPolygonNum;
	}

	public void setSixPolygonNum(int sixPolygonNum) {
		this.sixPolygonNum = sixPolygonNum;
	}

	public int getSevenPolygonNum() {
		return sevenPolygonNum;
	}

	public void setSevenPolygonNum(int sevenPolygonNum) {
		this.sevenPolygonNum = sevenPolygonNum;
	}

	public int getEightPolygonNum() {
		return eightPolygonNum;
	}

	public void setEightPolygonNum(int eightPolygonNum) {
		this.eightPolygonNum = eightPolygonNum;
	}

	public int getPolygonImageSizeLimit() {
		return polygonImageSizeLimit;
	}

	public void setPolygonImageSizeLimit(int polygonImageSizeLimit) {
		this.polygonImageSizeLimit = polygonImageSizeLimit;
	}

	public int getPolygonCutAreaLimit() {
		return polygonCutAreaLimit;
	}

	public void setPolygonCutAreaLimit(int polygonCutAreaLimit) {
		this.polygonCutAreaLimit = polygonCutAreaLimit;
	}

	public int getPolygonCutBorderLengthLimit() {
		return polygonCutBorderLengthLimit;
	}

	public void setPolygonCutBorderLengthLimit(int polygonCutBorderLengthLimit) {
		this.polygonCutBorderLengthLimit = polygonCutBorderLengthLimit;
	}

	public int getWebpQuality() {
		return webpQuality;
	}

	public void setWebpQuality(int webpQuality) {
		this.webpQuality = webpQuality;
	}

	public int getWebpAlphaQuality() {
		return webpAlphaQuality;
	}

	public void setWebpAlphaQuality(int webpAlphaQuality) {
		this.webpAlphaQuality = webpAlphaQuality;
	}

	public int getPngquantQualityMin() {
		return pngquantQualityMin;
	}

	public void setPngquantQualityMin(int pngquantQualityMin) {
		this.pngquantQualityMin = pngquantQualityMin;
	}

	public int getPngquantQualityMax() {
		return pngquantQualityMax;
	}

	public void setPngquantQualityMax(int pngquantQualityMax) {
		this.pngquantQualityMax = pngquantQualityMax;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(); 
		if (ProjectStatusEnum.POLYGON_EXE.equals(this.status)) {
			builder.append("O--");
		} else {
			builder.append("X--");
		}
		builder.append(this.name); 
		if (this.megreImageWidth > 0) { 
			builder.append("--("); 
			builder.append(this.megreImageWidth); 
			builder.append("*"); 
			builder.append(this.megreImageHeight);   
			builder.append(")"); 
		}
		return builder.toString();
	}
	
}
