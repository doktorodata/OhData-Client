
package org.doktorodata.client.test.northwind.gen.entities;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import javax.annotation.Generated;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntityTools;

@Generated("DoktorOData - OhData-Client")
public class Orders
    extends BaseEntity
{

    public static String _ENTITY_NAME = "Orders";
    private Integer OrderID;
    private String CustomerID;
    private Integer EmployeeID;
    private GregorianCalendar OrderDate;
    private GregorianCalendar RequiredDate;
    private GregorianCalendar ShippedDate;
    private Integer ShipVia;
    private BigDecimal Freight;
    private String ShipName;
    private String ShipAddress;
    private String ShipCity;
    private String ShipRegion;
    private String ShipPostalCode;
    private String ShipCountry;

    public String getEntityName() {
        return _ENTITY_NAME;
    }

    public Integer getOrderID() {
        return OrderID;
    }

    public void setOrderID(Integer _OrderID) {
        OrderID = _OrderID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String _CustomerID) {
        CustomerID = _CustomerID;
    }

    public Integer getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(Integer _EmployeeID) {
        EmployeeID = _EmployeeID;
    }

    public GregorianCalendar getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(GregorianCalendar _OrderDate) {
        OrderDate = _OrderDate;
    }

    public GregorianCalendar getRequiredDate() {
        return RequiredDate;
    }

    public void setRequiredDate(GregorianCalendar _RequiredDate) {
        RequiredDate = _RequiredDate;
    }

    public GregorianCalendar getShippedDate() {
        return ShippedDate;
    }

    public void setShippedDate(GregorianCalendar _ShippedDate) {
        ShippedDate = _ShippedDate;
    }

    public Integer getShipVia() {
        return ShipVia;
    }

    public void setShipVia(Integer _ShipVia) {
        ShipVia = _ShipVia;
    }

    public BigDecimal getFreight() {
        return Freight;
    }

    public void setFreight(BigDecimal _Freight) {
        Freight = _Freight;
    }

    public String getShipName() {
        return ShipName;
    }

    public void setShipName(String _ShipName) {
        ShipName = _ShipName;
    }

    public String getShipAddress() {
        return ShipAddress;
    }

    public void setShipAddress(String _ShipAddress) {
        ShipAddress = _ShipAddress;
    }

    public String getShipCity() {
        return ShipCity;
    }

    public void setShipCity(String _ShipCity) {
        ShipCity = _ShipCity;
    }

    public String getShipRegion() {
        return ShipRegion;
    }

    public void setShipRegion(String _ShipRegion) {
        ShipRegion = _ShipRegion;
    }

    public String getShipPostalCode() {
        return ShipPostalCode;
    }

    public void setShipPostalCode(String _ShipPostalCode) {
        ShipPostalCode = _ShipPostalCode;
    }

    public String getShipCountry() {
        return ShipCountry;
    }

    public void setShipCountry(String _ShipCountry) {
        ShipCountry = _ShipCountry;
    }

    public String getKey() {
        return BaseEntityTools.convertToString((OrderID));
    }

}
