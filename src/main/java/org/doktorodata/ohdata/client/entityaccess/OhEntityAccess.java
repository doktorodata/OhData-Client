package org.doktorodata.ohdata.client.entityaccess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.doktorodata.ohdata.client.base.OhCallerFactory;
import org.doktorodata.ohdata.client.base.OhQuery;
import org.doktorodata.ohdata.client.base.OhResult;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntityTools;
import org.doktorodata.ohdata.client.exceptions.OhDataCallException;
import org.doktorodata.ohdata.client.exceptions.OhEntityAccessException;
import org.json.JSONObject;


/**
 * 
 * The service to access entities via a flexible CRUD interface. Uses the OData interface to access the methods
 *
 * @param <T> The Entity that should be accesses
 */
public class OhEntityAccess<T extends BaseEntity> {
	
	private static String FIELDNAME_ENTITYNAME = "_ENTITY_NAME";
	
	private Class<? extends BaseEntity> clz;
	private OhCallerFactory ocf;
	
	public OhEntityAccess(OhCallerFactory ocf, Class<? extends BaseEntity> clz){
		this.clz = clz;
		this.ocf = ocf;
	}
	
	public OhEntityResult<T> query() throws OhEntityAccessException {
		return query(null);
	}

	public OhEntityResult<T>  query(OhQuery query) throws OhEntityAccessException{
		
		try{
		
			String entityName = getEntityName();
			OhResult readResult = ocf.getODataCaller().readFeed(entityName, query);
			OhEntityResult<T> result = new OhEntityResult<T>(readResult);
						
			if(readResult.isSuccess()){
				ODataFeed feed = readResult.getFeed();				
				ArrayList<T> entries = new ArrayList<T>();
				List<ODataEntry> odEntries = feed.getEntries();
				for (ODataEntry odEntry : odEntries) {
					T newEntry = mapEntity(odEntry);
					entries.add(newEntry);			
				}
				
				result.setFeed(entries);
			}
			
			return result;		
		
		}catch(OhDataCallException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
			throw new OhEntityAccessException(e);
		}
	
		
	}
	
	public OhEntityResult<T> get(T entry) throws OhEntityAccessException {
		return get(entry.getKey());
	}
	
	public OhEntityResult<T> get(Object key) throws OhEntityAccessException {
			
		try{
			
			String entityName = getEntityName();
			OhResult readResult = ocf.getODataCaller().readEntry(entityName, key);
			OhEntityResult<T> result = new OhEntityResult<T>(readResult);
						
			if(readResult.isSuccess()){
				ODataEntry odEntry = readResult.getEntry();				
				T newEntry = mapEntity(odEntry); 
				result.setEntry(newEntry);
			}
			
			return result;		
		
		}catch(OhDataCallException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
			throw new OhEntityAccessException(e);
		}
		
	}
	

	public OhEntityResult<T> create(T entry) throws OhEntityAccessException {
		
		try {
			JSONObject jsonData = BaseEntityTools.convertToJSONObject(clz, entry);		
			
			//TODO: Inject determinartions and validations
			
			String entityName = getEntityName();
			OhResult result = ocf.getODataCaller().createEntry(entityName, jsonData);
			OhEntityResult<T> entityResult = new OhEntityResult<T>(result);
			if(result.getEntry() != null){
				entityResult.setEntry(mapEntity(result.getEntry()));
			} else if(entityResult.getStatusCode() == OhResult.STATUS_CREATE_ERROR_ALREADY_EXISTING){
				entityResult.setEntry(entry);
			}
			
			
			return entityResult;
			
		} catch(OhDataCallException | OhEntityAccessException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e){
			throw new OhEntityAccessException(e);
		} 
	}
	


	public OhEntityResult<T> update(T entry) throws OhEntityAccessException {
		
		try {
			
			JSONObject jsonData = BaseEntityTools.convertToJSONObject(clz, entry);			
			String entityName = getEntityName();
			Object key = entry.getKey();
			OhResult result = ocf.getODataCaller().updateEntry(entityName, key, jsonData);
			OhEntityResult<T> entityResult = new OhEntityResult<T>(result);
			entityResult.setEntry(mapEntity(result.getEntry()));
			return entityResult;

		} catch(OhDataCallException | OhEntityAccessException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e){
			throw new OhEntityAccessException(e);
		} 
	}
	
	public OhEntityResult<T> upsert(T entry) throws OhEntityAccessException {
		
		try {
			
			JSONObject jsonData = BaseEntityTools.convertToJSONObject(clz, entry);		
			
			//TODO: Inject determinations / validations
			
			String entityName = getEntityName();
			Object key = entry.getKey();
			
			OhResult readResult = ocf.getODataCaller().readEntry(entityName, key);
			OhResult writeResult;
			
			if(readResult.getStatusCode() == OhResult.STATUS_ERROR_NOT_FOUND){
				writeResult = ocf.getODataCaller().createEntry(entityName, jsonData);
			} else if (readResult.getStatusCode() == OhResult.STATUS_READ_SUCCESS) {
				writeResult = ocf.getODataCaller().updateEntry(entityName, key, jsonData);
			} else {
				throw new OhEntityAccessException("Unknown status code " + readResult.getStatusCode() + " - " + readResult.getError());
			}
			
			OhEntityResult<T> entityResult = new OhEntityResult<T>(writeResult);
			entityResult.setEntry(mapEntity(writeResult.getEntry()));
			return entityResult;
		
		} catch(OhDataCallException | OhEntityAccessException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e){
			throw new OhEntityAccessException(e);
		} 
	}

	public OhEntityResult<T> delete(T entry) throws OhEntityAccessException {
		 
		try {
			String entityName = getEntityName();
			Object key = entry.getKey();
			OhResult deleteResult = ocf.getODataCaller().deleteEntry(entityName, key);
			
			OhEntityResult<T> entityResult = new OhEntityResult<T>(deleteResult);
			if(deleteResult.getEntry() != null){
				entityResult.setEntry(mapEntity(deleteResult.getEntry()));
			}
			return entityResult;
			
		} catch (OhEntityAccessException | OhDataCallException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			throw new OhEntityAccessException(e);
		}
		
	}
	
	private String getEntityName() throws OhEntityAccessException{
		try {
			return (String)clz.getField(FIELDNAME_ENTITYNAME).get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new OhEntityAccessException("Error accessing class data " + e.toString());
		}
	}

	
		
	@SuppressWarnings({ "unchecked"})
	private T mapEntity(ODataEntry odEntry) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Set<String> keys = odEntry.getProperties().keySet();
		T entry = (T)clz.newInstance();		
		for (String key : keys) {
			Object value = odEntry.getProperties().get(key);
			mapValue(entry, key, value);
		}
		return entry;
	}

	@SuppressWarnings("unchecked")
	private void mapValue(T entry, String key, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Method m = BaseEntityTools.getSetterMethod(clz, key);
		if(m != null){
			m.invoke(entry, value);
		}  else {
			if(value instanceof HashMap){
				HashMap<String, Object> cplxTypeValues = (HashMap<String, Object>) value;
				Set<String> subKeys = cplxTypeValues.keySet();
				for (String subKey : subKeys) {
					mapValue(entry, key + "_" + subKey, cplxTypeValues.get(subKey));
				}
			} else {
				System.out.println("Method for " + key + " not found - " + value.getClass());	
			}
			
		}
	}

	public void queryAsync(OhQuery query, OhEntityCallback<T> callback) {	
		new FeedThread(query, callback).start();
	}
	
	private class FeedThread extends Thread{
		
		
		private OhEntityCallback<T> callback;
		private OhQuery query;

		public FeedThread(OhQuery query, OhEntityCallback<T> callback){
			this.query = query;
			this.callback = callback;
		}
		
		public void run() {
			try {
				OhEntityResult<T> result = query(query);
				if(result.isSuccess()){
					callback.callSuccess(result);
				} else {
					callback.callError(result.getStatusCode(), result.getError(), null);
				}
			} catch (OhEntityAccessException e) {
				e.printStackTrace();
				callback.callError(OhResult.STATUS_SEVERE_ERROR, e.toString(), e);
			}				
		};	
		
	}


	



	



	

	
	
	
}
