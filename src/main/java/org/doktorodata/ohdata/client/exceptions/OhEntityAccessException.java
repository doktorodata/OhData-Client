package org.doktorodata.ohdata.client.exceptions;

public class OhEntityAccessException extends Exception {

	private static final long serialVersionUID = -4929301414027983658L;

	public OhEntityAccessException(String error) {
		super(error);
	}

	public OhEntityAccessException(Exception e) {
		super(e);
	}

}
