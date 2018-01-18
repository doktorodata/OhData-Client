package org.doktorodata.client.test.northwind.entitygen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhDataCallException;
import org.doktorodata.ohdata.client.exceptions.StubGenerationException;
import org.doktorodata.ohdata.client.tools.stubgeneration.EntityStubGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.codemodel.JClassAlreadyExistsException;


public class EntityGenerationTest {

	private static final String basePackage = "test.entitygen";
	private static final String basePackagePath = "test/entitygen";
	private static final String basePath = "./src/test/java";
	private static final String destination = "src/test/resources/Northwind.destination";
	private static final String[] entitiesToCheck = {"Products", "Customers"};
	
	@Before
	public void generate() throws EdmException, StubGenerationException, ConnectionFactoryException, OhDataCallException, IOException, JClassAlreadyExistsException{
		EntityStubGenerator pocoGen = new EntityStubGenerator(basePackage, destination, basePath);
		pocoGen.setEntitiesToGenerate(entitiesToCheck);
		pocoGen.generateEntityStubs();
	}
	
	@Test
	public void checkGeneratedFiles(){
		String genFolder = basePath + File.separator + basePackagePath;
		String entityFolder = genFolder + File.separator + "entities"; 
		File folder = new File(entityFolder);
		
		Assert.assertTrue("Folder " + entityFolder + "exists", folder.exists());
		
		String[] files = folder.list();
		
		Assert.assertEquals(entitiesToCheck.length,files.length);
		
		for (int i = 0; i < entitiesToCheck.length; i++) {
			Assert.assertTrue("Entity Products generated", Arrays.asList(files).contains(entitiesToCheck[i] + ".java"));
		}
		
	}
	
//	@Test
//	public void checkToLoad() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
//		
////		for (int i = 0; i < entitiesToCheck.length; i++) {
////			Class<BaseEntity> clzProducts = (Class<BaseEntity>) Class.forName(basePackage + ".entities." + entitiesToCheck[i]);
////			BaseEntity be  = clzProducts.newInstance();
////		}
//		
//	}
	
	@After
	public void cleanup() throws IOException{
		String cleanUpFolder = basePath + File.separator + basePackagePath;
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
