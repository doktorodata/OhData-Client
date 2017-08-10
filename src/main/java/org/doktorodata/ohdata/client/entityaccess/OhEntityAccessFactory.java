package org.doktorodata.ohdata.client.entityaccess;

import org.doktorodata.ohdata.client.base.OhClient;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.connectivity.ConnectionFactory;

public class OhEntityAccessFactory {

	private OhClient odatacaller;
	private ConnectionFactory cf;
	private String urlPath;
	private String defaultWritingDomain;

	public static OhEntityAccessFactory createwithConnection(ConnectionFactory cf, String urlPath){
		return new OhEntityAccessFactory(cf, urlPath);
	}
	
	public static OhEntityAccessFactory createWithDestination(String destname, String urlPath) throws ConnectionFactoryException{
		ConnectionFactory cf = ConnectionFactory.createFactory(destname);
		return new OhEntityAccessFactory(cf, urlPath);
	}
	
	private OhEntityAccessFactory(ConnectionFactory cf, String urlPath){
		this.cf = cf;
		this.urlPath = urlPath;
		this.odatacaller = new OhClient(cf, urlPath);
	}

	public OhClient getODataCaller() {
		return odatacaller;
	}

	public ConnectionFactory getCf() {
		return cf;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setDefaultWritingDomain(String domain) {
		this.defaultWritingDomain = domain;	
	}

	public String getDefaultWritingDomain() {
		return defaultWritingDomain;
	}
	
	
	
}
