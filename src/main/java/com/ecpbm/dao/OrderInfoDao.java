package com.ecpbm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.FetchType;

import com.ecpbm.dao.provider.OrderInfoDynaSqlProvider;
import com.ecpbm.pojo.OrderDetail;
import com.ecpbm.pojo.OrderInfo;

public interface OrderInfoDao {
	// 分頁獲取訂單訊息
	@Results({
			@Result(column = "uid", property = "ui", one = @One(select = "com.ecpbm.dao.UserInfoDao.getUserInfoById", fetchType = FetchType.EAGER)) })
	@SelectProvider(type = OrderInfoDynaSqlProvider.class, method = "selectWithParam")
	List<OrderInfo> selectByPage(Map<String, Object> params);

	// 根據條件查詢訂單總數
	@SelectProvider(type = OrderInfoDynaSqlProvider.class, method = "count")
	Integer count(Map<String, Object> params);

	// 保存訂單主表
	@Insert("insert into order_info(uid,status,ordertime,orderprice) "
			+ "values(#{uid},#{status},#{ordertime},#{orderprice})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int saveOrderInfo(OrderInfo oi);

	// 保存訂單明細
	@Insert("insert into order_detail(oid,pid,num) values(#{oid},#{pid},#{num})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int saveOrderDetail(OrderDetail od);

	// 根據訂單編號獲取訂單物件
	@Results({
			@Result(column = "uid", property = "ui", one = @One(select = "com.ecpbm.dao.UserInfoDao.getUserInfoById", fetchType = FetchType.EAGER)) })
	@Select("select * from order_info where id = #{id}")
	public OrderInfo getOrderInfoById(int id);

	// 根據訂單編號獲取訂單明細
	@Results({
			@Result(column = "pid", property = "pi", one = @One(select = "com.ecpbm.dao.ProductInfoDao.getProductInfoById", fetchType = FetchType.EAGER)) })
	@Select("select * from order_detail where oid = #{oid}")
	public List<OrderDetail> getOrderDetailByOid(int oid);

	// 刪除訂單主表紀錄
	@Delete("delete from order_info where id=#{id}")
	public int deleteOrderInfo(int id);

	// 根據訂單編號，刪除訂單明細紀錄
	@Delete("delete from order_detail where oid=#{id}")
	public int deleteOrderDetail(int id);

}
