
package org.doktorodata.client.test.acme.gen.entities;

import javax.annotation.Generated;

import org.doktorodata.ohdata.client.entityaccess.BaseEntityTools;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;

@Generated("DoktorOData - OhData-Client")
public class Suppliers
    extends BaseEntity
{

    public static String _ENTITY_NAME = "Suppliers";
    private Integer ID;
    private String Name;
    private String Address_Street;
    private String Address_City;
    private String Address_State;
    private String Address_ZipCode;
    private String Address_Country;
    private Integer Concurrency;

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

    public String getAddress_Street() {
        return Address_Street;
    }

    public void setAddress_Street(String _Address_Street) {
        Address_Street = _Address_Street;
    }

    public String getAddress_City() {
        return Address_City;
    }

    public void setAddress_City(String _Address_City) {
        Address_City = _Address_City;
    }

    public String getAddress_State() {
        return Address_State;
    }

    public void setAddress_State(String _Address_State) {
        Address_State = _Address_State;
    }

    public String getAddress_ZipCode() {
        return Address_ZipCode;
    }

    public void setAddress_ZipCode(String _Address_ZipCode) {
        Address_ZipCode = _Address_ZipCode;
    }

    public String getAddress_Country() {
        return Address_Country;
    }

    public void setAddress_Country(String _Address_Country) {
        Address_Country = _Address_Country;
    }

    public Integer getConcurrency() {
        return Concurrency;
    }

    public void setConcurrency(Integer _Concurrency) {
        Concurrency = _Concurrency;
    }

    public String getKey() {
        return BaseEntityTools.convertToString((ID));
    }

}
