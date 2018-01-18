
package org.doktorodata.client.test.northwind.gen.entities;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.doktorodata.ohdata.client.entityaccess.BaseEntityTools;
import org.doktorodata.ohdata.client.entityaccess.model.BaseEntity;

@Generated("DoktorOData - OhData-Client")
public class Order_Details
    extends BaseEntity
{

    public static String _ENTITY_NAME = "Order_Details";
    private Integer OrderID;
    private Integer ProductID;
    private BigDecimal UnitPrice;
    private Short Quantity;
    private Float Discount;

    public String getEntityName() {
        return _ENTITY_NAME;
    }

    public Integer getOrderID() {
        return OrderID;
    }

    public void setOrderID(Integer _OrderID) {
        OrderID = _OrderID;
    }

    public Integer getProductID() {
        return ProductID;
    }

    public void setProductID(Integer _ProductID) {
        ProductID = _ProductID;
    }

    public BigDecimal getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(BigDecimal _UnitPrice) {
        UnitPrice = _UnitPrice;
    }

    public Short getQuantity() {
        return Quantity;
    }

    public void setQuantity(Short _Quantity) {
        Quantity = _Quantity;
    }

    public Float getDiscount() {
        return Discount;
    }

    public void setDiscount(Float _Discount) {
        Discount = _Discount;
    }

    public String getKey() {
        String _key = "";
        _key = (("OrderID"+"=")+ BaseEntityTools.convertToString((OrderID)));
        _key = (_key +(","+(("ProductID"+"=")+ BaseEntityTools.convertToString((ProductID)))));
        return _key;
    }

}
