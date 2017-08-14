package org.doktorodata.ohdata.client.tools.apigeneration;

import java.io.FileInputStream;
import java.io.IOException;

import org.doktorodata.ohdata.util.StreamAndStringTools;
import org.json.JSONObject;

public class APIDefinitonLoader {

	private String apiDefinitionFile;

	public APIDefinitonLoader(String apiDefinitionFile) {
		this.apiDefinitionFile = apiDefinitionFile;
	}
	
	public JSONObject loadDefinition() throws IOException{
		
		//Read file content
		FileInputStream fis = new FileInputStream(apiDefinitionFile);
		String content = StreamAndStringTools.toString(fis, "UTF-8");
		fis.close();
		
		//Create JSON Object
		JSONObject apiDefs = new JSONObject(content);
	
		//Check content
		
		
		return apiDefs;
	}

}
