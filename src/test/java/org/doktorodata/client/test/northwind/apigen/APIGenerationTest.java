package org.doktorodata.client.test.northwind.apigen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhDataCallException;
import org.doktorodata.ohdata.client.exceptions.OhEntityAccessException;
import org.doktorodata.ohdata.client.exceptions.OhFeatureNotYetSupported;
import org.doktorodata.ohdata.client.exceptions.StubGenerationException;
import org.doktorodata.ohdata.client.tools.apigeneration.APIGenerator;
import org.doktorodata.ohdata.client.tools.stubgeneration.EntityStubGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.codemodel.JClassAlreadyExistsException;

public class APIGenerationTest {

	public static void main(String[] args) throws Exception {
	
		
		
	}
	
	private static final String basePackage = "test.apigen";
	private static final String basePath = "./src/test/java";
	private static final String destination = "src/test/resources/Northwind.destination";
	private static final String[] entitiesToGenerate = {"Products", "Customers"};
	
	@Before
	public void generate() throws EdmException, StubGenerationException, ConnectionFactoryException, OhDataCallException, IOException, JClassAlreadyExistsException, ClassNotFoundException, OhEntityAccessException, OhFeatureNotYetSupported{
		EntityStubGenerator pocoGen = new EntityStubGenerator(basePackage, destination, basePath);
		pocoGen.setEntitiesToGenerate(entitiesToGenerate);
		pocoGen.generateEntityStubs();
		
		APIGenerator apiGen = new APIGenerator(basePackage, destination, basePath);		
		apiGen.generateAPIClasses("src/test/resources/api.json");
	}
	
	@Test
	public void checkGeneratedFiles(){
		String genFolder = basePath + File.separator + basePackage.replaceAll("\\.", File.separator);
		String apiFolder = genFolder + File.separator + "api"; 
		File folder = new File(apiFolder);
		
		Assert.assertTrue("Folder " + apiFolder + "exists", folder.exists());
		
		String[] files = folder.list();
		Assert.assertEquals(1,files.length);		
		Assert.assertTrue("API for Products generated", Arrays.asList(files).contains("ProductAPI.java"));
	
	}
	
	@After
	public void cleanup() throws IOException{
		String cleanUpFolder = basePath + File.separator + basePackage.replaceAll("\\.", File.separator) + File.separator + "api";
		File folder = new File(cleanUpFolder);
		deleteFolder(folder);
		System.out.println("Cleanup successfull");
	}
	
	private void deleteFolder(File f) throws IOException {
		  if (f.isDirectory()) {
		    for (File c : f.listFiles())
		    	deleteFolder(c);
		  }
		  if (!f.delete())
		    throw new FileNotFoundException("Failed to delete file: " + f);
		}
	
}
