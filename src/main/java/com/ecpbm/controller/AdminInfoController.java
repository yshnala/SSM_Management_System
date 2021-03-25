package com.ecpbm.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.ecpbm.pojo.AdminInfo;
import com.ecpbm.pojo.Functions;
import com.ecpbm.pojo.TreeNode;
import com.ecpbm.service.AdminInfoService;
import com.ecpbm.util.JsonFactory;

@SessionAttributes(value = { "admin" })
@Controller
@RequestMapping("/admininfo")
public class AdminInfoController {
	@Autowired
	private AdminInfoService adminInfoService;

	@RequestMapping(value = "/login", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String login(AdminInfo ai, ModelMap model) {
		// 後台登入驗證
		AdminInfo admininfo = adminInfoService.login(ai);
		if (admininfo != null && admininfo.getName() != null) {
			// 通過驗證後,再判斷是否已為該管理員分配功能權限
			if (adminInfoService.getAdminInfoAndFunctions(admininfo.getId()).getFs().size() > 0) {
				// 驗證通過且已分配功能權限，則將admininfo物件存入model中
				model.put("admin", admininfo);
				// 以JSON格式向頁面發送成功訊息
				return "{\"success\":\"true\",\"message\":\"登入成功\"}";
			} else {
				return "{\"success\":\"false\",\"message\":\"您沒有權限，請聯絡超級管理員設置權限！\"}";
			}
		} else
			return "{\"success\":\"false\",\"message\":\"登錄失敗\"}";
	}

	@RequestMapping("getTree")
	@ResponseBody
	public List<TreeNode> getTree(@RequestParam(value = "adminid") String adminid) {
		// 根據管理員編號，獲取AdminInfo物件
		AdminInfo admininfo = adminInfoService.getAdminInfoAndFunctions(Integer.parseInt(adminid));
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		// 獲取關聯的Functions對象集合
		List<Functions> functionsList = admininfo.getFs();
		// 對List<Functions>類型的Functions對象集合排序
		Collections.sort(functionsList);
		// 將排序後的Functions對象集合轉換到List<TreeNode>類型的列表nodes
		for (Functions functions : functionsList) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(functions.getId());
			treeNode.setFid(functions.getParentid());
			treeNode.setText(functions.getName());
			nodes.add(treeNode);
		}
		// 調用自定義的JsonFactory的buildtree方法，為nodes列表中的各個TreeNode元素中的
		// children屬性賦值(該節點包含的子節點)
		List<TreeNode> treeNodes = JsonFactory.buildtree(nodes, 0);
		return treeNodes;
	}

	// 退出
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ResponseBody
	public String logout(SessionStatus status) {
		// @SessionAttributes清除
		status.setComplete();
		return "{\"success\":\"true\",\"message\":\"註銷成功\"}";
	}
}
