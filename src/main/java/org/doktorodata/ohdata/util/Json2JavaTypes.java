package org.doktorodata.ohdata.util;

import java.math.BigInteger;
import java.util.GregorianCalendar;

import org.doktorodata.ohdata.client.exceptions.StubGenerationException;

public class Json2JavaTypes {

	public static Class getClassTypeForJSONType(String jsonTypeName) throws StubGenerationException{
		Class typeClz = null;
		
		if(jsonTypeName.contains("Int32")){
			typeClz = Integer.class;
		} else if(jsonTypeName.contains("Int64")){
			typeClz = BigInteger.class;
		} else if(jsonTypeName.contains("Single")){
			typeClz = Float.class;
		}  else if(jsonTypeName.contains("DateTime")){
			typeClz = GregorianCalendar.class;
		}  else if(jsonTypeName.contains("Decimal")){
			typeClz = Double.class;
		}  else if(jsonTypeName.contains("Int16")){
			typeClz = Integer.class;
		}  else if(jsonTypeName.contains("Binary")){
			typeClz = byte[].class;
		}  else if(jsonTypeName.contains("String")){
			typeClz = String.class;
		}  else if(jsonTypeName.contains("Boolean")){
			typeClz = Boolean.class;
		} else {
			throw new StubGenerationException("EDM Datatype currently not supported, needs to be added, create issue for type " + jsonTypeName);
		}
		
		return typeClz;
	}
}
