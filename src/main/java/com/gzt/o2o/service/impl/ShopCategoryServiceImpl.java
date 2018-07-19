package com.gzt.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzt.o2o.dao.ShopCategoryDao;
import com.gzt.o2o.entity.ShopCategory;
import com.gzt.o2o.service.ShopCategoryService;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService{
	@Autowired
	private ShopCategoryDao ShopCategoryDao;
	
	@Override
	public List<ShopCategory> queryShopCategory(ShopCategory shopCategoryCondition) {
		return ShopCategoryDao.queryShopCategory(shopCategoryCondition);
	}

}
