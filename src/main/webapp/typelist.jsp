<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'typelist.jsp' starting page</title>
    
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
    <table id="typeDg" class="easyui-datagrid"></table>
	<div id="typeTb" style="padding:2px 5px;">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true" onclick="addType();">新增</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-edit" plain="true" onclick="editType();">修改</a> 
	</div>
	
	<div id="typeDlg" class="easyui-dialog" title="New Type" closed="true"
		style="width:500px;">
		<div style="padding:10px 60px 20px 60px">
			<form id="typeForm" method="POST" action="">
				<table cellpadding="5">					
					<tr>
						<td>產品名稱:</td>
						<td><input class="easyui-textbox" type="text" id="name"
							name="name" data-options="required:true"></input></td>
					</tr>					
				</table>
			</form>
			<div style="text-align:center;padding:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="saveType();">儲存</a> <a href="javascript:void(0)"
					class="easyui-linkbutton" onclick="clearForm();">清空</a>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {
			$('#typeDg').datagrid({
				singleSelect : false, //設置datagrid為單選
				url : 'type/getType/0', //為datagrid設置資料來源				
				rownumbers : true, //顯示行號
				fit : true, //設置自適應
				toolbar : '#typeTb', //為datagrid添加工具欄
				columns : [ [ { //編輯datagrid的列
					title : '序號',
					field : 'id',
					align : 'center',
					checkbox : true
				}, {
					field : 'name',
					title : '商品類型',
					width : 200
				}] ]
			});
		});

		var urls;
		var data;

		
		function addType() {
			$('#typeDlg').dialog('open').dialog('setTitle', '新增商品類型');
			$('#typeDlg').form('clear');
			urls = 'type/addType';
		}

		function saveType() {
			$("#typeForm").form("submit", {
				url : urls, //使用参数				
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.success == 'true') {
						$("#typeDg").datagrid("reload");
					}
					$("#typeDlg").dialog("close");
					$.messager.show({
						title : "提示訊息",
						msg : result.message
					});
				}
			});
		}
		function clearForm() {
			$('#typeForm').form('clear');
		}

		function editType() {
			var row = $("#typeDg").datagrid("getSelected");
			if (row) {
				$("#typeDlg").dialog("open").dialog('setTitle', '修改產品訊息');
				$("#typeForm").form("load", {
					"name" : row.name
				});
				urls = "type/updateType?id=" + row.id;
			}
		}		
		
	</script>
  </body>
</html>
