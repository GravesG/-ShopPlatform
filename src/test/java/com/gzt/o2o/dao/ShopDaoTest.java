package com.gzt.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzt.o2o.BaseTest;
import com.gzt.o2o.entity.Area;
import com.gzt.o2o.entity.PersonInfo;
import com.gzt.o2o.entity.Shop;
import com.gzt.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest{
	@Autowired
	private ShopDao shopDao;
	
	@Test
	public void testInertShop() {
		Shop shop = new Shop();
		Area area = new Area();
		PersonInfo personInfo = new PersonInfo();
		ShopCategory shopCategory = new ShopCategory();
		personInfo.setUserId(1L);
		area.setAreaId(3);
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(personInfo);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺");
		shop.setShopDesc("tese");
		shop.setShopAddr("test");
		shop.setPhone("test");
		shop.setShopImg("test");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");
		int row = shopDao.insertShop(shop);
		assertEquals(1, row);
	}
	
	@Test
	public void testUpdateShop() {
		Shop shop = new Shop();
		shop.setShopId(1L);
		shop.setShopDesc("测试描述");
		shop.setShopAddr("测试地址");
		int row = shopDao.updateShop(shop);
		assertEquals(1, row);
	}
	
	@Test
	public void testQueryByShopId() {
		long shopId = 1;
		
		Shop shop = shopDao.queryByShopId(shopId);
		System.out.println(shop.getArea());
		System.out.println(shop.getArea().getAreaName());
	}
}
