package com.ecpbm.service;

import java.util.List;
import java.util.Map;
import com.ecpbm.pojo.Pager;
import com.ecpbm.pojo.UserInfo;

public interface UserInfoService {
	// 取得合法客户
	public List<UserInfo> getValidUser();

	// 根據用戶編號查詢客戶
	public UserInfo getUserInfoById(int id);

	// 分頁顯示客戶
	List<UserInfo> findUserInfo(UserInfo userInfo, Pager pager);

	// 客戶記數
	Integer count(Map<String, Object> params);
	
	// 修改指定編號的客戶狀態
	void modifyStatus(String ids, int flag); 
}
