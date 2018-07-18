package com.gzt.o2o.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();

	public static String generateThumbnail(InputStream thumnnaliInputStream,String fileName, String targetAddr) {
		//File file = new File("D:\\CloudMusic\\MV\\ljj.mp4");
		String relativeAddr = "";
		try {
		/*FileInputStream input = new FileInputStream(thumnnaliInputStream);
		MultipartFile multipartFile = new MockMultipartFile("file", thumnnaliInputStream.getName(), "text/plain", input);*/
		String realFileName = getRandomFileName();// 获取文件随机名
		String extension = getFileExtension(fileName);// 获取后缀名
		makeDirPath(targetAddr);
		relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		
			Thumbnails.of(thumnnaliInputStream).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath.substring(1) + "xiong.jpg")), 0.25f)
					.outputQuality(0.8f).toFile(dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relativeAddr;
	}

	/**
	 * 创建目标路径所涉及的目录
	 * 
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	/**
	 * 获取文件拓展名
	 * 
	 * @param cFile
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 获取文件名当前时间加随机5位数
	 * 
	 * @return
	 */
	public static String getRandomFileName() {
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sdf.format(new Date());
		return nowTimeStr + rannum;
	}
	
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
		//判断是否存在
		if(fileOrPath.exists()) {
			//判断是否是文件夹
			if(fileOrPath.isDirectory()) {
				//循环删除所有文件
				File[] file = fileOrPath.listFiles();
				for(int i=0;i<file.length;i++) {
					file[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}

	public static void main(String[] args) throws IOException {
		/*Thumbnails.of(new File("D:\\DeskTopPhoto\\erha.jpg")).size(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("D:\\DeskTopPhoto\\xiong.jpg")), 0.25f)
				.outputQuality(0.8f).toFile("D:\\DeskTopPhoto\\erhanew.jpg");*/
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		path= path.substring(1);
		System.out.println(path);
	}
}
