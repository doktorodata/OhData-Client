package org.doktorodata.ohdata.client.base;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;

public class OhResult {

	public static final int STATUS_ERROR_NOT_FOUND = 01;
	public static final int STATUS_DELETE_SUCCESS = 40;
	public static final int STATUS_READ_SUCCESS = 10;
	public static final int STATUS_CREATE_SUCCESS = 20;
	public static final int STATUS_CREATE_ERROR_ALREADY_EXISTING = 21;
	public static final int STATUS_ERROR_EXISTING = 23;
	public static final int STATUS_UPDATE_SUCCESS = 30;
	public static final int STATUS_UPDATE_NOT_EXISTING = 31;
	public static final int STATUS_SEVERE_ERROR = 99;
	
	private boolean success;
	private String error;
	private ODataEntry entry;
	private ODataFeed feed;
	private int statusCode = 0;
	

	public OhResult(boolean success, ODataEntry entry) {
		super();
		this.success = success;
		this.entry = entry;
	}
	

	public OhResult(boolean success, String error) {
		super();
		this.success = success;
		this.error = error;
	}

	public OhResult(boolean success, int statusCode) {
		super();
		this.success = success;
		this.statusCode = statusCode;
	}


	public OhResult(boolean success, int statusCode, ODataFeed feed) {
		this.success = success;
		this.statusCode = statusCode;
		this.feed = feed;
	}


	public OhResult(boolean success, int statusCode, ODataEntry entry) {
		this.success = success;
		this.statusCode = statusCode;
		this.entry = entry;
	}


	public boolean isSuccess() {
		return success;
	}
	
	public String getError() {
		return error;
	}
	
	public ODataEntry getEntry() {
		return entry;
	}


	public ODataFeed getFeed() {
		return feed;
	}


	public int getStatusCode() {
		return statusCode;
	}


	
	
}
