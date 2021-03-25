<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'createorder.jsp' starting page</title>

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

	<!-- 定義table,用於創建easy ui的datagrid元件 -->
	<table id="odbox"></table>

	<!-- 創建datagrid元件的工具欄 -->
	<div id="ordertb" style="padding: 2px 5px;">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true" onclick="addOrderDetail();">增加訂單明細</a><a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-save" plain="true" onclick="saveorder();">儲存訂單</a><a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-remove" plain="true" onclick="removeOrderDetail();">刪除訂單明細</a>
	</div>

	<!-- 創建訂單主表 -->
	<div id="divOrderInfo">
		<div style="padding: 3px">
			客戶名稱&nbsp;<input style="width: 115px;" id="create_uid"
				class="easyui-combobox" name="create_uid" value="0"
				data-options="valueField:'id',textField:'userName',url:'userinfo/getValidUser'">&nbsp;&nbsp;&nbsp;
			訂單金額&nbsp;<input type="text" name="create_orderprice"
				id="create_orderprice" class="easyui-textbox" readonly="readonly"
				style="width: 115px" /> &nbsp;&nbsp;
		</div>
		<div style="padding: 3px">
			訂單日期&nbsp;<input type="text" name="create_ordertime"
				id="create_ordertime" class="easyui-datebox" style="width: 115px"
				value="<%=new Date().toLocaleString()%>" /> &nbsp;&nbsp;
			訂單狀態&nbsp;<select id="create_status" class="easyui-combobox"
				name="create_status" style="width: 115px;">
				<option value="未付款" selected>未付款</option>
				<option value="已付款">已付款</option>
				<option value="待发货">待出貨</option>
				<option value="已发货">已出貨</option>
				<option value="已完成">已完成</option>
			</select>
		</div>
	</div>

<script type="text/javascript">	      
	var $odbox = $('#odbox');
	$(function() {
		$odbox.datagrid({
			rownumbers : true,
			singleSelect : false, 
			fit : true,
			toolbar : '#ordertb',
			header : '#divOrderInfo',
			columns : [ [ {
				title : '序號',
				field : '',
				align : 'center',
				checkbox : true
			}, {
				field : 'pid',
				title : '商品名稱',
				width : 300,
				editor : {
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						url : 'productinfo/getOnSaleProduct',
						onChange: function (newValue, oldValue) {
	                       var rows = $odbox.datagrid('getRows');      
	                       var orderprice=0;                     
	                       for (var i = 0; i < rows.length; i++) {                                          
	                           var pidEd = $('#odbox').datagrid('getEditor', {
	                               index: i,
	                               field: 'pid'
	                           });
	                           var priceEd = $('#odbox').datagrid('getEditor', {
	                               index: i,
	                               field: 'price'
	                           });
	                           var totalpriceEd = $('#odbox').datagrid('getEditor', {
	                               index: i,
	                               field: 'totalprice'
	                           });
	                           var numEd = $('#odbox').datagrid('getEditor', {
	                               index: i,
	                               field: 'num'
	                           });
	                           if (pidEd != null){
	                           	var pid=$(pidEd.target).combobox('getValue');                                     	
	                           	$.ajax({
								  type: 'POST',
								  url: 'productinfo/getPriceById',
								  data: {pid : pid},
								  success:  function(result) {
										$(priceEd.target).numberbox('setValue',result);
										$(totalpriceEd.target).numberbox('setValue',result * $(numEd.target).numberbox('getValue'));
										orderprice=Number(orderprice)+Number($(totalpriceEd.target).numberbox('getValue'));
								  },
								  dataType: 'json',
								  async : false
								});
	                          }
	                       }
	                       $("#create_orderprice").textbox("setValue",orderprice);
	                    }
					}
				}
			}, {
				field : 'price',
				title : '單價',
				width : 80,
				editor: {
					type : "numberbox",		
					options: {
						editable : false
					}	
				}					
			} , {
				field : 'num',
				title : '數量',
				width : 50,
				editor : {
					type : 'numberbox',
					options :{
						onChange: function (newValue, oldValue) {
	                        var rows = $odbox.datagrid('getRows');
	                        var orderprice=0;  
	                        for (var i = 0; i < rows.length; i++) { 
	                            var priceEd = $('#odbox').datagrid('getEditor', {
	                                index: i,
	                                field: 'price'
	                            });
	                            var totalpriceEd = $('#odbox').datagrid('getEditor', {
	                                index: i,
	                                field: 'totalprice'
	                            });
	                            var numEd = $('#odbox').datagrid('getEditor', {
	                                index: i,
	                                field: 'num'
	                            });
	                            $(totalpriceEd.target).numberbox('setValue',$(priceEd.target).numberbox('getValue') * $(numEd.target).numberbox('getValue'));
	                            orderprice=Number(orderprice)+Number($(totalpriceEd.target).numberbox('getValue'));
	                        }
	                        $("#create_orderprice").textbox("setValue",orderprice);
						}
					}
	             }
			}, {
				field : 'totalprice',
				title : '小計',
				width : 100,
				editor: {
					type : "numberbox",		
					options: {
						editable : false
					}	
				}	
			}  ] ]
		});
	});

		// datagrid中加入紀錄行
		function addOrderDetail() {
			$odbox.datagrid('appendRow', {
				num : '1',
				price : '0',
				totalprice : '0'
			});
			var rows = $odbox.datagrid('getRows');
			// 讓增加的行處於可編輯狀態
			$odbox.datagrid('beginEdit', rows.length - 1);
		}

		// datagrid中删除紀錄行
		function removeOrderDetail() {
		    // 獲取所選擇的行紀錄
			var rows = $odbox.datagrid('getSelections');
			if (rows.length > 0) {
				// 獲取訂單金額值
				var create_orderprice =  $("#create_orderprice").textbox("getValue");
				//遍歷選中的行紀錄，以更新訂單金額
				for (var i = 0; i < rows.length; i++) {
					var index = $odbox.datagrid('getRowIndex', rows[i]);
					var totalpriceEd = $('#odbox').datagrid('getEditor', {
                          index: index,
                          field: 'totalprice'
		             });	             
					create_orderprice = create_orderprice - Number($(totalpriceEd.target).numberbox('getValue'));
					$odbox.datagrid('deleteRow', index);
				}
				$("#create_orderprice").textbox("setValue",create_orderprice);
			} else {
				$.messager.alert('提示', '請選擇要刪除的行', 'info');
			}
		}

		// 保存訂單
		function saveorder() {	
		    // 獲取客戶訂單
			var uid = $("#create_uid").combobox("getValue");
			if(uid==0){
				$.messager.alert('提示', '請選擇客戶名稱', 'info');
			} else {
				// 取消datagrid控制的行編輯狀態
				create_endEdit();
				// 定義orderinfo存放訂單主表數據
				var orderinfo = [];
				// 獲取訂單時間
				var ordertime = $("#create_ordertime").datebox("getValue");
				// 獲取訂單狀態
				var status = $("#create_status").combobox("getValue");				
				// 獲取訂單金額
				var orderprice = $("#create_orderprice").textbox("getValue");
				orderinfo.push({
					ordertime : ordertime,
					uid : uid,
					status : status,
					orderprice : orderprice
				});
				// 獲取訂單明細（即datagrid元件中的紀錄）
				if ($odbox.datagrid('getChanges').length) {
					// 獲取datagrid元件中插入的記錄行
					var inserted = $odbox.datagrid('getChanges', "inserted");
					// 獲取datagrid元件中删除的記錄行
					var deleted = $odbox.datagrid('getChanges', "deleted");
					// 獲取datagrid元件中更新的記錄行
					var updated = $odbox.datagrid('getChanges', "updated");							
					// 定義effectRow,保存inserted和orderinfo
					var effectRow = new Object();
					if (inserted.length) {
						effectRow["inserted"] = JSON.stringify(inserted);
					}					
					effectRow["orderinfo"] = JSON.stringify(orderinfo);
					// 提交請求
					$.post(
					"orderinfo/commitOrder",
					effectRow,
					function(data) {
						if (data == 'success') {
							$.messager.alert("提示", "創建成功！");
							$odbox.datagrid('acceptChanges');
							if ($('#tabs').tabs('exists', '創建訂單')) {
								$('#tabs').tabs('close', '創建訂單');
							}
							$("#orderDg").datagrid('reload'); 
						} else {
							$.messager.alert("提示", "創建失敗！");
						}
					});
				}
			}	

		}

		// 取消datagrid控件的行編輯狀態
		function create_endEdit() {
			var rows = $odbox.datagrid('getRows');
			for (var i = 0; i < rows.length; i++) {
				$odbox.datagrid('endEdit', i);
			}
		}
	</script>

</body>
</html>
