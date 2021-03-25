package com.ecpbm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ecpbm.pojo.Type;

public interface TypeDao {
	// 查詢所有商品類型
	@Select("select * from type")
	public List<Type> selectAll();

	// 根據類型編號查詢商品類型
	@Select("select * from type where id = #{id}")
	public Type selectById(int id);
	
	// 增加商品類型
	@Insert("insert into type(name) values(#{name})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	public int add(Type type);
	
	// 更新商品類型
	@Update("update type set name = #{name} where  id = #{id}")
	public int update(Type type);	
}
