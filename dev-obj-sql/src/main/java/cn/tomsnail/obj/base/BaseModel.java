package cn.tomsnail.obj.base;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;




import cn.tomsnail.framwork.validator.annotation.FieldValidator;
import cn.tomsnail.util.string.StringUtils;

public class BaseModel {

	@FieldValidator(onlyToBean=true,mapperName="")
	public String start;
	
	@FieldValidator(onlyToBean=true,mapperName="")
	public String limit;
	
	@FieldValidator(onlyToBean=true,mapperName="")
	public int total;
	
	@FieldValidator(onlyToBean=true,mapperName="")
	public String orderBy;
	
	private Map<String,Object> resultMap = new HashMap<String,Object>();
	
	public Map<String, Object> getResultMap() {
		return resultMap;
	}
	
	public String getStart() {
		if (sqlinj(this.start)) {
			return "";
		}
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getLimit() {
		if (sqlinj(this.limit)) {
			return "";
		}
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}



	
	/**
	 * 获取查询排序字符串
	 * @return
	 */
	public String getOrderBy() {
		if (sqlinj(this.orderBy)) {
			return "";
		}
		return orderBy;
	}

	/**
	 * 设置查询排序，标准查询有效， 实例： updatedate desc, name asc
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	private static boolean sqlinj(String t){
		if(StringUtils.isBlank(t)){
			return false;
		}
		String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
				+ "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
		Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		if (sqlPattern.matcher(t).find()) {
			return true;
		}
		return false;
	}
	
}
