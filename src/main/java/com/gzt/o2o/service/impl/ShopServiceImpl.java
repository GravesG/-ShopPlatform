package com.gzt.o2o.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzt.o2o.dao.ShopDao;
import com.gzt.o2o.dto.ShopExecution;
import com.gzt.o2o.entity.Shop;
import com.gzt.o2o.enums.ShopStateEnum;
import com.gzt.o2o.service.ShopService;
import com.gzt.o2o.util.ImageUtil;
import com.gzt.o2o.util.PageCalculator;
import com.gzt.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService{
	@Autowired
	private ShopDao shopDao;

	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) {
		if(shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		}
		try {
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			int effectedNum = shopDao.insertShop(shop);
			if(effectedNum < 0) {
				throw new RuntimeException("店铺创建失败");
			}else {
				if(shopImgInputStream != null) {
					try {
						addShopImg(shop,shopImgInputStream,fileName);
					} catch (Exception e) {
						throw new RuntimeException("addShopImg error:"+e.getMessage());
					}
					//更新店铺图片地址
					effectedNum = shopDao.updateShop(shop);
					if(effectedNum<0) {
						throw new RuntimeException("更新图片地址失败");
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("addShop error:"+e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}

	
	private void addShopImg(Shop shop, InputStream shopImgInputStream,String fileName) {
		//获取图片路径的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream,fileName, dest);
		shop.setShopImg(shopImgAddr);
	}


	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}


	@Override
	public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) {
		if(shop ==null || shop.getShopId()==null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		}else {
			try {
				//1.判断是否需要处理图片
				if(shopImgInputStream!=null && fileName!=null && !"".equals(fileName) ) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if(tempShop.getShopImg()!=null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					addShopImg(shop, shopImgInputStream, fileName);
				}
				//2.更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if(effectedNum<=0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				}else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS,shop);
				}
			} catch (Exception e) {
				throw new RuntimeException("modifyShop error:" + e.getMessage());
			}
		}
	}


	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution sc = new ShopExecution();
		if(shopList != null) {
			sc.setShopList(shopList);
			sc.setCount(count);
		}else {
			sc.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return sc;
	}


}
