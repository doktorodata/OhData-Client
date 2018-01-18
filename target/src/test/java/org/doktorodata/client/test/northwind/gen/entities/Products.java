
package org.doktorodata.client.test.northwind.gen.entities;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.doktorodata.ohdata.client.entityaccess.BaseEntityTools;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;

@Generated("DoktorOData - OhData-Client")
public class Products
    extends BaseEntity
{

    public static String _ENTITY_NAME = "Products";
    private Integer ProductID;
    private String ProductName;
    private Integer SupplierID;
    private Integer CategoryID;
    private String QuantityPerUnit;
    private BigDecimal UnitPrice;
    private Short UnitsInStock;
    private Short UnitsOnOrder;
    private Short ReorderLevel;
    private Boolean Discontinued;

    public String getEntityName() {
        return _ENTITY_NAME;
    }

    public Integer getProductID() {
        return ProductID;
    }

    public void setProductID(Integer _ProductID) {
        ProductID = _ProductID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String _ProductName) {
        ProductName = _ProductName;
    }

    public Integer getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(Integer _SupplierID) {
        SupplierID = _SupplierID;
    }

    public Integer getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(Integer _CategoryID) {
        CategoryID = _CategoryID;
    }

    public String getQuantityPerUnit() {
        return QuantityPerUnit;
    }

    public void setQuantityPerUnit(String _QuantityPerUnit) {
        QuantityPerUnit = _QuantityPerUnit;
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(BigDecimal _UnitPrice) {
        UnitPrice = _UnitPrice;
    }

    public Short getUnitsInStock() {
        return UnitsInStock;
    }

    public void setUnitsInStock(Short _UnitsInStock) {
        UnitsInStock = _UnitsInStock;
    }

    public Short getUnitsOnOrder() {
        return UnitsOnOrder;
    }

    public void setUnitsOnOrder(Short _UnitsOnOrder) {
        UnitsOnOrder = _UnitsOnOrder;
    }

    public Short getReorderLevel() {
        return ReorderLevel;
    }

    public void setReorderLevel(Short _ReorderLevel) {
        ReorderLevel = _ReorderLevel;
    }

    public Boolean getDiscontinued() {
        return Discontinued;
    }

    public void setDiscontinued(Boolean _Discontinued) {
        Discontinued = _Discontinued;
    }

    public String getKey() {
        return BaseEntityTools.convertToString((ProductID));
    }

}
