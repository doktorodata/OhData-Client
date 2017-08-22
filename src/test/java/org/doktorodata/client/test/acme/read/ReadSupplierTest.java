package org.doktorodata.client.test.acme.read;

import java.util.Iterator;
import java.util.List;

import org.doktorodata.client.test.acme.gen.entities.Suppliers;
import org.doktorodata.ohdata.client.base.OhCallerFactory;
import org.doktorodata.ohdata.client.entityaccess.OhEntityAccess;
import org.doktorodata.ohdata.client.exceptions.ConnectionFactoryException;
import org.doktorodata.ohdata.client.exceptions.OhEntityAccessException;
import org.junit.Test;

public class ReadSupplierTest {

	private static final String destination = "src/test/resources/ACME.destination";

	@Test
	public void readSupplier() throws ConnectionFactoryException, OhEntityAccessException{
		
		OhCallerFactory ocf = OhCallerFactory.createWithDestination(destination);
		OhEntityAccess<Suppliers> oea = new OhEntityAccess<>(ocf, Suppliers.class);
		
		List<Suppliers> suppliers = oea.query().getFeed();
		for (Suppliers supplier : suppliers) {
			System.out.println(supplier.getName() + "-" + supplier.getAddress_City());
		}
		
	}
}
