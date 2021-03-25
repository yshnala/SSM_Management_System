package com.ecpbm.service;

import com.ecpbm.pojo.AdminInfo;

public interface AdminInfoService {
	// 登入驗證
	public AdminInfo login(AdminInfo ai);

	// 根據管理員編號，獲取管理員對象及關聯的功能權限
	public AdminInfo getAdminInfoAndFunctions(Integer id);
}
