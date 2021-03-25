package com.ecpbm.service;

import java.util.List;
import java.util.Map;

import com.ecpbm.pojo.Pager;
import com.ecpbm.pojo.ProductInfo;

public interface ProductInfoService {
	// 分頁顯示商品
	List<ProductInfo> findProductInfo(ProductInfo productInfo, Pager pager);

	// 商品記數
	Integer count(Map<String, Object> params);

	// 增加商品
	public void addProductInfo(ProductInfo pi);
	
	// 修改商品
	public void modifyProductInfo(ProductInfo pi);
	
	// 更新商品狀態
	void modifyStatus(String ids, int flag);

	// 取得在售商品列表
	List<ProductInfo> getOnSaleProduct(); 
	
	// 根據產品id獲取產品
	ProductInfo getProductInfoById(int id);
}
