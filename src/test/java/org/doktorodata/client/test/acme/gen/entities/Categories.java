
package org.doktorodata.client.test.acme.gen.entities;

import javax.annotation.Generated;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntityTools;

@Generated("DoktorOData - OhData-Client")
public class Categories
    extends BaseEntity
{

    public static String _ENTITY_NAME = "Categories";
    private Integer ID;
    private String Name;

    public String getEntityName() {
        return _ENTITY_NAME;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer _ID) {
        ID = _ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String _Name) {
        Name = _Name;
    }

    public String getKey() {
        return BaseEntityTools.convertToString((ID));
    }

}
