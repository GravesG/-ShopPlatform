package com.gzt.o2o.dao;

import com.gzt.o2o.entity.Shop;

public interface ShopDao {
	int insertShop(Shop shop);
	
	int updateShop(Shop shop);
	
	Shop queryByShopId(Long shopId);
}
