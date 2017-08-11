package org.doktorodata.ohdata.client.tools.apigeneration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Generated;

import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.doktorodata.ohdata.client.base.OhClient;
import org.doktorodata.ohdata.client.base.OhQuery;
import org.doktorodata.ohdata.client.entityaccess.OhEntityAccess;
import org.doktorodata.ohdata.client.entityaccess.OhEntityAccessFactory;
import org.doktorodata.ohdata.client.entityaccess.OhEntityIterator;
import org.doktorodata.ohdata.client.entityaccess.OhEntityResult;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntityTools;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhDataCallException;
import org.doktorodata.ohdata.client.exceptions.OhEntityAccessException;
import org.doktorodata.ohdata.client.exceptions.OhFeatureNotYetSupported;
import org.doktorodata.ohdata.client.exceptions.StubGenerationException;
import org.doktorodata.ohdata.client.tools.stubgeneration.EntityStubGenerator;
import org.doktorodata.ohdata.connectivity.ConnectionFactory;
import org.doktorodata.ohdata.util.StreamAndStringTools;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class APIGenerator {

	public static final String SUB_PACKAGE = "api";
	private String localFolder;
	private String basePackage;
	private String destination;
	private HashMap<String, JVar> entityEAMap;
	private JClass betools;
	
	public APIGenerator(String basePackage, String destination, String localFolder) {
		this.basePackage = basePackage;
		this.localFolder = localFolder;
		this.destination = destination;
	}
	
	public void generateAPIClasses(String apiDefinitionFile) throws ConnectionFactoryException, OhDataCallException, EdmException, IOException, JClassAlreadyExistsException, StubGenerationException, ClassNotFoundException, OhEntityAccessException, OhFeatureNotYetSupported{
		generateAPIClasses(apiDefinitionFile, "");
	}
	
	public void generateAPIClasses(String apiDefinitionFile, String path) throws ConnectionFactoryException, OhDataCallException, EdmException, IOException, JClassAlreadyExistsException, StubGenerationException, ClassNotFoundException, OhEntityAccessException, OhFeatureNotYetSupported{
		
		//Prepare
		String fullPackage = basePackage + "." +  SUB_PACKAGE;	
		entityEAMap = new HashMap<String, JVar>();
		
		//Contact OData Service
		ConnectionFactory cf = ConnectionFactory.createFactory(destination);
		OhClient vda = new OhClient(cf, path);
		Edm edm = vda.readEdm();
		
		//Initialize Code Generator
		JCodeModel cm = new JCodeModel();
		this.betools = cm.ref(BaseEntityTools.class);
				
		//Load file with API definition
		FileInputStream fis = new FileInputStream(apiDefinitionFile);
		String content = StreamAndStringTools.toString(fis, "UTF-8");
		fis.close();
		JSONObject apiDefs = new JSONObject(content);
		
		//Start iterating the API
		Iterator<String> apiIterator = apiDefs.keys();
		while (apiIterator.hasNext()) {
			String key = (String) apiIterator.next();
			JSONObject apiDef = apiDefs.getJSONObject(key);
			String entity = apiDef.getString("entity");
			
			//Where to write the files
			String fullPath = localFolder + File.separator +  fullPackage.replaceAll("\\.", "\\" + File.separator) + File.separator;
			new File(fullPath).mkdirs();
			
			//Create Class
			JDefinedClass clz = cm._class(fullPackage + "." + key);
			clz.annotate(Generated.class).param("value", "DoktorOData - OhData-Client");
	
			//Generate constructor
			JMethod constr = clz.constructor(JMod.PUBLIC);
			constr._throws(ConnectionFactoryException.class);
			JVar dest = constr.param(String.class, "destination");
			JBlock constrBody = constr.body();
			
			//Determine all entities used in configuration
			entityEAMap.put(entity, null);
			JSONArray funcs = apiDef.getJSONArray("functions");
			for(int i = 0; i < funcs.length(); i++){
				if(funcs.getJSONObject(i).has("entity"))
					entityEAMap.put(funcs.getJSONObject(i).getString("entity"), null);
			}
				
			//Create entity access factories for all entities in this api
			Iterator<String> allEntityIt = entityEAMap.keySet().iterator();
			while (allEntityIt.hasNext()) {
				String entityInAPI = (String) allEntityIt.next();
				JClass ohEAFClz = cm.ref(OhEntityAccessFactory.class);
				JVar entityEAF = clz.field(JMod.PRIVATE, ohEAFClz, entityInAPI.toLowerCase() + "EAF");
				constrBody.assign(entityEAF, ohEAFClz.staticInvoke("createWithDestination").arg(dest).arg(path));
				
				String fullClassName = basePackage + "." + EntityStubGenerator.SUB_PACKAGE + "." + firstUpper(entityInAPI);
				JDefinedClass ohEntityClz = cm.anonymousClass(Class.forName(fullClassName)); 
				JClass ohEAClz = cm.ref(OhEntityAccess.class).narrow(ohEntityClz);
				JVar entityEA = clz.field(JMod.PRIVATE, ohEAClz, entityInAPI.toLowerCase() + "EA");
				entityEAMap.put(entityInAPI, entityEA);
				
				constrBody.assign(entityEA, JExpr._new(ohEAClz).arg(entityEAF).arg(ohEntityClz.staticRef("class")));
			}
			
			//Generate functions in sub method
			for(int i = 0; i < funcs.length(); i++){
				JSONObject func = funcs.getJSONObject(i);
				generateFunction(func, clz, cm, edm, entity);
			}
			
		}
		
		//Write the generated files into this folder
		File folder = new File(this.localFolder);
		folder.mkdirs();
		cm.build(folder);
	}

	@SuppressWarnings("rawtypes")
	private void generateFunction(JSONObject functionDef, JDefinedClass clz, JCodeModel cm, Edm edm, String entity) throws EdmException, StubGenerationException, ClassNotFoundException, OhEntityAccessException, OhFeatureNotYetSupported {
		String name = functionDef.getString("name");
		String type = functionDef.getString("type");
		
		//Is this method for another entity
		if(functionDef.has("entity"))
			entity = functionDef.getString("entity");
		
		//Get Entity definition
		EdmEntitySet es = edm.getDefaultEntityContainer().getEntitySet(entity);
		
		//Create the method
		JMethod method = clz.method(JMod.PUBLIC, Void.class, name);;				
		
		//Create Return Type
		String fullClassName = basePackage + "." + EntityStubGenerator.SUB_PACKAGE + "." + firstUpper(entity);
		JDefinedClass ohEntityClz = cm.anonymousClass(Class.forName(fullClassName)); 
		method.type(cm.ref(OhEntityResult.class).narrow(ohEntityClz));
		
		//Fill the first parts into the method (OhQuery)
		method._throws(OhEntityAccessException.class);
		JBlock methBody = method.body();
		
		//Shortcut type getbyid --> Takes the key
		if(type.equals("getbyid")){
						
			//Loop all available key properties
			List<EdmProperty> keys = es.getEntityType().getKeyProperties();
			
			if(keys.size() == 1){
				EdmProperty key = keys.get(0);
				String keyName = key.getName();
				
				Class typeClz = BaseEntityTools.getClassTypeForJSONType(key.getType().getName());
				JVar keyVar = method.param(typeClz, keyName);
				
				//Return key
				JVar entityEA = entityEAMap.get(entity);
				methBody._return(JExpr.invoke(entityEA,"get").arg(keyVar));	
				
			} else {
				JVar keyString = methBody.decl(cm.ref(String.class), "_key", JExpr.lit(""));
				int cnt = 0;
				
				for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
					EdmProperty key = (EdmProperty) iterator.next();
					String keyName = key.getName();

					Class typeClz = BaseEntityTools.getClassTypeForJSONType(key.getType().getName());
					JVar keyVar = method.param(typeClz, keyName);
					
					if (keys.size() > 1) {
						if(cnt++ == 0){
							methBody.assign(keyString, JExpr.lit(keyName).plus(JExpr.lit("=")).plus(betools.staticInvoke("convertToString").arg(keyVar)));
						} else {
							methBody.assign(keyString, keyString.plus(JExpr.lit(",").plus(JExpr.lit(keyName).plus(JExpr.lit("=")).plus(betools.staticInvoke("convertToString").arg(keyVar)))));
						}
					} else {
						throw new OhEntityAccessException("Entity has no keys. Cannot create getbyid method");
					}
				}
				
				//Return key
				JVar entityEA = entityEAMap.get(entity);
				methBody._return(JExpr.invoke(entityEA,"get").arg(keyString));	
			}
			
			
		
		
		//All query types
		} else if(type.equals("query")){
			
			//Create query object
			JClass ohQueryClz = cm.ref(OhQuery.class);
			JVar ohQuery = methBody.decl(ohQueryClz, "query");
			ohQuery.init(JExpr._new(ohQueryClz));
			
			//Generate Parameters and Filters
			JSONArray filters = functionDef.getJSONArray("filters");
			for(int i = 0; i < filters.length(); i++){
				JSONObject filter = filters.getJSONObject(i);
				
				//Call submethod to generate the filter parameter
				generateFilterParameter(filter, es, entity, method, ohQuery, cm, i);	
			}
			
			//define return type iterator or standard
			if(functionDef.has("return") && functionDef.getString("return").equals("iterator")){
				//Change return type to iterator
				JClass entityIteratorClz = cm.ref(OhEntityIterator.class).narrow(ohEntityClz);
				method.type(entityIteratorClz);
				JVar entityEA = entityEAMap.get(entity);
				methBody._return(JExpr._new(entityIteratorClz).arg(entityEA).arg(ohQuery));
			} else {
				JVar entityEA = entityEAMap.get(entity);
				methBody._return(JExpr.invoke(entityEA,"query").arg(ohQuery));	
			}
		
		} else {
			throw new OhFeatureNotYetSupported("This type " + type + " is not yet supported");
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	private void generateFilterParameter(JSONObject filter, EdmEntitySet es, String entity, JMethod method, JVar ohQuery, JCodeModel cm, int i) throws EdmException, StubGenerationException {
		String propertyName = filter.getString("property");
		String paramName = propertyName;
		
		if(filter.has("name"))
			paramName = filter.getString("name");
		
		String operator = filter.getString("operator");
		boolean isOr = false;
		
		if(filter.has("or") && filter.getBoolean("or"))
			isOr = true;
				
		//And or Or
		if(i > 0){
			if(isOr)
				method.body().invoke(ohQuery, "or");
			else 
				method.body().invoke(ohQuery, "and");
		}
		
		EdmTyped property = es.getEntityType().getProperty(propertyName);
		Class clzType = BaseEntityTools.getClassTypeForJSONType(property.getType().getName());
		
		//Add the parameter and add statement to body to assign to query filter 
		JVar param = method.param(clzType, paramName);
		method.body().invoke(ohQuery, "filter").arg(propertyName).arg(operator).arg(param);
	
	}

	private static String firstUpper(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
}
