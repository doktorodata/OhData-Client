package org.doktorodata.ohdata.client.exceptions;

public class ConnectionFactoryException extends Exception {

	private static final long serialVersionUID = -206368153720659769L;

	public ConnectionFactoryException(Exception e) {
		super(e);
	}

	public ConnectionFactoryException(String error) {
		super(error);
	}

}
