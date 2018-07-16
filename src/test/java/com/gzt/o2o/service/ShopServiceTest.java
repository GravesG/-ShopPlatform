package com.gzt.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzt.o2o.BaseTest;
import com.gzt.o2o.dto.ShopExecution;
import com.gzt.o2o.entity.Area;
import com.gzt.o2o.entity.PersonInfo;
import com.gzt.o2o.entity.Shop;
import com.gzt.o2o.entity.ShopCategory;
import com.gzt.o2o.enums.ShopStateEnum;

public class ShopServiceTest extends BaseTest {
	@Autowired
	private ShopService shopService;

	@Test
	public void testAddShop() throws FileNotFoundException {
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
		shop.setShopName("测试的店铺3");
		shop.setShopDesc("tese3");
		shop.setShopAddr("test3");
		shop.setPhone("test3");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File file = new File("D:\\DeskTopPhoto\\erha.jpg");
		InputStream is;

		is = new FileInputStream(file);
		ShopExecution addShop = shopService.addShop(shop, is, file.getName());

		assertEquals(ShopStateEnum.CHECK.getState(), addShop.getState());
	}
}
