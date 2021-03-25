package com.ecpbm.pojo;

public class Pager {
	private int curPage;// 待顯示頁
	private int perPageRows;// 每頁顯示
	private int rowCount; // 紀錄總數
	private int pageCount; // 總頁數

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int currentPage) {
		this.curPage = currentPage;
	}

	public int getPerPageRows() {
		return perPageRows;
	}

	public void setPerPageRows(int perPageRows) {
		this.perPageRows = perPageRows;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	// 根據rowCount和perPageRows計算總頁數
	public int getPageCount() {
		return (rowCount + perPageRows - 1) / perPageRows;
	}

	// 分頁顯示時，取得當前頁的第一條Data的索引
	public int getFirstLimitParam() {
		return (this.curPage - 1) * this.perPageRows;
	}
}
