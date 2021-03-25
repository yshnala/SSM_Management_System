package com.ecpbm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecpbm.pojo.OrderDetail;
import com.ecpbm.pojo.OrderInfo;
import com.ecpbm.pojo.Pager;
import com.ecpbm.service.OrderInfoService;
import com.ecpbm.service.ProductInfoService;
import com.ecpbm.service.UserInfoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonParseException;

@Controller
@RequestMapping("/orderinfo")
public class OrderInfoController {
	@Autowired
	OrderInfoService orderInfoService;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	ProductInfoService productInfoService;

	// 分頁顯示
	@RequestMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> list(Integer page, Integer rows, OrderInfo orderInfo) {
		// 初始化一個分頁對象Pager
		Pager pager = new Pager();
		pager.setCurPage(page);
		pager.setPerPageRows(rows);
		// 創建對象params,用來封裝查詢條件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderInfo", orderInfo);
		//獲取滿足條件的訂單總數
		int totalCount = orderInfoService.count(params);
		// 獲取滿足條件的訂單列表
		List<OrderInfo> orderinfos = orderInfoService.findOrderInfo(orderInfo, pager);
		// 創建result物件，存查詢結果
		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put("total", totalCount);
		result.put("rows", orderinfos);
		return result;
	}

	// 存訂單
	@ResponseBody
	@RequestMapping(value = "/commitOrder")
	public String commitOrder(String inserted, String orderinfo)
			throws JsonParseException, JsonMappingException, IOException {
		try {
			// 創建ObjectMapper物件,實現JavaBean和JSON的轉換
			ObjectMapper mapper = new ObjectMapper();
			// 設置輸入時忽略在JSON字串中存在但Java物件實際沒有的屬性
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			// 將json字符串orderinfo轉換成JavaBean物件（訂單訊息）
			OrderInfo oi = mapper.readValue(orderinfo, OrderInfo[].class)[0];
			// 存訂單訊息
			orderInfoService.addOrderInfo(oi);
			// 將json字串轉換成List<OrderDetail>集合（訂單明細訊息）
			List<OrderDetail> odList = mapper.readValue(inserted, new TypeReference<ArrayList<OrderDetail>>() {
			});
			// 將訂單明細的其他屬性賦值
			for (OrderDetail od : odList) {
				od.setOid(oi.getId());
				// 存訂單明細
				orderInfoService.addOrderDetail(od);
			}
			return "success";
		} catch (Exception e) {
			return "failure";
		}
	}

	// 根據訂單id獲取要查看的訂單, 在返回訂單明細頁
	@RequestMapping("/getOrderInfo")
	public String getOrderInfo(String oid, Model model) {
		OrderInfo oi = orderInfoService.getOrderInfoById(Integer.parseInt(oid));
		model.addAttribute("oi", oi);
		return "orderdetail";
	}

	// 根據訂單明細id獲取訂單明細
	@RequestMapping("/getOrderDetails")
	@ResponseBody
	public List<OrderDetail> getOrderDetails(String oid) {
		List<OrderDetail> ods = orderInfoService.getOrderDetailByOid(Integer.parseInt(oid));
		for (OrderDetail od : ods) {
			// od.setPid(od.getPi().getId());
			od.setPrice(od.getPi().getPrice());
			od.setTotalprice(od.getPi().getPrice() * od.getNum());
		}
		return ods;
	}

	// 刪除訂單
	@ResponseBody
	@RequestMapping(value = "/deleteOrder", produces = "text/html;charset=UTF-8")
	public String deleteOrder(String oids) {
		String str = "";
		try {
			oids = oids.substring(0, oids.length() - 1);
			String[] ids = oids.split(",");
			for (String id : ids) {
				orderInfoService.deleteOrder(Integer.parseInt(id));
			}
			str = "{\"success\":\"true\",\"message\":\"刪除成功！\"}";
		} catch (Exception e) {
			str = "{\"success\":\"false\",\"message\":\"刪除失敗！\"}";
		}
		return str;
	}

}
