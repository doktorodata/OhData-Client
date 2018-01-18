package org.doktorodata.ohdata.client.entityaccess;

import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;

public interface OhEntityCallback<T extends BaseEntity> {
	
	public void callSuccess(OhEntityResult<T> result);
	
	public void callError(int statusCode, String errorText, Exception e); 
	
}
