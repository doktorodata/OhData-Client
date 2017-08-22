package org.doktorodata.client.test.northwind;

import java.io.IOException;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhDataCallException;
import org.doktorodata.ohdata.client.exceptions.OhEntityAccessException;
import org.doktorodata.ohdata.client.exceptions.OhFeatureNotYetSupported;
import org.doktorodata.ohdata.client.exceptions.StubGenerationException;
import org.doktorodata.ohdata.client.tools.stubgeneration.EntityStubGenerator;

import com.sun.codemodel.JClassAlreadyExistsException;

public class RegenerateTestArctifacts {

	private static final String basePackage = "org.doktorodata.client.test.gen";
	private static final String basePath = "./src/test/java";
	private static final String destination = "src/test/resources/Northwind.destination";
	private static final String[] entitiesToCheck = {"Products", "Orders", "Order_Details"};
	
	public static void main(String[] args) throws EdmException, StubGenerationException, ConnectionFactoryException, OhDataCallException, IOException, JClassAlreadyExistsException, ClassNotFoundException, OhEntityAccessException, OhFeatureNotYetSupported {
		
		EntityStubGenerator pocoGen = new EntityStubGenerator(basePackage, destination, basePath);
		pocoGen.setEntitiesToGenerate(entitiesToCheck);
		pocoGen.generateEntityStubs();
		
		
	}
}
