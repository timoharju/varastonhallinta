# Group 4

# otp-varastonhallinta

# Overview
Maven warehouse project for warehouse management

# Installation
1. Create a connectable mysql database.
2. Edit the persistense.xml and hibernate.conf files to include the proper information to connect to your database (user, password, database name).
3. Compile and run the application.

# Tools & technologies used 
1.	Java 1.8
2.	Maven 3.6.2
3.	Hibernate 5.x
4.	Mysql 5
5.	Eclipse
6.  JUnit 3.8.1

####	Dependencies (POM.xml)
```xml
   		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>5.4.1.Final</version>
		</dependency>
			<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>8.0.17</version>
		</dependency>
```