package com.ecpbm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.ecpbm.pojo.Functions;

public interface FunctionDao {
	// 根據管理員id,獲取功能權限
	@Select("select * from functions where id in (select fid from powers where aid = #{aid} )")
	public List<Functions> selectByAdminId(Integer aid);
}
