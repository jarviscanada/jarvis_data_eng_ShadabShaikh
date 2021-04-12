# Introduction

This project explores the relationship between Java and RDBMS using JDBC to interact with data in a retail customer order database. The application employs CRUD (Create, Read, Update, Delete) operations on the database using PostgreSQL and Data Transfer Objects. Psql was instanced using Docker and dependencies were handled through Maven while the JDBC handled the connection between the application and the database.

# Implementation

## ER Diagram

![ER Diagram](assets/customerOrderSchema.png?raw=true "ER Diagram")

# Design Patterns
This project uses both the DAO and the repository design patterns.The Data Access Object (DAO) isolates the application layers from the persistence layer using an abstract API. The OrderDAO class follows the DAO pattern and is best suited for handling join-heavy queries while the CustomerDAO class is best defined as using the repository pattern since it only interacts with a single customer table. DAO is a great choice in a centralized system where joins are computer with foreign keys.

# Testing
The output from the SQL queries in JDBC was tested against the database GUI application DBeaver to produce the same results.
