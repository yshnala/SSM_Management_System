package com.ecpbm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.ecpbm.dao.provider.UserInfoDynaSqlProvider;
import com.ecpbm.pojo.UserInfo;

public interface UserInfoDao {
	// 獲取系統合法用戶，即Table user_info中status為1的用戶列表
	@Select("select * from user_info where status=1")
	public List<UserInfo> getValidUser();

	// 根據用戶id號獲取UserInfo
	@Select("select * from user_info where id=#{id}")
	public UserInfo getUserInfoById(int id);

	// 分頁獲取用戶訊息
	@SelectProvider(type = UserInfoDynaSqlProvider.class, method = "selectWithParam")
	List<UserInfo> selectByPage(Map<String, Object> params);

	// 根據條件查詢客戶總數
	@SelectProvider(type = UserInfoDynaSqlProvider.class, method = "count")
	Integer count(Map<String, Object> params);

	// 更新客戶狀態
	@Update("update user_info set status=#{flag} where id in (${ids})")
	void updateState(@Param("ids") String ids, @Param("flag") int flag);
}
