<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
  if(session.getAttribute("admin")==null)
  	response.sendRedirect("/digital-um/admin_login.jsp");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'newslist.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
    <!-- 創建一個table -->
	<table id="userListDg" class="easyui-datagrid"></table>
	
	<!-- 創建工具欄 -->
	<div id="userListTb" style="padding:2px 5px;"><a href="javascript:void(0)"
		class="easyui-linkbutton" iconCls="icon-edit" plain="true"
		onclick="SetIsEnableUser(1);">啟用客戶</a> <a href="javascript:void(0)"
		class="easyui-linkbutton" iconCls="icon-remove"
		onclick="SetIsEnableUser(0);" plain="true">禁用客户</a>
	</div>
	
	<!-- 創建搜尋欄 -->
	<div id="searchUserListTb" style="padding:4px 3px;">
		<form id="searchUserListForm">
			<div style="padding:3px ">
				客戶名稱&nbsp;&nbsp;<input class="easyui-textbox" name="search_userName"
					id="search_userName" style="width:110px" /><a href="javascript:void(0)"
					class="easyui-linkbutton" iconCls="icon-search" plain="true"
					onclick="searchUserInfo();">查詢</a>
			</div>			
		</form>
	</div>

	<script type="text/javascript">
		$(function() {
			$('#userListDg').datagrid({
				singleSelect : false,
				url : 'userinfo/list',
				queryParams : {}, //查詢條件
				pagination : true, //啟用分頁
				pageSize : 5, //設置初始每頁筆數
				pageList : [ 5, 10, 15 ], //設置可供選擇的筆數
				rownumbers : true, //顯示行號
				fit : true, //設置自適應
				toolbar : '#userListTb', //為datagrid添加工具欄
				header : '#searchUserListTb', //為datagrid標頭添加搜索欄
				columns : [ [ { //編輯datagrid的列
					title : '序號',
					field : 'id',
					align : 'center',
					checkbox : true
				}, {
					field : 'userName',
					title : '登入名',					
					width : 100
				}, {
					field : 'realName',
					title : '真實姓名',
					width : 80
				}, {
					field : 'sex',
					title : '性别',
					width : 100
				}, {
					field : 'address',
					title : '住址',
					width : 200
				} , {
					field : 'email',
					title : '電子信箱',
					width : 150
				} , {
					field : 'regDate',
					title : '註冊日期',
					width : 100
				} , {
					field : 'status',
					title : '客戶狀態',
					width : 100,
					formatter : function(value, row, index) {
						if (row.status==1) {
							return "啟用";
						} else {
							return "禁用";
						}
					}
				} ] ]
			});
		});

		var urls;
		var data;
		
		function searchUserInfo() {
			var userName = $('#search_userName').textbox("getValue");
			$('#userListDg').datagrid('load', {
				userName : userName
			});
		}
		
		
		// 啟用或禁用客戶
		function SetIsEnableUser(flag) {
			var rows = $("#userListDg").datagrid('getSelections');
			if (rows.length > 0) {
				$.messager.confirm('Confirm', '確定要修改用戶狀態嗎?', function(r) {
					if (r) {
						var uids = "";
						for (var i = 0; i < rows.length; i++) {
							uids += rows[i].id + ",";
						}						
						$.post('userinfo/setIsEnableUser', {
							uids : uids,
							flag : flag
						}, function(result) {
							if (result.success == 'true') {
								$("#userListDg").datagrid('reload'); 
								$.messager.show({
									title : '提示訊息',
									msg : result.message
								});
							} else {
								$.messager.show({
									title : '提示訊息',
									msg : result.message
								});
							}
						}, 'json');

					}
				});
			} else {
				$.messager.alert('提示', '請選擇要啟用或禁用的客戶', 'info');
			}
		}
		
	</script>
</body>
</html>
