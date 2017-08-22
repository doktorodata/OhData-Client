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
Add this dependency to your POM.

´
<dependency>
	<groupId>org.doktorodata.ohdata</groupId>
	<artifactId>OhData-Client</artifactId>
	<version>0.8.0</version>
</dependency>
´
### Add a destination file

### Generate the entity stubs

### Start coding



## More Questions and Answers
### Example OData Services
There are several OData service examples that can be access for testing purposes.
- The Northwind service provides read access only <http://services.odata.org/V2/Northwind/Northwind.svc/>
- This service provides read and write access <http://services.odata.org/V2/(S(o2eiimg0orsice3aiklbrlfy))/OData/OData.svc/>

### OData Versions V2 vs V4  
This is one of the disadvantages. There are various versions of OData. The most used ones are V2 and V4. This OhData Client library currently only supports V2, but as soon as we have finished this we will continue with V4.