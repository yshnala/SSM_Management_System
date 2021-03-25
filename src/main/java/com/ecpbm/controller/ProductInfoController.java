package com.ecpbm.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ecpbm.pojo.Pager;
import com.ecpbm.pojo.ProductInfo;
import com.ecpbm.service.ProductInfoService;

@Controller
@RequestMapping("/productinfo")
public class ProductInfoController {
	@Autowired
	ProductInfoService productInfoService;

	// 後台商品列表分頁顯示
	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> list(Integer page, Integer rows, ProductInfo productInfo) {
		// 初始化分頁物件pager
		Pager pager = new Pager();
		pager.setCurPage(page);
		pager.setPerPageRows(rows);
		// 創建params，封裝查詢條件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productInfo", productInfo);
		// 獲取滿足條件的商品總數
		int totalCount = productInfoService.count(params);
		// 獲取滿足條件的商品列表
		List<ProductInfo> productinfos = productInfoService.findProductInfo(productInfo, pager);
		// 創建result物件，保存查詢結果
		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put("total", totalCount);
		result.put("rows", productinfos);
		// 將結果以JSON格式發送到前端Controller
		return result;
	}

	// 增加商品
	@RequestMapping(value = "/addProduct", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String addProduct(ProductInfo pi, @RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, ModelMap model) {
		// Server端upload資料夾路徑
		String path = request.getSession().getServletContext().getRealPath("product_images");
		// 獲取檔名
		String fileName = file.getOriginalFilename();
		// new一個File物件
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try {
		
			// 將上傳文件寫入到Server指定資料夾
			file.transferTo(targetFile);
			pi.setPic(fileName);
			productInfoService.addProductInfo(pi);
			return "{\"success\":\"true\",\"message\":\"商品新增成功\"}";
		} catch (Exception e) {
			return "{\"success\":\"false\",\"message\":\"商品新增失敗\"}";
		}
	}

	// 修改商品
	@RequestMapping(value = "/updateProduct", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateProduct(ProductInfo pi, @RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, ModelMap model) {
		// Server端upload物件路徑
		String path = request.getSession().getServletContext().getRealPath("product_images");
		// 獲取檔名
		String fileName = file.getOriginalFilename();
		// new一個File物件
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try {
			// 將上傳檔案寫入Server指定目錄
			file.transferTo(targetFile);
			pi.setPic(fileName);
			productInfoService.modifyProductInfo(pi);
			return "{\"success\":\"true\",\"message\":\"商品修改成功\"}";
		} catch (Exception e) {
			return "{\"success\":\"false\",\"message\":\"商品修改失敗\"}";
		}
	}

	// 商品下架(删除商品)
	@RequestMapping(value = "/deleteProduct", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String deleteProduct(@RequestParam(value = "id") String id, @RequestParam(value = "flag") String flag) {
		String str = "";
		try {
			productInfoService.modifyStatus(id.substring(0, id.length() - 1), Integer.parseInt(flag));
			str = "{\"success\":\"true\",\"message\":\"删除成功\"}";
		} catch (Exception e) {
			str = "{\"success\":\"false\",\"message\":\"刪除失敗\"}";
		}
		return str;
	}
	
	// 獲取在售商品列表
	@ResponseBody
	@RequestMapping("/getOnSaleProduct")
	public List<ProductInfo> getOnSaleProduct() {
		List<ProductInfo> piList = productInfoService.getOnSaleProduct();
		return piList;
	}
	
	// 根據商品id獲取商品物件
	@ResponseBody
	@RequestMapping("/getPriceById")
	public String getPriceById(@RequestParam(value = "pid") String pid) {
		if (pid != null && !"".equals(pid)) {
			ProductInfo pi = productInfoService.getProductInfoById(Integer.parseInt(pid));
			return pi.getPrice() + "";
		}else{
			return "";
		}
	}

}
