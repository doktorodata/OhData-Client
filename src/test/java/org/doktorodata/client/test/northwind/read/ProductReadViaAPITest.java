package org.doktorodata.client.test.northwind.read;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.doktorodata.client.test.northwind.gen.api.ProductAPI;
import org.doktorodata.client.test.northwind.gen.entities.Order_Details;
import org.doktorodata.client.test.northwind.gen.entities.Products;
import org.doktorodata.ohdata.client.entityaccess.OhEntityIterator;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhEntityAccessException;
import org.junit.Test;

public class ProductReadViaAPITest {

	private static final String destination = "src/test/resources/Northwind.destination";
	private static String productName = "Aniseed Syrup";
	
	@Test
	public void readProductsByName() throws ConnectionFactoryException, OhEntityAccessException {
		
		ProductAPI productAPI = new ProductAPI(destination);
		List<Products> products = productAPI.getProductsWithName(productName).getFeed();
		
		for (Products product : products) {			
			Assert.assertEquals(productName, product.getProductName());
			System.out.println(product.getProductID() + " - " + product.getProductName());
		}
		
	}
	
	private final int productID = 3;
	
	@Test
	public void readProductByID() throws ConnectionFactoryException, OhEntityAccessException{
		ProductAPI productAPI = new ProductAPI(destination);
		Products product = productAPI.getProductByID(productID).getEntry();
		Assert.assertEquals(productID, product.getProductID().intValue());
	}
	
	private final double minPrice = 12;
	private final double maxPrice = 45.0;
	
	@Test
	public void readProductsByPriceRange() throws ConnectionFactoryException, OhEntityAccessException {
		
		ProductAPI productAPI = new ProductAPI(destination);
		OhEntityIterator<Products> productIT = productAPI.getProductsWithPriceRange(new BigDecimal(minPrice), new BigDecimal(maxPrice));
		
		int cnt = 0;
		
		while (productIT.hasNext()) {
			Products product = (Products) productIT.next();
			
			Assert.assertTrue("Higher than min price ", product.getUnitPrice().doubleValue() > minPrice);
			Assert.assertTrue("Lower than max price ", product.getUnitPrice().doubleValue() < maxPrice);
			
			cnt++;
		}
		
		System.out.println("Checked "+ cnt + " products");
	}
	
	@Test
	public void readOrderDetailsForThreeProducts() throws ConnectionFactoryException, OhEntityAccessException {
		
		ProductAPI productAPI = new ProductAPI(destination);
		OhEntityIterator<Products> productIT = productAPI.getProductsWithPriceRange(new BigDecimal(minPrice), new BigDecimal(maxPrice));
		
		int cnt = 0;
		
		while (productIT.hasNext()) {
			Products product = (Products) productIT.next();
			List<Order_Details> ordersOfProduct = productAPI.getOrdersOfProduct(product.getProductID()).getFeed();
			System.out.println(product.getProductName());
			Assert.assertTrue("Orders found", ordersOfProduct.size() > 0);
			for (Order_Details orderPos : ordersOfProduct) {
				System.out.println("\t" + orderPos.getOrderID() + " with discount " + orderPos.getDiscount());
			
			}
			
			if(cnt++ > 3)
				break;
		}
			
	}
}
