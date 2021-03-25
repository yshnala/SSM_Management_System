<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
	<!-- 定義table, 創建Easy UI的datagrid元件 -->
	<table id="orderDg" class="easyui-datagrid"></table>
	
	<!-- 工具欄 -->
	<div id="orderTb" style="padding:2px 5px;">
	   <a href="javascript:void(0)" class="easyui-linkbutton" 
	      iconCls="icon-edit" plain="true" onclick="editOrder();">查看明細</a> 
	   <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove"
			onclick="removeOrder();" plain="true">刪除訂單</a>
	</div>
	
	<!-- 查詢表單 -->
	<div id="searchOrderTb" style="padding:2px 5px;">
		<form id="searchOrderForm">
			<div style="padding:3px">
				訂單編號&nbsp;<input class="easyui-textbox" name="search_oid"
					id="search_oid" style="width:110px" />
			</div>
			<div style="padding:3px">
				客戶名稱&nbsp;<input style="width:115px;" id="search_uid"
					class="easyui-combobox" value="0" name="search_uid"
					data-options="valueField:'id',textField:'userName',url:'userinfo/getValidUser'">&nbsp;&nbsp;&nbsp;
				訂單狀態&nbsp;<select id="search_status" class="easyui-combobox" name="search_status" style="width:115px;">
					<option value="請選擇" selected>請選擇</option>
					<option value="未付款">未付款</option>
					<option value="已付款">已付款</option>
					<option value="待出貨">待出貨</option>
					<option value="已出貨">已出貨</option>
					<option value="已完成">已完成</option>
				</select>&nbsp;&nbsp;&nbsp; 訂單時間 &nbsp;<input class="easyui-datebox"
					name="orderTimeFrom" id="orderTimeFrom" style="width:115px;" /> ~
				<input class="easyui-datebox" name="orderTimeTo" id="orderTimeTo"
					style="width:115px;" /> <a href="javascript:void(0)"
					class="easyui-linkbutton" iconCls="icon-search" plain="true"
					onclick="searchOrderInfo();">查詢</a>
			</div>
		</form>
	</div>


	<script type="text/javascript">
		$(function() {
			$('#orderDg').datagrid({
				singleSelect : false,
				url : 'orderinfo/list', //為datagrid設置資料來源
				queryParams : {}, //查詢條件
				pagination : true, //啟用分頁
				pageSize : 5, //設置初始每頁筆數
				pageList : [ 5, 10, 15 ], //設置可供選擇的每頁筆數
				rownumbers : true, //顯示行號
				fit : true, //設置自適應
				toolbar : '#orderTb', //為datagrid添加工具欄
				header : '#searchOrderTb', //為datagrid標頭添加搜索欄
				columns : [ [ { //編輯datagrid的列
					title : '序號',
					field : 'id',
					align : 'center',
					checkbox : true
				}, {
					field : 'ui',
					title : '訂單客戶',
					formatter : function(value, row, index) {
						if (row.ui) {
							return row.ui.userName;
						} else {
							return value;
						}
					},
					width : 100
				}, {
					field : 'status',
					title : '訂單狀態',
					width : 80
				}, {
					field : 'ordertime',
					title : '訂單時間',
					width : 100
				}, {
					field : 'orderprice',
					title : '訂單金額',
					width : 100
				} ] ]
			});
		});

		var urls;
		var data;
		
		// 刪除訂單
		function removeOrder() {
			// 獲取選中的訂單紀錄行
			var rows = $("#orderDg").datagrid('getSelections');
			if (rows.length > 0) {
				$.messager.confirm('Confirm', '確定要刪除嗎?', function(r) {
					if (r) {
						var ids = "";
						//獲取選中訂單id,保存到ids中
						for (var i = 0; i < rows.length; i++) {
							ids += rows[i].id + ",";
						}
						//發出請求
						$.post('orderinfo/deleteOrder', {
							oids : ids
						}, function(result) {
							if (result.success == 'true') {
								$("#orderDg").datagrid('reload'); 
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
				$.messager.alert('提示', '請選擇要刪除的行', 'info');
			}
		}		

		// 查看明细
		function editOrder() {
		    var rows = $("#orderDg").datagrid('getSelections');
			if (rows.length > 0) {
				var row = $("#orderDg").datagrid("getSelected");
				if ($('#tabs').tabs('exists', '訂單明細')) {
					$('#tabs').tabs('close', '訂單明細');
				}
				$('#tabs').tabs('add', {
					title : "訂單明細",
					href : 'orderinfo/getOrderInfo?oid=' + row.id,
					closable : true
				});
			}else {
				$.messager.alert('提示', '請選擇要修改的訂單', 'info');
			}
		}

	// 查詢訂單
	function searchOrderInfo() {			
		var oid = $('#search_oid').val();
		var status = $('#search_status').combobox("getValue");
		var uid = $('#search_uid').combobox("getValue");
		var orderTimeFrom = $("#orderTimeFrom").datebox("getValue");
		var orderTimeTo = $("#orderTimeTo").datebox("getValue");
		$('#orderDg').datagrid('load', {
			id : oid,
			status : status,
			uid : uid,
			orderTimeFrom : orderTimeFrom,
			orderTimeTo : orderTimeTo
		});
	}
	</script>
</body>
</html>
