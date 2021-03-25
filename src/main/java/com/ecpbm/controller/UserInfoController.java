package com.ecpbm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecpbm.pojo.Pager;
import com.ecpbm.pojo.UserInfo;
import com.ecpbm.service.UserInfoService;

@Controller
@RequestMapping("/userinfo")
public class UserInfoController {

	@Autowired
	UserInfoService userInfoService;

	@RequestMapping("/getValidUser")
	@ResponseBody
	public List<UserInfo> getValidUser() {
		List<UserInfo> uiList = userInfoService.getValidUser();
		UserInfo ui = new UserInfo();
		ui.setId(0);
		ui.setUserName("請選擇...");
		uiList.add(0, ui);
		return uiList;
	}

	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> userlist(Integer page, Integer rows, UserInfo userInfo) {
		// 創建分頁類物件
		Pager pager = new Pager();
		pager.setCurPage(page);
		pager.setPerPageRows(rows);
		// 創建params物件，封裝查詢條件
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userInfo", userInfo);
		// 根據查詢條件，獲取客戶紀錄
		int totalCount = userInfoService.count(params);
		// 根據查詢條件，分頁獲取客戶列表
		List<UserInfo> userinfos = userInfoService.findUserInfo(userInfo, pager);
		// 創建對象result，保存查詢結果
		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put("total", totalCount);
		result.put("rows", userinfos);
		return result;
	}

	// 更新客戶狀態
	@RequestMapping(value = "/setIsEnableUser", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String setIsEnableUser(@RequestParam(value = "uids") String uids,
			@RequestParam(value = "flag") String flag) {
		try {
			userInfoService.modifyStatus(uids.substring(0, uids.length() - 1), Integer.parseInt(flag));
			return "{\"success\":\"true\",\"message\":\"更改成功\"}";
		} catch (Exception e) {
			return "{\"success\":\"false\",\"message\":\"更改失敗\"}";
		}
	}

}
