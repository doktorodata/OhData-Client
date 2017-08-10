package org.doktorodata.ohdata.client.entityaccess.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.GregorianCalendar;

import org.json.JSONObject;

/**
 * 
 * Tools that help to work with generic entities (BaseEntity), to get data, set data or convert them to JSONObjects
 *
 */
public class BaseEntityTools {

	
	public static JSONObject convertToJSONObject(Class<?> clz, Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JSONObject json = new JSONObject();
		Field[] fld = clz.getDeclaredFields();
		for (int i = 0; i < fld.length; i++) {
			Field field = fld[i];
			if(field.getName().startsWith("_"))
				continue;
			Method getter = getGetterMethod(clz, field.getName());
			Object value = getter.invoke(obj);
			
			if(value != null && field.getType() == GregorianCalendar.class){
				value = "/Date(" +((GregorianCalendar)value).getTime().getTime() +")/";
			}
			
			json.put(field.getName(), value);			
		}
				
		return json;
	}
	
	public static Method getSetterMethod(Class<?> clz, String name) {
		String setterName = "set" + name.substring(0,1).toUpperCase() + name.substring(1);
		Method[] meths = clz.getMethods();
		for (int i = 0; i < meths.length; i++) {
			Method method = meths[i];
			if(method.getName().equals(setterName)){
				return method;
			}
		}
		return null;
	}
	
	public static Method getGetterMethod(Class<?> clz, String name) {
		String setterName = "get" + name.substring(0,1).toUpperCase() + name.substring(1);
		Method[] meths = clz.getMethods();
		for (int i = 0; i < meths.length; i++) {
			Method method = meths[i];
			if(method.getName().equals(setterName)){
				return method;
			}
		}
		return null;
	}

	public static Object getValue(Class<? extends BaseEntity> clz, Object obj, String field) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method m = getGetterMethod(clz, field);
		return m.invoke(obj);		
	}

	public static boolean hasField(Class<? extends BaseEntity> clz, String fieldName) {
		Field[] fld = clz.getDeclaredFields();
		for (int i = 0; i < fld.length; i++) {
			Field field = fld[i];
			if(field.getName().equals(fieldName))
				return true;
		}
				
		return false;
	}
	
	public static String convertToString(Object any) {
		return "" + any;
	}
	
}
