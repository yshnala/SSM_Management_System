package com.ecpbm.dao.provider;

import java.util.Map;
import org.apache.ibatis.jdbc.SQL;
import com.ecpbm.pojo.UserInfo;

public class UserInfoDynaSqlProvider {
	// 分頁動態查詢
	public String selectWithParam(Map<String, Object> params) {
		String sql = new SQL() {
			{
				SELECT("*");
				FROM("user_info");
				if (params.get("userInfo") != null) {
					UserInfo userInfo = (UserInfo) params.get("userInfo");
					if (userInfo.getUserName() != null && !userInfo.getUserName().equals("")) {
						WHERE("  userName LIKE CONCAT ('%',#{userInfo.userName},'%') ");
					}
				}
			}
		}.toString();
		if (params.get("pager") != null) {
			sql += " limit #{pager.firstLimitParam} , #{pager.perPageRows}  ";
		}
		return sql;
	}

	// 根據條件動態查詢總記錄數
	public String count(Map<String, Object> params) {
		return new SQL() {
			{
				SELECT("count(*)");
				FROM("user_info");
				if (params.get("userInfo") != null) {
					UserInfo userInfo = (UserInfo) params.get("userInfo");
					if (userInfo.getUserName() != null && !userInfo.getUserName().equals("")) {
						WHERE("  userName LIKE CONCAT ('%',#{userInfo.userName},'%') ");
					}
				}
			}
		}.toString();
	}

}
