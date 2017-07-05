package com.k3k.unplat.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.k3k.unplat.common.Constants;

public class Page<T> {

	private int page;// 当前页
	private int rows;// pageSize
	private int pageSize;
	private int totalRows;
	private int totalPages;
	private int startIndex;
	private int endIndex;
	private List<T> results;
	private long regCount;
	private BigDecimal totalScore;

	public long getRegCount() {
		return regCount;
	}

	public void setRegCount(long regCount) {
		this.regCount = regCount;
	}

	public BigDecimal getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(BigDecimal totalScore) {
		this.totalScore = totalScore;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		setPageSize(rows);
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		// if (page < 1) {
		// page = 1;
		// } else {
		// startIndex = pageSize * (page - 1);
		// }
		// System.out.println("rows:"+rows);
		// System.out.println("pageSize:"+pageSize);
		// System.out.println("page:"+page);
		// System.out.println("startIndex : " + startIndex);
		// endIndex = startIndex + pageSize > totalRows ? totalRows : startIndex
		// + pageSize;
		this.page = page;
	}

	public int getStartIndex() {
		startIndex = pageSize * (page - 1);
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		endIndex = startIndex + pageSize > totalRows ? totalRows : startIndex
				+ pageSize;
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		totalPages = (totalRows + pageSize - 1) / pageSize;
		this.totalRows = totalRows;
		if (totalPages < page) {
			page = totalPages;
			startIndex = pageSize * (page - 1);
			endIndex = totalRows;
		}
		endIndex = startIndex + pageSize > totalRows ? totalRows : startIndex
				+ pageSize;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getPageSize() {
		if (pageSize <= 0) {
			pageSize = Integer.parseInt(UnionUtils.getValue(
					Constants.CONFIG_FILE, Constants.PAGE_SIZE));
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	// 用于jquery easyui的grid
	public Map<String, Object> toGridMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", getTotalRows());
		map.put("rows", getResults());
		return map;
	}

}
