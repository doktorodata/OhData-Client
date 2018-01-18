package org.doktorodata.ohdata.client.base;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

public class OhQuery {

	public OhQuery(){
		
	}
	
	/*
	 * Skip and Top
	 */
	private int skip = 0;
	
	public OhQuery skip(int skip){
		this.skip = skip;
		return this;
	}
	
	private int top = -1;
	
	public OhQuery top(int top){
		this.top = top;
		return this;
	}
	
	/*
	 * Orderby 
	 */
	
	private String orderbyString = "";
	
	public OhQuery orderby(String field){
		if(!orderbyString.equals("")){
			orderbyString += ",";
		}
		
		orderbyString += field;
		
		return this;
	}
	
	/*
	 * Expand
	 */
	private String expandString = "";
	
	public OhQuery expand(String association){
		if(!expandString.equals("")){
			expandString += ",";
		}
		
		expandString += association;
		
		return this;
	}
	
	/*
	 * Filters
	 */
	ArrayList<String> filters = new ArrayList<String>();
	private boolean lastFilterDelimited = true;
	
	public OhQuery filter(String field, String operator, String value){
		checkFilterDelimition();
		String filter = field + " " + operator + " '" + value + "'";
		filters.add(filter);
		return this;
	}
	
	public OhQuery filter(String field, String operator, int value){
		checkFilterDelimition();
		String filter = field + " " + operator + " " + value;
		filters.add(filter);
		return this;
	}
	
	public OhQuery filter(String field, String operator, float value){
		checkFilterDelimition();
		String filter = field + " " + operator + " " + value;
		filters.add(filter);
		return this;
	}
	
	public OhQuery filter(String field, String operator, double value){
		checkFilterDelimition();
		String filter = field + " " + operator + " " + value;
		filters.add(filter);
		return this;
	}
	
	public OhQuery filter(String field, String operator, BigDecimal value){
		checkFilterDelimition();
		String filter = field + " " + operator + " " + value;
		filters.add(filter);
		return this;
	}
	
	public OhQuery filter(String field, String operator, Short value){
		checkFilterDelimition();
		String filter = field + " " + operator + " " + value;
		filters.add(filter);
		return this;
	}
	
	public OhQuery filter(String field, String operator, Date time) {
		checkFilterDelimition();
		String timeToEDM = String.format("%1$tFT%1$tT",time);
		String filter = field + " " + operator + " datetime'" + timeToEDM + "'";
		filters.add(filter);
		return this;
	}
		
	private void checkFilterDelimition() {
		if(!lastFilterDelimited ){
			and();
		}		
		lastFilterDelimited = false;
	}

	public OhQuery openSub(){
		checkFilterDelimition();
		filters.add("(");
		return this;
	}
	
	public OhQuery closeSub(){
		checkFilterDelimition();
		filters.add(")");
		return this;
	}
	
	public OhQuery and(){
		lastFilterDelimited = true;
		filters.add(" and ");
		return this;
	}
	
	public OhQuery or() {
		lastFilterDelimited = true;
		filters.add(" or ");
		return this;
	}
	
	
	public String generateParameterString(){
		String parameterString = "";
		if(filters.size() > 0){
			String filterString = "";
			filterString += "(";
			for (String filter : filters) {
				filterString += filter;
			}
			filterString += ")";
			parameterString = addParameter(parameterString, "$filter",filterString);
		}
		
		if(!expandString.equals("")){
			parameterString = addParameter(parameterString, "$expand",expandString);		
		}
		
		if(!orderbyString.equals("")){
			parameterString = addParameter(parameterString, "$orderby", orderbyString);
		}
		
		if(top > 0){
			parameterString = addParameter(parameterString, "$top", new Integer(top).toString());
		}
		
		if(skip > 0){
			parameterString = addParameter(parameterString, "$skip", new Integer(skip).toString());
		}
		
		return parameterString;
	}

	private String addParameter(String parameterString, String field, String value) {
		try {
			parameterString += field + "=" + URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		if(!parameterString.equals(""))
			parameterString += "&";
		return parameterString;
	}

	

}
