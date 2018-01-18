package org.doktorodata.ohdata.client.exceptions;

public class OhDataCallException extends Exception {
 
	private static final long serialVersionUID = -7468275375067133793L;

	public OhDataCallException(String error) {
		super(error);
	}

	public OhDataCallException(Exception e) {
		super(e);
	}

}
