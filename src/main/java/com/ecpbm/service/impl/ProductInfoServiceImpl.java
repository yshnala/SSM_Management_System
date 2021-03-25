package com.ecpbm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ecpbm.dao.ProductInfoDao;
import com.ecpbm.pojo.Pager;
import com.ecpbm.pojo.ProductInfo;
import com.ecpbm.service.ProductInfoService;

@Service("productInfoService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class ProductInfoServiceImpl implements ProductInfoService {

	@Autowired
	ProductInfoDao productInfoDao;

	@Override
	public List<ProductInfo> findProductInfo(ProductInfo productInfo, Pager pager) {
		// 創建對象params
		Map<String, Object> params = new HashMap<String, Object>();
		// 將封裝有查詢條件的productInfo對象放入params
		params.put("productInfo", productInfo);
		// 根據條件計算商品總數
		int recordCount = productInfoDao.count(params);
		// 給pager對象設置rowCount屬性值(紀錄總數)
		pager.setRowCount(recordCount);
		if (recordCount > 0) {
			// 將page對象放入params
			params.put("pager", pager);
		}
		// 分頁獲取商品訊息
		return productInfoDao.selectByPage(params);
	}

	@Override
	public Integer count(Map<String, Object> params) {
		return productInfoDao.count(params);
	}

	@Override
	public void addProductInfo(ProductInfo pi) {
		productInfoDao.save(pi);
	}

	@Override
	public void modifyProductInfo(ProductInfo pi) {
		productInfoDao.edit(pi);
	}

	@Override
	public void modifyStatus(String ids, int flag) {
		productInfoDao.updateState(ids, flag);
	}

	@Override
	public List<ProductInfo> getOnSaleProduct() {
		return productInfoDao.getOnSaleProduct();
	}

	@Override
	public ProductInfo getProductInfoById(int id) {
		return productInfoDao.getProductInfoById(id);
	}
}
