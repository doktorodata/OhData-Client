package org.doktorodata.ohdata.connectivity;

import java.net.URLConnection;

import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ConnectionFactory { 

	private final static Logger logger = LoggerFactory.getLogger(DirectHTTPConnectionFactory.class);
	
	protected ConnectionFactory(){
		
	}
	
	public static ConnectionFactory createFactory(String destname) throws ConnectionFactoryException{
		
		//TODO: Keep instance per destname and don't create a new one.
		
		try {
				return new DirectHTTPConnectionFactory(destname); 
		} catch(Exception e){
			logger.error(e.toString());
			throw new ConnectionFactoryException(e);
		}
	}
	
	public abstract URLConnection getConnection(String path) throws ConnectionFactoryException;


	
	
}
