package com.ecpbm.dao;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import com.ecpbm.pojo.AdminInfo;

public interface AdminInfoDao {

	// 根據登入名和密碼查詢管理員
	@Select("select * from admin_info where name = #{name} and pwd = #{pwd}")
	public AdminInfo selectByNameAndPwd(AdminInfo ai);

	//根據管理員id獲取管理員對象及關聯的功能集合
	@Select("select * from admin_info where id = #{id}")
	@Results({ @Result(id = true, column = "id", property = "id"), @Result(column = "name", property = "name"),
			@Result(column = "pwd", property = "pwd"),
			@Result(column = "id", property = "fs", many = @Many(select = "com.ecpbm.dao.FunctionDao.selectByAdminId", fetchType = FetchType.EAGER)) })
	AdminInfo selectById(Integer id);
}
