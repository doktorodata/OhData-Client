package org.doktorodata.ohdata.client.entityaccess;

import java.util.List;

import org.doktorodata.ohdata.client.base.OhResult;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;

public class OhEntityResult<T  extends BaseEntity> {

	private List<T> feed;
	private T entry;
	private int statusCode = 0;
	private String error;
	private boolean success;
	
	
	
	public OhEntityResult(OhResult result) {
		this.statusCode = result.getStatusCode();
		this.error = result.getError();
		this.success = result.isSuccess();
	}
	
	public List<T> getFeed() {
		return feed;
	}
	public T getEntry() {
		return entry;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public String getError() {
		return error;
	}
	public boolean isSuccess() {
		return success;
	}
	protected void setFeed(List<T> feed) {
		this.feed = feed;
	}
	protected void setEntry(T entry) {
		this.entry = entry;
	}
	protected void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	protected void setError(String error) {
		this.error = error;
	}
	protected void setSuccess(boolean success) {
		this.success = success;
	}
	
	
	
}
