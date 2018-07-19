package com.gzt.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzt.o2o.dao.ProductCategoryDao;
import com.gzt.o2o.dto.ProductCategoryExecution;
import com.gzt.o2o.entity.ProductCategory;
import com.gzt.o2o.enums.ProductCategoryStateEnum;
import com.gzt.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws RuntimeException {
		if(productCategoryList!=null && productCategoryList.size()>=0) {
			try {
				int effectedNum = productCategoryDao.batchInsertproductCategory(productCategoryList);
				if(effectedNum <= 0) {
					throw new RuntimeException("店铺类别创建失败");
				}else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new RuntimeException("店铺类别创建失败");
			}
		}else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	
}
