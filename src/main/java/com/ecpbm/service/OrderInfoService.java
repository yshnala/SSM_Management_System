package com.ecpbm.service;

import java.util.List;
import java.util.Map;
import com.ecpbm.pojo.*;

public interface OrderInfoService {
	// 分頁顯示訂單
	List<OrderInfo> findOrderInfo(OrderInfo orderInfo, Pager pager);

	// 訂單記數
	Integer count(Map<String, Object> params);

	// 新增訂單主表
	public int addOrderInfo(OrderInfo oi);

	// 新增訂單明細
	public int addOrderDetail(OrderDetail od);

	// 根據訂單編號獲取訂單訊息
	public OrderInfo getOrderInfoById(int id);

	// 以訂單編號取得訂單明細
	public List<OrderDetail> getOrderDetailByOid(int oid);

	// 刪除訂單
	public int deleteOrder(int id);
}
