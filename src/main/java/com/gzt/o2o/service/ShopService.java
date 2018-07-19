package com.gzt.o2o.service;

import java.io.InputStream;

import com.gzt.o2o.dto.ShopExecution;
import com.gzt.o2o.entity.Shop;

public interface ShopService {
	/**
	 * 添加商铺
	 * @param shop
	 * @param shopImg
	 * @param fileName
	 * @return
	 */
	ShopExecution addShop(Shop shop,InputStream shopImg,String fileName);
	
	/**
	 * 通过商铺id获取商铺信息
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);
	
	/**
	 * 修改商铺信息
	 * @param shop
	 * @return
	 */
	ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream,String fileName);
	
	/**
	 * 更具shopCondition分页返回相应列表数据
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
}
