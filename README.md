# OhData-Client - Consume OData in an easy and powerful way

## Introduction
### What is OData and what is the idea of this library
OData (<http://www.odata.org/>) is an "open protocol to allow the creation and consumption of queryable and interoperable RESTful APIs in a simple and standard way."
It has the official slogan "the best way to REST". It is quite powerful and has various benefits, but also has it's downsides and complexities. 
I personally like OData and made good experience in the .NET world, but the in the Java world it is not so easy to use. 
"OhData Client" uses the most familiar OData library Olingo and builds on top a powerful library that makes it possible to consume OData V2 services in a few seconds. 
Come in and see with your own eyes.

## Getting Started
### Maven
Add maven dependency to your POM.

```
<dependency>
	<groupId>org.doktorodata.ohdata</groupId>
	<artifactId>OhData-Client</artifactId>
	<version>0.8.0</version>
</dependency>
```
### Add a destination file
Create a destination file that contains the URL of your OData services. The base URL is also sufficient and you can add the full path later. Additionally the destination contains the option to provide the authentication method. 

*Remark*
This approach (storing the authentication in this text file) is currently the starting point, if you need something more sufficient please don't hesitate to build your own ConnectionFactory (e.g. using a secure store) by extending the abstract class org.doktorodata.ohdata.connectivity.ConnectionFactory,

Here are several examples for destination files:

#### Without Authentication
```
URL=http://services.odata.org/V2/Northwind/Northwind.svc
Authentication=None
```

#### With Basic Auth
```
URL=http://services.odata.org/V2/Northwind/Northwind.svc
Authentication=Basic
User=DRODATA
Password=Great1234
```

#### With oAuth Token
```
URL=http://services.odata.org/V2/Northwind/Northwind.svc
Authentication=oAuthToken
Password=kh3khuwz7z23h23h2iuh232732h3k2
```

#### With HTTP Proxy
```
URL=http://services.odata.org/V2/Northwind/Northwind.svc
Authentication=None
proxyhost=proxy.local
proxyport=9000
```

### Generate the entity stubs
The usage of the OhData library starts with the generation of stubs. Therefore the metadata of the OData services is read and a the stub generator creates POJO classes for all entities in the OData services. These classes are later used in your productive code.

Therefore I usualy create a simple JAVA class that calls the EntityStubGenerator in the main method. This generator is called with the following attributes:
* the base package to which the stubs shall be generated
* the destination of the OData services
* the base path to which the stubs shall be generated

```Java
public class RegenerateTestArctifacts {

	private static final String basePackage = "org.doktorodata.client.test.gen";
	private static final String basePath = "./src/test/java";
	private static final String destination = "src/test/resources/Northwind.destination";
	
	public static void main(String[] args) throws Exception {		
		EntityStubGenerator pocoGen = new EntityStubGenerator(basePackage, destination, basePath);
		pocoGen.generateEntityStubs();
		
	}
}

```
### Start coding
After the successful generation of the stubs these classes can be used to call the OData services comfortably. The initialization part creates the OhCallerFactory with the name of the destination. The OhEntityAccess uses Generics and is initialized with the concrete entity class you want to access in the OData service. Afterwards the OhQuery allows to create complex queries and they are executed calling the query method of the OhEntityAccess. In this example you can see how it works. 

```Java

String destination = "src/test/resources/Northwind.destination";
String productName = "Aniseed Syrup";

//Initialize
OhCallerFactory ocf = OhCallerFactory.createWithDestination(destination);
OhEntityAccess<Products> oea = new OhEntityAccess<>(ocf, Products.class);
	
//Query by Product Name
OhQuery query = new OhQuery();
query.filter("ProductName", "eq", product.getProductName());
oea.query(query).getFeed();
```

## More Questions and Answers
### Example OData Services
There are several OData service examples that can be access for testing purposes.
- The Northwind service provides read access only <http://services.odata.org/V2/Northwind/Northwind.svc/>
- This service provides read and write access <http://services.odata.org/V2/(S(o2eiimg0orsice3aiklbrlfy))/OData/OData.svc/>

### OData Versions V2 vs V4  
This is one of the disadvantages. There are various versions of OData. The most used ones are V2 and V4. This OhData Client library currently only supports V2, but as soon as we have finished this we will continue with V4.

### Can I contribute
Yes, feel free to send me an email and I'm happy for every support I can get. 
