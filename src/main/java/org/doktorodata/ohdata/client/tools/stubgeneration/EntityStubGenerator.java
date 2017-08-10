package org.doktorodata.ohdata.client.tools.stubgeneration;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Generated;

import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.doktorodata.ohdata.client.base.OhClient;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhDataCallException;
import org.doktorodata.ohdata.client.exceptions.StubGenerationException;
import org.doktorodata.ohdata.connectivity.ConnectionFactory;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

/**
 * 
 * Generates the necessary stubs for a custom development IDSpace
 *
 */
public class EntityStubGenerator {

	private String localFolder;
	private String basePackage;
	private String destination;
	private String underscoreSplitLetter = null;

	public EntityStubGenerator(String basePackage, String destination) {
		this.localFolder = ".";
		this.basePackage = basePackage;
		this.destination = destination;
	}

	public EntityStubGenerator(String basePackage, String destination, String localFolder) {
		this.basePackage = basePackage;
		this.localFolder = localFolder;
		this.destination = destination;
	}

	public void generateEntityStubs() throws StubGenerationException, ConnectionFactoryException, EdmException, OhDataCallException, IOException, JClassAlreadyExistsException{
		generateEntityStubs("");
	}	
	 
	public void generateEntityStubs(String path) throws StubGenerationException, ConnectionFactoryException, EdmException, OhDataCallException, IOException, JClassAlreadyExistsException{		
	
		String backendPath = path;
		ConnectionFactory cf = ConnectionFactory.createFactory(destination);
		OhClient vda = new OhClient(cf, backendPath);
		
		Edm edm = vda.readEdm();
		List<EdmEntitySet> es = edm.getDefaultEntityContainer().getEntitySets();

		JCodeModel cm = new JCodeModel();	
		
		for (EdmEntitySet entity : es) {
			entity.getEntityType();
			
			String simpleName = entity.getName();
			String subPackage = "";
			
			if(underscoreSplitLetter != null){
				
				String[] splits = simpleName.split(underscoreSplitLetter);
				for(int i = 0; i < splits.length-1; i++){
					subPackage += splits[i] + ".";
				}
				subPackage.substring(0,subPackage.length()-1);
				simpleName = splits[splits.length-1];				
			}
			
			
			if(Character.isDigit(simpleName.charAt(0))){
				simpleName = "_" + simpleName;
			}
			
			
			String fullPackage = basePackage + "." +  subPackage.replaceAll("\\_", "\\.");	
			
			if(fullPackage.endsWith("."))
				fullPackage = fullPackage.substring(0, fullPackage.length()-1);
			
			String fullPath = localFolder + File.separator +  fullPackage.replaceAll("\\.", "\\" + File.separator) + File.separator;
			new File(fullPath).mkdirs();
			
			//Start creating the file
				String className =  firstUpper(simpleName);
				JDefinedClass clz = cm._class(fullPackage + "." + className);
				clz.annotate(Generated.class).param("value", "DoktorOData - OhData-Client");
				clz._extends(BaseEntity.class);
				
				JFieldVar fieldCtx = clz.field(JMod.STATIC, String.class, "_CONTEXT", JExpr.lit(subPackage));
				JFieldVar fieldEntity = clz.field(JMod.STATIC, String.class, "_ENTITY_NAME", JExpr.lit(entity.getName()));
				JFieldVar fieldFullEn = clz.field(JMod.STATIC, String.class, "_FULL_ENTITY_NAME", JExpr.lit(simpleName));

				clz.method(JMod.PUBLIC, String.class, "getContext").body()._return(fieldCtx);
				clz.method(JMod.PUBLIC, String.class, "getEntityName").body()._return(fieldEntity);
				clz.method(JMod.PUBLIC, String.class, "getFullEntityName").body()._return(fieldFullEn);
			
				//Keep the properties
				HashMap<String, JFieldVar> propFields = new HashMap<String, JFieldVar>();
				
				//Write the properties
				List<String> props = entity.getEntityType().getPropertyNames();
				for (String propName : props) {
					EdmTyped prop = entity.getEntityType().getProperty(propName);
					String name = prop.getName();

					@SuppressWarnings("rawtypes")
					Class typeClz = null;
					
					if(prop.getType().getName().contains("Int32")){
						
						typeClz = Integer.class;
					} else if(prop.getType().getName().contains("Int64")){
						
						typeClz = BigInteger.class;
					} else if(prop.getType().getName().contains("Single")){
							
						typeClz = Float.class;
					}  else if(prop.getType().getName().contains("DateTime")){
						
						typeClz = GregorianCalendar.class;
					}  else if(prop.getType().getName().contains("Decimal")){
						
						typeClz = Double.class;
					}  else if(prop.getType().getName().contains("Int16")){
						
						typeClz = Integer.class;
					}  else if(prop.getType().getName().contains("Binary")){
						
						typeClz = byte[].class;
					}  else if(prop.getType().getName().contains("String")){
						
						typeClz = String.class;
					}  else if(prop.getType().getName().contains("Boolean")){
						
						typeClz = Boolean.class;
					} else {
						throw new StubGenerationException("EDM Datatype currently not supported, needs to be added, create issue for type " + prop.getType());
					}
					
					JFieldVar fieldProp = clz.field(JMod.PRIVATE, typeClz, name, null);
					JMethod methGet = clz.method(JMod.PUBLIC, typeClz, "get"+ EntityStubGenerator.firstUpper(name));
					methGet.body()._return(fieldProp);
					
					JMethod methSet = clz.method(JMod.PUBLIC, Void.TYPE, "set"+ EntityStubGenerator.firstUpper(name));
					methSet.body().assign(fieldProp, methSet.param(typeClz, "_" + name));

					propFields.put(name, fieldProp);
					
				}
				
				
				//Write the key method
				JMethod methKey = clz.method(JMod.PUBLIC, String.class, "getKey");
				//JMethod convMeth = clz.getMethod("convertString", null);
				
				List<EdmProperty> keyProps = entity.getEntityType().getKeyProperties();
				if(keyProps.size() == 1){
					EdmProperty keyProp = keyProps.get(0);	
					String name = keyProp.getName();
					methKey.body()._return(JExpr.direct("convertString(" + name + ")"));
				} else if(keyProps.size() > 1) {
					StringBuilder sb = new StringBuilder();					
					for (int i = 0; i < keyProps.size(); i++) {
						EdmProperty keyProp = keyProps.get(i);
						String name = keyProp.getName();
						sb.append("\"" + name + "=\" + convertString(" + name + ")" );
						if(i < keyProps.size()-1){
							sb.append("+\",\"+");
						}
					} 
					methKey.body()._return(JExpr.direct(sb.toString()));
				} else {
					throw new StubGenerationException("Entity has no key property / currently not supported");
				}
			
			
						
			System.out.println("Writing " + entity.getName() + " to " + fullPath +  simpleName + ".java");
		}
			
		//Write the files
		File file = new File(this.localFolder);
		file.mkdirs();
		cm.build(file);
		
	}

	private static String firstUpper(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

}
