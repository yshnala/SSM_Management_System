<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
	<table id="editodbox"></table>

	<div id="editordertb" style="padding: 2px 5px;">
		<div id="editdivOrderInfo">
			<div style="padding: 3px">
				客戶名稱&nbsp;<input style="width: 115px;" id="edit_uid"
					class="easyui-textbox" name="edit_uid" readonly="readonly"
					value="${requestScope.oi.ui.userName }">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				訂單金額&nbsp;<input type="text" name="edit_orderprice"
					id="edit_orderprice" value="${requestScope.oi.orderprice }"
					class="easyui-textbox" readonly="readonly" style="width: 115px" />
				&nbsp;&nbsp;
			</div>
			<div style="padding: 3px">
				訂單日期&nbsp;<input type="text" name="edit_ordertime" readonly="readonly"
					id="edit_ordertime" value="${requestScope.oi.ordertime }"
					class="easyui-datebox" style="width: 115px" /> &nbsp;&nbsp;&nbsp;&nbsp; 訂單狀態&nbsp;<input
					id="edit_status" class="easyui-textbox" name="edit_status"
					style="width: 115px;" readonly="readonly" value="${requestScope.oi.status }">					
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var $editodbox = $('#editodbox');
		$(function() {
			$editodbox.datagrid({
				url : 'orderinfo/getOrderDetails?oid=${requestScope.oi.id }',						
				rownumbers : true,
				singleSelect : false,
				fit : true,
				toolbar : '#editordertb',
				columns : [ [ {
					field : 'pid',
					title : '商品名稱',
					width : 300,
					formatter : function(value, row, index) {
						if (row.pi) {
							return row.pi.name;
						} else {
							return value;
						}
					}
				}, {
					field : 'price',
					title : '單價',
					width : 80
				}, {
					field : 'num',
					title : '數量',
					width : 50
				}, {
					field : 'totalprice',
					title : '總計',
					width : 100
				} ] ]
			});
		});
	</script>



</body>
</html>
