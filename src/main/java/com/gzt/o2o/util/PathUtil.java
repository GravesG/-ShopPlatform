package com.gzt.o2o.util;

public class PathUtil {
	private static String separator = System.getProperty("file.separator");
	
	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if(os.toLowerCase().subSequence(0, 3).equals("win")) {
			basePath="D:/DeskTopPhoto";
		}else {
			basePath="/home/xiangze/image/";
		}
		basePath = basePath.replace("/", separator);
		return basePath;
	}
	
	public static String getShopImagePath(Long shopId) {
		String imagePath = "/upload/item/shop/"+shopId+"/";
		return imagePath.replace("/", separator);
	}
	public static void main(String[] args) {
		getImgBasePath();
	}
}
