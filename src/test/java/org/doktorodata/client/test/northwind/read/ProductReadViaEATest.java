package org.doktorodata.client.test.northwind.read;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.doktorodata.client.test.northwind.gen.entities.Products;
import org.doktorodata.ohdata.client.base.OhCallerFactory;
import org.doktorodata.ohdata.client.base.OhQuery;
import org.doktorodata.ohdata.client.entityaccess.OhEntityAccess;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhEntityAccessException;
import org.junit.Test;

public class ProductReadViaEATest {

	private static final String destination = "src/test/resources/Northwind.destination";
	
	private static int productId = 3;
	private static String productName = "Aniseed Syrup";
	
	@Test
	public void readProductsByName() throws ConnectionFactoryException, OhEntityAccessException {
		
		
		OhCallerFactory ocf = OhCallerFactory.createWithDestination(destination);
		OhEntityAccess<Products> oea = new OhEntityAccess<>(ocf, Products.class);
		
		Products product = oea.get(productId).getEntry();		
		Assert.assertEquals(productName, product.getProductName());
		
		OhQuery query = new OhQuery();
		query.filter("ProductName", "eq", product.getProductName());
		Assert.assertEquals(productId, oea.query(query).getFeed().get(0).getProductID().intValue());
		
	}
	
	
}
