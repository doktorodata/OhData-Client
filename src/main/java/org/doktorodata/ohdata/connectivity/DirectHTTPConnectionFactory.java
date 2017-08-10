package org.doktorodata.ohdata.connectivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;

import org.apache.cxf.common.util.Base64Utility;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.util.StreamAndStringTools;
import org.json.JSONObject;

public class DirectHTTPConnectionFactory extends ConnectionFactory {


	private Properties prop;

	private String currentToken;
	private Date currentTokenLifetime;

//	private final static Logger logger = LoggerFactory.getLogger(DirectHTTPConnectionFactory.class);
	
	public DirectHTTPConnectionFactory(String destname) throws ConnectionFactoryException {
		try {
			prop = new Properties();
			File f = new File("src/main/resources/" + destname + ".destination");
			
			FileInputStream fis = new FileInputStream(f);
			prop.load(fis);
		
			fis.close();
						
			
			String proxyhost = prop.getProperty("proxyhost");
			String proxyport = prop.getProperty("proxyport");
		
			if(proxyhost != null && proxyport != null){
				System.setProperty("http.proxyHost", proxyhost);
				System.setProperty("https.proxyHost", proxyhost);
				System.setProperty("http.proxyPort", proxyport);
				System.setProperty("https.proxyPort", proxyport);
			}
			
			
		} catch (IOException e) {
			throw new ConnectionFactoryException(e);
		}

	}

	@Override
	public URLConnection getConnection(String path) throws ConnectionFactoryException {
		try{
			String baseURL = prop.getProperty("URL");
			String fullURL = baseURL + "/" + path;
			
			URL url = new URL(fullURL); 
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String authType = prop.getProperty("Authentication");
			
			if(authType.equals("BasicAuthentication")){	
				String user = prop.getProperty("User");
				String pass = prop.getProperty("Password");
				String encoded = Base64Utility.encode((user + ":" + pass).getBytes());
				conn.setRequestProperty("Authorization", "Basic " + encoded);
			} else if(authType.equals("oAuthToken")){
				String bearer = prop.getProperty("Password");
				conn.setRequestProperty("Authorization", "Bearer " + bearer);
			} else if(authType.equals("oAuth")){
				conn.setRequestProperty("Authorization", "Bearer " + retrieveValidBearerToken());
			}
			
			return conn;
		
		} catch (FileNotFoundException e) {
			throw new ConnectionFactoryException(e);
		} catch (MalformedURLException e) {
			throw new ConnectionFactoryException(e);
		} catch (IOException e) {
			throw new ConnectionFactoryException(e);
		}
	}

	

	
	private String retrieveValidBearerToken() throws ConnectionFactoryException {
		
		try{
			
			if(currentToken != null && (currentTokenLifetime == null || (new Date()).after(currentTokenLifetime)) ){
				return currentToken;
			} else {
				String oauthurl = prop.getProperty("oAuthURL");
				String clientid = prop.getProperty("User");
				String secret = prop.getProperty("Password");
				String scope = prop.getProperty("Scope");
											
				HttpURLConnection oAuthConn = (HttpURLConnection) new URL(oauthurl).openConnection();

				oAuthConn.setRequestMethod("POST");
				oAuthConn.setDoOutput(true);

				String encoded = Base64Utility.encode((clientid + ":" + secret).getBytes());
				oAuthConn.setRequestProperty("Authorization", "Basic " + encoded);
				oAuthConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

				oAuthConn.connect();
				
				OutputStream os = oAuthConn.getOutputStream();
				String bodyContent = "grant_type=client_credentials";
				if(scope!=null && !scope.equals("")){
					bodyContent += "&scope=" + scope;
				}
				
				StreamAndStringTools.write(bodyContent, os, "UTF-8");
				os.close();
					
				InputStream is = oAuthConn.getInputStream();
				String content = StreamAndStringTools.toString(is, "UTF-8");
				JSONObject json = new JSONObject(content);
				is.close();
								
				currentToken = json.getString("access_token");
				int lifetime = json.getInt("expires_in");
				
				currentTokenLifetime = new Date(new Date().getTime() + lifetime*1000 - 60000); //expire 60 seconds ealier
						
				return currentToken;
			}
			
		}catch(IOException e){
			throw new ConnectionFactoryException(e);
		}
		
	}
	

}
