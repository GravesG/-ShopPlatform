package com.gzt.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzt.o2o.BaseTest;
import com.gzt.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Test
	public void testQueryShopCategory() {
		/*List<ShopCategory> list = shopCategoryDao.queryShopCategory(new ShopCategory());
		assertEquals(1, list.size());*/
		ShopCategory ShopCategory = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		parentCategory.setShopCategoryId(1L);
		ShopCategory.setParent(parentCategory);
		List<ShopCategory>  list = shopCategoryDao.queryShopCategory(ShopCategory);
		assertEquals(1, list.size());
		System.out.println(list.get(0).getShopCategoryName());
	}
}
