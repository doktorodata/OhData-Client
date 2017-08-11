package org.doktorodata.ohdata.client.base;

import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.connectivity.ConnectionFactory;

public class OhCallerFactory {

	private OhCaller odatacaller;
	private ConnectionFactory cf;
	private String urlPath;
	
	public static OhCallerFactory createwithConnection(ConnectionFactory cf){
		return new OhCallerFactory(cf, "");
	}
	
	public static OhCallerFactory createwithConnection(ConnectionFactory cf, String urlPath){
		return new OhCallerFactory(cf, urlPath);
	}
	
	public static OhCallerFactory createWithDestination(String destname) throws ConnectionFactoryException{
		ConnectionFactory cf = ConnectionFactory.createFactory(destname);
		return new OhCallerFactory(cf, "");
	}
	
	public static OhCallerFactory createWithDestination(String destname, String urlPath) throws ConnectionFactoryException{
		ConnectionFactory cf = ConnectionFactory.createFactory(destname);
		return new OhCallerFactory(cf, urlPath);
	}
	
	private OhCallerFactory(ConnectionFactory cf, String urlPath){
		this.cf = cf;
		this.urlPath = urlPath;
		this.odatacaller = new OhCaller(cf, urlPath);
	}

	public OhCaller getODataCaller() {
		return odatacaller;
	}

	public ConnectionFactory getCf() {
		return cf;
	}

	public String getUrlPath() {
		return urlPath;
	}
	
}