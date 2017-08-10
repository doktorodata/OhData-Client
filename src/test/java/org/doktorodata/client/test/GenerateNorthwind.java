package org.doktorodata.client.test;

import org.doktorodata.ohdata.client.tools.stubgeneration.EntityStubGenerator;

public class GenerateNorthwind {

	public static void main(String[] args) throws Exception {
		
		/*Execution*/
		EntityStubGenerator pocoGen = new EntityStubGenerator("org.doktorodata.client.test.gen", "Northwind", "./src/test/java");		
		pocoGen.generateEntityStubs();
		
	}
	
}
