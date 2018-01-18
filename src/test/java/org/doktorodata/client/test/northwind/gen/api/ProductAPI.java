
package org.doktorodata.client.test.northwind.gen.api;

import java.math.BigDecimal;

import javax.annotation.Generated;

import org.doktorodata.client.test.northwind.gen.entities.Order_Details;
import org.doktorodata.client.test.northwind.gen.entities.Products;
import org.doktorodata.ohdata.client.base.OhCallerFactory;
import org.doktorodata.ohdata.client.base.OhQuery;
import org.doktorodata.ohdata.client.entityaccess.OhEntityAccess;
import org.doktorodata.ohdata.client.entityaccess.OhEntityIterator;
import org.doktorodata.ohdata.client.entityaccess.OhEntityResult;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhEntityAccessException;

@Generated("DoktorOData - OhData-Client")
public class ProductAPI {

    private OhCallerFactory callerFactory;
    private OhEntityAccess<Order_Details> order_detailsEA;
    private OhEntityAccess<Products> productsEA;

    public ProductAPI(String destination)
        throws ConnectionFactoryException
    {
        callerFactory = OhCallerFactory.createWithDestination(destination, "");
        order_detailsEA = new OhEntityAccess<Order_Details>(callerFactory, Order_Details.class);
        productsEA = new OhEntityAccess<Products>(callerFactory, Products.class);
    }

    public OhEntityResult<Products> getProductByID(Integer ProductID)
        throws OhEntityAccessException
    {
        return productsEA.get(ProductID);
    }

    public OhEntityIterator<Products> getProductsWithPriceRange(BigDecimal minPrice, BigDecimal maxPrice)
        throws OhEntityAccessException
    {
        OhQuery query = new OhQuery();
        query.filter("UnitPrice", "gt", minPrice);
        query.and();
        query.filter("UnitPrice", "lt", maxPrice);
        return new OhEntityIterator<Products>(productsEA, query);
    }

    public OhEntityResult<Products> getProductsWithName(String ProductName)
        throws OhEntityAccessException
    {
        OhQuery query = new OhQuery();
        query.filter("ProductName", "eq", ProductName);
        return productsEA.query(query);
    }

    public OhEntityResult<Order_Details> getOrdersOfProduct(Integer ProductID)
        throws OhEntityAccessException
    {
        OhQuery query = new OhQuery();
        query.filter("ProductID", "eq", ProductID);
        return order_detailsEA.query(query);
    }

}
