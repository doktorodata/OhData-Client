
package org.doktorodata.client.test.acme.gen.entities;

import javax.annotation.Generated;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntityTools;

@Generated("DoktorOData - OhData-Client")
public class Suppliers
    extends BaseEntity
{

    public static String _ENTITY_NAME = "Suppliers";
    private Integer ID;
    private String Name;
    private String AddressStreet;
    private String AddressCity;
    private String AddressState;
    private String AddressZipCode;
    private String AddressCountry;
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

    public String getAddressStreet() {
        return AddressStreet;
    }

    public void setAddressStreet(String _AddressStreet) {
        AddressStreet = _AddressStreet;
    }

    public String getAddressCity() {
        return AddressCity;
    }

    public void setAddressCity(String _AddressCity) {
        AddressCity = _AddressCity;
    }

    public String getAddressState() {
        return AddressState;
    }

    public void setAddressState(String _AddressState) {
        AddressState = _AddressState;
    }

    public String getAddressZipCode() {
        return AddressZipCode;
    }

    public void setAddressZipCode(String _AddressZipCode) {
        AddressZipCode = _AddressZipCode;
    }

    public String getAddressCountry() {
        return AddressCountry;
    }

    public void setAddressCountry(String _AddressCountry) {
        AddressCountry = _AddressCountry;
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
