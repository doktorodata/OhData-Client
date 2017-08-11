
package test.apigen.entities;

import javax.annotation.Generated;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntityTools;

@Generated("DoktorOData - OhData-Client")
public class Customers
    extends BaseEntity
{

    public static String _ENTITY_NAME = "Customers";
    private String CustomerID;
    private String CompanyName;
    private String ContactName;
    private String ContactTitle;
    private String Address;
    private String City;
    private String Region;
    private String PostalCode;
    private String Country;
    private String Phone;
    private String Fax;

    public String getEntityName() {
        return _ENTITY_NAME;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String _CustomerID) {
        CustomerID = _CustomerID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String _CompanyName) {
        CompanyName = _CompanyName;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String _ContactName) {
        ContactName = _ContactName;
    }

    public String getContactTitle() {
        return ContactTitle;
    }

    public void setContactTitle(String _ContactTitle) {
        ContactTitle = _ContactTitle;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String _Address) {
        Address = _Address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String _City) {
        City = _City;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String _Region) {
        Region = _Region;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String _PostalCode) {
        PostalCode = _PostalCode;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String _Country) {
        Country = _Country;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String _Phone) {
        Phone = _Phone;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String _Fax) {
        Fax = _Fax;
    }

    public String getKey() {
        return BaseEntityTools.convertToString((CustomerID));
    }

}
