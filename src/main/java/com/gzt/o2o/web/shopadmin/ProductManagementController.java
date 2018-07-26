package com.gzt.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzt.o2o.dto.ImageHolder;
import com.gzt.o2o.dto.ProductExecution;
import com.gzt.o2o.entity.Product;
import com.gzt.o2o.entity.ProductCategory;
import com.gzt.o2o.entity.Shop;
import com.gzt.o2o.enums.ProductStateEnum;
import com.gzt.o2o.service.ProductCategoryService;
import com.gzt.o2o.service.ProductService;
import com.gzt.o2o.util.CodeUtil;
import com.gzt.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;

	private static final int IMAGEMAXCOUNT = 6;
	
	
	@RequestMapping(value = "/getproductlistbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductLlistByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取页码信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//从session中获取shopId
		Shop currentShop = (Shop) request.getSession().getAttribute(
				"currentShop");
		
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null)
				&& (currentShop.getShopId() != null)) {
			long productCategoryId = HttpServletRequestUtil.getLong(request,
					"productCategoryId");
			String productName = HttpServletRequestUtil.getString(request,
					"productName");
			Product productCondition = compactProductCondition(
					currentShop.getShopId(), productCategoryId, productName);
			ProductExecution pe = productService.getProductList(
					productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	
	@RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductById(@RequestParam Long productId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(productId>-1) {
			Product product = productService.getProductById(productId);
			//获取商品类别
			List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		boolean statusChange = HttpServletRequestUtil.getBoolean(request,
				"statusChange");
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//如果请求中存在文件流
		try {
			//判断request里面是否含有Multipart文件流
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, productImgList);
			} 
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			String productStr = HttpServletRequestUtil.getString(request,
					"productStr");
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (product != null) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute(
						"currentShop");
				product.setShop(currentShop);

				ProductExecution pe = productService.modifyProduct(product,
						thumbnail, productImgList);
				if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		
		return modelMap;
	}

	private ImageHolder handleImage(HttpServletRequest request, List<ImageHolder> productImgList) throws IOException {
		ImageHolder thumbnail = null;
		MultipartHttpServletRequest multipartRequest = null;
		multipartRequest = (MultipartHttpServletRequest) request;
		//取出缩略图
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
		//取出详情图并构建List<ImageHolder>列表，最多6张照片
		for (int i = 0; i < IMAGEMAXCOUNT; i++) {
			CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest
					.getFile("productImg" + i);
			if (productImgFile != null) {
				ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
				productImgList.add(productImg);
			}else {
				break;
			}
		}
		return thumbnail;
	}
	

	@RequestMapping(value = "/addproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//json转换为实体类的对象
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		String productStr = HttpServletRequestUtil.getString(request,
				"productStr");
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			//判断request里面是否含有Multipart文件流
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, productImgList);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (product != null && thumbnail != null && productImgList.size() > 0) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute(
						"currentShop");
				Shop shop = new Shop();
				shop.setShopId(currentShop.getShopId());
				product.setShop(shop);
				ProductExecution pe = productService.addProduct(product,
						thumbnail, productImgList);
				if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;
	}

	private Product compactProductCondition(long shopId,
			long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if (productCategoryId != -1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if (productName != null) {
			productCondition.setProductName(productName);
		}
		return productCondition;
	}
}
