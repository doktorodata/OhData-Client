package org.doktorodata.ohdata.client.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhDataCallException;
import org.doktorodata.ohdata.connectivity.ConnectionFactory;
import org.doktorodata.ohdata.util.StreamAndStringTools;
import org.json.JSONObject;

/**
 * 
 * LowLevel Class that call OData services generically
 *
 */
public class OhClient {

	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_ATOM_XML = "application/atom+xml";

	public static String contentTypeToBeUsed = APPLICATION_JSON;

	private static final String SEPARATOR = "/";
	private static final String HTTP_METHOD_GET = "GET";
	private static final String HTTP_METHOD_POST = "POST";
	private static final String HTTP_METHOD_PUT = "PUT";
	private static final String HTTP_METHOD_DELETE = "DELETE";

	private static final String SET_COOKIE = "Set-Cookie";
	private static final String COOKIE_VALUE_DELIMITER = ";";
	private static final String SET_COOKIE_SEPARATOR = "; ";
	private static final String COOKIE = "Cookie";
	private static final char NAME_VALUE_SEPARATOR = '=';

	private ConnectionFactory cf;
	private String urlPath;
	private Edm edm;
	private CookieManager cookieMan;

	public OhClient(ConnectionFactory cf, String urlPath) {
		this.cf = cf;
		this.urlPath = urlPath;
		this.cookieMan = new CookieManager();

	}

	public Edm readEdm() throws OhDataCallException {

		try {
			if (this.edm == null) {
				String content = executeGET(urlPath + "/$metadata", APPLICATION_XML);

				InputStream is = StreamAndStringTools.toInputStream(content, "UTF-8");

				this.edm = EntityProvider.readMetadata(is, false);
			}

			return this.edm;
		} catch (ODataException e) {
			throw new OhDataCallException(e);
		} catch (UnsupportedEncodingException e) {
			throw new OhDataCallException(e);
		}
	}

	public void setCookies(URLConnection conn) throws URISyntaxException {
		StringBuffer cookieStringBuffer = new StringBuffer();
		List<HttpCookie> cookies = cookieMan.getCookieStore().get(conn.getURL().toURI());
		for (HttpCookie cookie : cookies) {
			String cookieName = cookie.getName();
			String value = cookie.getValue();

			cookieStringBuffer.append(cookieName);
			cookieStringBuffer.append(NAME_VALUE_SEPARATOR);
			cookieStringBuffer.append(value);
			cookieStringBuffer.append(SET_COOKIE_SEPARATOR);
		}
		conn.setRequestProperty(COOKIE, cookieStringBuffer.toString());
	}

	public void getAndStoreCookies(URLConnection conn) throws URISyntaxException {
		String headerName = null;
		for (int i = 1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
			if (headerName.equalsIgnoreCase(SET_COOKIE)) {
				String tokenString = conn.getHeaderField(i);

				StringTokenizer st = new StringTokenizer(tokenString, COOKIE_VALUE_DELIMITER);

				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					if (token.contains("=")) {
						String name = token.substring(0, token.indexOf(NAME_VALUE_SEPARATOR));
						String value = token.substring(token.indexOf(NAME_VALUE_SEPARATOR) + 1, token.length());
						cookieMan.getCookieStore().add(conn.getURL().toURI(), new HttpCookie(name, value));
					}
				}

			}
		}
	}

	public OhResult readFeed(String entitySetName, OhQuery query) throws OhDataCallException {
		try {
			Edm edm = readEdm();

			EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
			String serviceUri = urlPath;
			String absolutUri = createUri(serviceUri, entitySetName, null, query);

			HttpURLConnection connection = (HttpURLConnection) cf.getConnection(absolutUri);
			connection.setRequestMethod(HTTP_METHOD_GET);
			connection.setRequestProperty("Accept", contentTypeToBeUsed);
			setCookies(connection);
			connection.connect();

//			System.out.println(absolutUri);
			
			HttpStatusCodes statusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
			if (statusCode == HttpStatusCodes.OK) {
				InputStream is = connection.getInputStream();
				getAndStoreCookies(connection);
				ODataFeed feed = EntityProvider.readFeed(contentTypeToBeUsed,
						entityContainer.getEntitySet(entitySetName), is, EntityProviderReadProperties.init().build());
				is.close();
				connection.disconnect();
				return new OhResult(true, OhResult.STATUS_READ_SUCCESS, feed);
			} else if (statusCode == HttpStatusCodes.NOT_FOUND) {
				connection.disconnect();
				return new OhResult(false, OhResult.STATUS_ERROR_NOT_FOUND);
			} else {
				connection.disconnect();
				throw handleConnectionError(connection,
						"HTTP Error Code " + connection.getResponseCode() + " " + connection.getResponseMessage());
			}
		} catch (IOException | EntityProviderException | EdmException | ConnectionFactoryException
				| URISyntaxException e) {
			throw new OhDataCallException(e);
		}

	}

	public OhResult readEntry(String entitySetName, String keyValue) throws OhDataCallException {
		return readEntry(entitySetName, keyValue, null);
	}

	public OhResult readEntry(String entitySetName, String keyValue, OhQuery query)
			throws OhDataCallException {
		try {
			Edm edm = readEdm();
			String serviceUri = urlPath;
			EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
			String absolutUri = createUri(serviceUri, entitySetName, keyValue, query);

			HttpURLConnection connection = (HttpURLConnection) cf.getConnection(absolutUri);
			connection.setRequestMethod(HTTP_METHOD_GET);
			connection.setRequestProperty("Accept", contentTypeToBeUsed);
			setCookies(connection);
			connection.connect();

			HttpStatusCodes statusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
			if (statusCode == HttpStatusCodes.OK) {
				getAndStoreCookies(connection);
				InputStream is = connection.getInputStream();
				ODataEntry entry = EntityProvider.readEntry(contentTypeToBeUsed,
						entityContainer.getEntitySet(entitySetName), is, EntityProviderReadProperties.init().build());
				is.close();
				connection.disconnect();
				return new OhResult(true, OhResult.STATUS_READ_SUCCESS, entry);
			} else if (statusCode == HttpStatusCodes.NOT_FOUND) {
				connection.disconnect();
				return new OhResult(false, OhResult.STATUS_ERROR_NOT_FOUND);
			} else {
				connection.disconnect();
				throw handleConnectionError(connection,
						"HTTP Error Code " + connection.getResponseCode() + " " + connection.getResponseMessage());
			}

		} catch (IOException | EntityProviderException | EdmException | ConnectionFactoryException
				| URISyntaxException e) {
			throw new OhDataCallException(e);
		}
	}

	public OhResult createEntry(String entitySetName, JSONObject json) throws OhDataCallException {

		try {
			Edm edm = readEdm();
			String serviceUri = urlPath;
			String absolutUri = createUri(serviceUri, entitySetName, null);
			String contentType = APPLICATION_JSON;

			HttpURLConnection connection = (HttpURLConnection) cf.getConnection(absolutUri);
			connection.setRequestMethod(HTTP_METHOD_POST);

			String csrfToken = getCSRFToken(absolutUri);
			if (csrfToken != null) {
				connection.addRequestProperty("x-csrf-token", csrfToken);
			}

			connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
			connection.setRequestProperty("Accept", contentType);
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("DataServiceVersion", "2.0");
			connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			setCookies(connection);

			EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
			EdmEntitySet entitySet = entityContainer.getEntitySet(entitySetName);

			connection.setDoOutput(true);
			connection.setDoInput(true);

			connection.setRequestProperty("Content-Length", new Integer(json.toString().getBytes().length).toString());
			OutputStream os = connection.getOutputStream();

			StreamAndStringTools.write(json.toString(), os, "UTF-8");
			os.flush();

			connection.connect();

			// if a entity is created (via POST request) the response body
			// contains
			ODataEntry entry = null;
			HttpStatusCodes statusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());

			if (statusCode == HttpStatusCodes.CREATED) {
				// get the content as InputStream and de-serialize it into an
				// ODataEntry object
				getAndStoreCookies(connection);
				InputStream content = connection.getInputStream();
				String contentStr = StreamAndStringTools.toString(content, "UTF-8");
				InputStream content2 = StreamAndStringTools.toInputStream(contentStr, "UTF-8");
				entry = EntityProvider.readEntry(contentType, entitySet, content2,
						EntityProviderReadProperties.init().build());
				return new OhResult(true, OhResult.STATUS_CREATE_SUCCESS, entry);
			} else {
				String errorText = getErrornousConnectionMessage(connection);
				if(errorText.contains("Cannot insert. Instance already exists")){
					return new OhResult(false, OhResult.STATUS_CREATE_ERROR_ALREADY_EXISTING); 
				} else {
					throw new OhDataCallException("response code is " + statusCode + " " + connection.getResponseCode() + " "
							+ connection.getResponseMessage() + " with error details " + errorText + " with payload " + json.toString() + " to URL " + absolutUri);
				}
			}

		} catch (IOException | EntityProviderException | EdmException | ConnectionFactoryException
				| URISyntaxException e) {
			throw new OhDataCallException(e);
		}
	}

	private String getErrornousConnectionMessage(HttpURLConnection connection) throws IOException {
		String errorText = "";
		if (connection != null) {
			InputStream errorStream = connection.getErrorStream();
			if (errorStream != null) {
				errorText = StreamAndStringTools.toString(errorStream, "UTF-8");
			} else {
				InputStream inputStream = connection.getInputStream();
				if (inputStream != null) {
					errorText = StreamAndStringTools.toString(inputStream, "UTF-8");
				}
			}
		}
		return errorText;
	}

	public OhResult updateEntry(String entitySetName, String id, JSONObject json) throws OhDataCallException {

		try {

			String serviceUri = urlPath;
			String absolutUri = createUri(serviceUri, entitySetName, id);
			String contentType = APPLICATION_JSON;
			Edm edm = readEdm();

			HttpURLConnection connection = (HttpURLConnection) cf.getConnection(absolutUri);
			connection.setRequestMethod(HTTP_METHOD_PUT);

			String csrfToken = getCSRFToken(absolutUri);
			if (csrfToken != null) {
				connection.addRequestProperty("x-csrf-token", csrfToken);
			}

			connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
			connection.setRequestProperty("Accept", contentType);
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("DataServiceVersion", "2.0");
			connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			setCookies(connection);

			EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
			EdmEntitySet entitySet = entityContainer.getEntitySet(entitySetName);

			connection.setDoOutput(true);
			connection.setDoInput(true);

			connection.setRequestProperty("Content-Length", new Integer(json.toString().getBytes().length).toString());
			OutputStream os = connection.getOutputStream();

			StreamAndStringTools.write(json.toString(), os, "UTF-8");
			os.flush();

			connection.connect();

			// if a entity is created (via POST request) the response body
			ODataEntry entry = null;
			HttpStatusCodes statusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());

			if (statusCode == HttpStatusCodes.NO_CONTENT) {
				// get the content as InputStream and de-serialize it into an ODataEntry object
				getAndStoreCookies(connection);
				InputStream content = StreamAndStringTools.toInputStream(json.toString(), "UTF-8");
				entry = EntityProvider.readEntry(contentType, entitySet, content ,
						EntityProviderReadProperties.init().build());

				connection.disconnect();
				return new OhResult(true, OhResult.STATUS_UPDATE_SUCCESS, entry);
			} else if (statusCode == HttpStatusCodes.NOT_FOUND) {
				connection.disconnect();
				return new OhResult(false, OhResult.STATUS_UPDATE_NOT_EXISTING);
			} else {
				throw handleConnectionError(connection, "response code is " + statusCode);
			}

		} catch (IOException | EdmException | ConnectionFactoryException
				| URISyntaxException | EntityProviderException e) {
			throw new OhDataCallException(e);
		}
	}

	public OhResult deleteEntry(String entityName, String id) throws OhDataCallException {
		try {
			String serviceUri = urlPath;
			String absolutUri = createUri(serviceUri, entityName, id);
			String csrfUri = createUri(serviceUri, entityName, null);

			String csrfToken = getCSRFToken(csrfUri);

			HttpURLConnection connection = (HttpURLConnection) cf.getConnection(absolutUri);

			if (csrfToken != null) {
				connection.addRequestProperty("x-csrf-token", csrfToken);
			}

			connection.setRequestMethod(HTTP_METHOD_DELETE);
			connection.setRequestProperty("Accept", APPLICATION_XML);
			setCookies(connection);
			connection.connect();

			HttpStatusCodes statusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());

			if (statusCode == HttpStatusCodes.NO_CONTENT) {
				getAndStoreCookies(connection);
				connection.disconnect();
				return new OhResult(true, OhResult.STATUS_DELETE_SUCCESS);
			} else if (statusCode == HttpStatusCodes.NOT_FOUND) {
				return new OhResult(false, OhResult.STATUS_ERROR_NOT_FOUND);
			} else {
				throw handleConnectionError(connection, "response code is " + statusCode);
			}

		} catch (IOException | ConnectionFactoryException | URISyntaxException e) {
			throw new OhDataCallException(e);
		}
	}

	/*
	 * Private methods
	 */

	private OhDataCallException handleConnectionError(HttpURLConnection connection, String outsideErrorText)
			throws IOException {
		String errorText = getErrornousConnectionMessage(connection);
		
		return new OhDataCallException(outsideErrorText + " " + connection.getResponseCode() + " "
				+ connection.getResponseMessage() + " with error details " + errorText);
	}

	private String createUri(String serviceUri, String entitySetName, String id) throws UnsupportedEncodingException {
		return createUri(serviceUri, entitySetName, id, null);
	}

	private String createUri(String serviceUri, String entitySetName, String id, OhQuery query) throws UnsupportedEncodingException {

		
		final StringBuilder absolutUri = new StringBuilder(serviceUri).append(SEPARATOR).append(entitySetName);
		if (id != null) {
			id = URLEncoder.encode(id, "UTF-8");
			absolutUri.append("('").append(id).append("')");
		}
		
		if(query != null){
			String paramString = query.generateParameterString();
			absolutUri.append("?").append(paramString);
		}

		
		
		return absolutUri.toString();
	}

	private String getCSRFToken(String absolutUri)
			throws IOException, ConnectionFactoryException, OhDataCallException, URISyntaxException {

		String url = absolutUri;

		HttpURLConnection c = (HttpURLConnection) cf.getConnection(url);
		c.setRequestProperty("x-csrf-Token", "Fetch");
		c.setRequestProperty("Accept", APPLICATION_JSON);
		c.setRequestMethod("GET");
		setCookies(c);
		c.connect();

		if (c.getResponseCode() == 200) {
			String csrf = c.getHeaderField("x-csrf-token");
			getAndStoreCookies(c);
			return csrf;
		} else {
			throw handleConnectionError(c, "error calling csrf token" + url);
		}

	}

	private String executeGET(String urlPath, String contentType) throws OhDataCallException {
		try {

			HttpURLConnection conn = (HttpURLConnection) cf.getConnection(urlPath);
			conn.setRequestProperty("Accept", contentType);
			conn.connect();

			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				String response = StreamAndStringTools.toString(is, "UTF-8");
				is.close();
				conn.disconnect();
				return response;
			} else {
				conn.disconnect();
				throw handleConnectionError(conn,
						"HTTP Error Code " + conn.getResponseCode() + " " + conn.getResponseMessage());
			}

		} catch (MalformedURLException e) {
			throw new OhDataCallException(e);
		} catch (IOException e) {
			throw new OhDataCallException(e);
		} catch (ConnectionFactoryException e) {
			throw new OhDataCallException(e);
		}

	}

}
