Table of contents
* [Introduction](#introduction)
* [Quick Start](#quick-start)
* [Implementation](#implementation) 
* [Test](#test) 
* [Deployment](#deployment) 
* [Improvements](#improvements) 

# Introduction
This project aims to replace a legacy security trading platform with a new system that applies microservice architecture and the Springboot framework. The new application manages clients and accounts and facilitates online stock trading and is presented as a proof of concept to the Jarvis trading team. The backend of the trading platform follows the MVC design patterns and provides a REST API that allows clients to fetch real-time quote information, create a trading account, withdraw or deposit money and store quote data to a database using PSQL. Http requests are made to IEXCloud for quote data and response calls are parsed and data is stored in the local database using JDBC. Security orders can also be executed and stored in the database by extending the project using the pre-defined models.

Thre were many different technologies used including:
* Springboot for dependency management and injection
* Apache Tomcat to implement the Java Servlet, JavaServer Pages and Java WebSocket technologies to run java code on a HTTP web server
* Postgres for storage of client, account balance, quotes, security order and position data
* JDBC for interacting with the postgres DB
* Maven for project management and dependencies along with packaging
* SwaggerUI to provide a UI to interact with the REST API at the frontend
* Junit for Integration testing and automating test cases
* Docker for remote deployment

# Quick Start
- Prequisites: Docker, CentOS 7    
#### 1a. Download images

```
docker pull cursabre/trading-psql
docker pull cursabre/trading-app
```
#### 1b. OR build images using docker files
```
cd ./springboot/psql
docker build -t trading-psql .  #The dot denotes the current directory

cd ./springboot/
docker build -t trading-app .  #The dot denotes the current directory
```
#### Setup network
```
docker network create --driver bridge trading-net
```
#### Start the psql container
```
docker run --name trading-psql \
-e POSTGRES_PASSWORD=password \
-e POSTGRES_DB=jrvstrading \
-e POSTGRES_USER=postgres \
--network trading-net \
-d -p 5432:5432 trading-psql
```
#### Register IEX token for quote access
```
IEX_PUB_TOKEN="YOUR_TOKEN"
```
#### Start the trading app container which uses the trading-psql and network
```
docker run --name trading-app-dev -e "PSQL_HOST=trading-psql-dev" -e "PSQL_PORT=5432" -e "PSQL_DB=jrvstrading" -e "PSQL_USER=postgres" -e "PSQL_PASSWORD=password" -e "IEX_PUB_TOKEN=${IEX_PUB_TOKEN}" --network trading-net -p 8080:8080 -t trading-app
```
#### Verify containers are running (if not already in springboot run window)
```
docker ps
```
#### Open application with Swagger by visiting  http://localhost:8080/swagger-ui.html in a browser
API calls can also be executed using your favourite API software (ex. Postman)

(screenshot)


## REST API Usage
### Swagger
Swagger UI allows developers to visualize and interact with API's resources without having the implementation logic in place. It uses OpenAPI specification to automatically generate a visual UI with documentation for back end implementation and client side consumption.

# Test 
How did you test your application? Did you use any testing libraries? What's the code coverage?
The application was tested at every layer (Repository, Service, Controller) using integration testing, API calls and UI interaction. Testing was implemented with high regard to code coverage (>80%) to evaluate working behaviour.
