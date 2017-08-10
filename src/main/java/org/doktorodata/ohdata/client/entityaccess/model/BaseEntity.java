package org.doktorodata.ohdata.client.entityaccess.model;

public abstract class BaseEntity {

	public abstract String getContext();	
	public abstract String getEntityName();
	public abstract String getFullEntityName();
	public abstract String getKey();

	public String convertString(Object any) {
		return any.toString();
	}
	

}
