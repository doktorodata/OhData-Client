package org.doktorodata.ohdata.client.entityaccess.model;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

public abstract class BaseEntity {

	private ODataEntry _odataentry;
	
	public abstract String getEntityName();
	public abstract String getKey();
	public ODataEntry _getODataEntry(){
		return _odataentry;
	}
	
	public void _setOdataEntry(ODataEntry _odataentry){
		this._odataentry = _odataentry;
	}

}
