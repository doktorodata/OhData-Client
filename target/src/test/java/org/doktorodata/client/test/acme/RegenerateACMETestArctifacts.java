package org.doktorodata.client.test.acme;

import java.io.IOException;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhDataCallException;
import org.doktorodata.ohdata.client.exceptions.OhEntityAccessException;
import org.doktorodata.ohdata.client.exceptions.OhFeatureNotYetSupported;
import org.doktorodata.ohdata.client.exceptions.StubGenerationException;
import org.doktorodata.ohdata.client.tools.stubgeneration.EntityStubGenerator;

import com.sun.codemodel.JClassAlreadyExistsException;

public class RegenerateACMETestArctifacts {

	private static final String basePackage = "org.doktorodata.client.test.acme.gen";
	private static final String basePath = "./src/test/java";
	private static final String destination = "src/test/resources/ACME.destination";
	private static final String[] entitiesToCheck = {"Products", "Suppliers", "Categories"};
	
	public static void main(String[] args) throws EdmException, StubGenerationException, ConnectionFactoryException, OhDataCallException, IOException, JClassAlreadyExistsException, ClassNotFoundException, OhEntityAccessException, OhFeatureNotYetSupported {
		
		EntityStubGenerator pocoGen = new EntityStubGenerator(basePackage, destination, basePath);
		pocoGen.setEntitiesToGenerate(entitiesToCheck);
		pocoGen.generateEntityStubs();
		
	}
}
