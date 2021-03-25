package com.ecpbm.service;

import java.util.List;

import com.ecpbm.pojo.Type;

public interface TypeService {
	// 獲取產品類型
	public List<Type> getAll();
	
	// 新增產品類型
	public int addType(Type type); 
	
	// 更新產品類型
	public void updateType(Type type);
}
