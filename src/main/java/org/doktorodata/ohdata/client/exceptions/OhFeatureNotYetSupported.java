package org.doktorodata.ohdata.client.exceptions;

public class OhFeatureNotYetSupported extends Exception {

	private static final long serialVersionUID = -4495175476577379409L;

	public OhFeatureNotYetSupported(String feature) {
		super(feature);
	}
}
