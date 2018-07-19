package com.gzt.o2o.service;

import java.util.List;

import com.gzt.o2o.dto.ProductCategoryExecution;
import com.gzt.o2o.entity.ProductCategory;

public interface ProductCategoryService {
	/**
	 * 查询指定某个店铺商铺下的所有商品类别
	 * 
	 * @param long shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
	
	/**
	 * 批量添加方法
	 * @param productCategoryList
	 * @return
	 * @throws RuntimeException
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
	throws RuntimeException;
}
