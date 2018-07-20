package com.gzt.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="shopadmin",method= {RequestMethod.GET})
public class ShopAdminController {
	@RequestMapping(value="/shopoperation")
	public String shopOperation() {
		return "shop/shopoperation";
	}
	
	@RequestMapping(value = "/shoplist", method = RequestMethod.GET)
	private String myList() {
		return "shop/shoplist";
	}
	
	@RequestMapping(value = "/shopmanagement", method = RequestMethod.GET)
	private String shopMangement() {
		return "shop/shopmanagement";
	}
	
	@RequestMapping(value = "/productcategorymanage", method = RequestMethod.GET)
	private String productCategoryManagement() {
		return "shop/productcategorymanage";
	}
}
