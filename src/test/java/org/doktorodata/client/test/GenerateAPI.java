package org.doktorodata.client.test;

import org.doktorodata.ohdata.client.tools.apigeneration.APIGenerator;

public class GenerateAPI {

	public static void main(String[] args) throws Exception {
	
		APIGenerator apiGen = new APIGenerator("org.doktorodata.client.test.gen", "Northwind", "./src/test/java");		
		apiGen.generateAPIClasses("api.json");
		
	}
	
}
