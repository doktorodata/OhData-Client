package org.doktorodata.ohdata.client.entityaccess;

import java.util.Iterator;
import java.util.LinkedList;

import org.doktorodata.ohdata.client.base.OhQuery;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;

public class OhEntityIterator<T extends BaseEntity> implements Iterator<T>, OhEntityCallback<T>{

	private OhEntityAccess<T> oda;	
	private LinkedList<T> itemsLoaded = new LinkedList<T>();
	private int skip = 0;
	private OhQuery query;
	private boolean currentlyLoading = false;
	private boolean errorState = false;
	private boolean hasBeenStarted = false;
	private Exception errorStateException;
	private String errorStateText;	
	
	public static int LOAD_SIZE = 10;
	public static int CONTINUE_LOADING_LIMIT = 2;
	
	public OhEntityIterator(OhEntityAccess<T> oda, OhQuery query){
		this.oda = oda;
		this.query = query;
		if(this.query == null){
			this.query = new OhQuery();
		}
	}	

	public void startLoading(){	
		hasBeenStarted = true;
		query.top(LOAD_SIZE);
		query.skip(0);
		loadNextSet();
	}
	
	public boolean hasError(){
		return errorState ;
	}
	
	private void loadNextSet(){
		if(errorState)
			return;
		
		query.skip(skip);
		skip = skip + LOAD_SIZE;

		synchronized (itemsLoaded) {			
			currentlyLoading = true;
			oda.queryAsync(query, this);
		}
	}
	
	@Override
	public void callSuccess(OhEntityResult<T> result) {
		synchronized (itemsLoaded) {
			itemsLoaded.addAll(result.getFeed());
			currentlyLoading = false;
			itemsLoaded.notifyAll();
		}		
	}

	@Override
	public void callError(int statusCode, String errorText, Exception e) {
		synchronized (itemsLoaded) {
			errorState = true;
			errorStateText = errorText;
			errorStateException = e;
			currentlyLoading = false;
			itemsLoaded.notifyAll();
		}				
	}
	
	
	
	public boolean isErrorState() {
		return errorState;
	}

	public Exception getErrorStateException() {
		return errorStateException;
	}

	public String getErrorStateText() {
		return errorStateText;
	}

	public int getCount(){
		return itemsLoaded.size();		
	}
	
	@Override
	public boolean hasNext() {
		synchronized (itemsLoaded) {
			
			if(!hasBeenStarted ){
				startLoading();
			}
			
			if(itemsLoaded.size() > 0){
				return true;
			} else {
				while(currentlyLoading){
					try {
						itemsLoaded.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if(itemsLoaded.size() > 0){
					return true;
				}else {
					return false;
				}
			}
		}
		
	}

	@Override
	public T next() {
		synchronized (itemsLoaded) {
			if(itemsLoaded.size() < CONTINUE_LOADING_LIMIT && currentlyLoading == false){
				loadNextSet();
			}
			return itemsLoaded.poll();
			
		
		}
		
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	
}
