Table of contents
* [Introduction](#introduction)
* [Quick Start](#quick-start)
* [Implementation](#implementation) 
* [Test](#test) 
* [Deployment](#deployment) 
* [Improvements](#improvements) 

# Introduction
This project aims to replace a legacy security trading platform with a new system that applies microservice architecture and the Springboot framework. The new application manages clients and accounts and facilitates online stock trading and is presented as a proof of concept to the Jarvis trading team. The backend of the trading platform follows the MVC design patterns and provides a REST API that allows clients to fetch real-time quote information, create a trading account, withdraw or deposit money and store quote data to a database using PSQL. Http requests are made to IEX Cloud for quote data and response calls are parsed and data is stored in the local database using JDBC. Security orders can also be executed and stored in the database by extending the project using the pre-defined models.

There were many different technologies used including:
* Springboot for dependency management and injection
* Apache Tomcat to implement the Java Servlet, JavaServer Pages and Java WebSocket technologies to run java code on a HTTP web server
* Postgres for storage of client, account balance, quotes, security order and position data
* JDBC for interacting with the postgres DB
* Maven for project management and dependencies along with packaging
* SwaggerUI to provide a UI to interact with the REST API at the frontend
* Junit for Integration testing and automating test cases
* Docker for remote deployment

# Quick Start
- Prequisites: Docker 17.05 or higher, CentOS 7    
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

# Implementation
## Architecture
The trading application implements the MVC design pattern which separates out the components on their respective layers and keeps consistent with the microservice architecture. The client makes a REST HTTP request which is handled with Apache Tomcat which sends the request to the intended endpoint controller class which processes the request. Since the application employs RESTful API it can be consumed by clients through web and mobile applications.

### Controller Layer
Set the endpoints which the client is sending requests to based on the REST API calls. The Controller layer then sends the request with data to the appropriate service component. 

### Service Layer
Contains the business logic of the application. This layer validates inputs before passing it down to the appropriate DAO layer. The data returned is returned to the called Controller class.

### Data Access Object Layer
Interacts with the database to directly implement CRUD operations. Constructs SQL queries to execute based on the passed object models. Also manages and handles the HttpClient and DataSource (JDBC) for web or database connections.

### SpringBoot
Handles dependency injection and provides the Tomcat web servlet to handle the REST API requests to their endpoints. The Spring framework implements Inversion of Control for loose decoupling of the execution from implementation. Springboot beans were applied to the classes through annotation (@Service, @Repository etc.)

### PSQL and IEX
PSQL is the Postgres database used for persistent storage of the application. The trading-psql docker image runs a SQL script to initialize the database and allow for connections from the trading-app. IEX Cloud is a flexible financial development platform that provides financial data. IEX API calls return quote data (JSON) which is parsed into Quote objects with selective fields and can be added to the local quote database.

## REST API Usage
### Swagger
Swagger UI allows developers to visualize and interact with API's resources without having the implementation logic in place. It uses OpenAPI specification to automatically generate a visual UI with documentation for back end implementation and client side consumption. Clients can use the application without having to define API calls or parse JSON responses.
### Quote Controller 
The Quote controller fetches quote information from IEXQuote and stores the information into the PSQL database in the quote table.   
- **GET** `/quote/dailyList`: Lists all securities that are available to trading in this trading system
- **GET** `/quote/iex/ticker/{ticker}`: Displays all the quote information for the security pulled from the IEX.
- **POST** `/quote/tickerId/{tickerId}`: Adds a new security to the dailyList to be available for trading.
- **PUT** `/quote/`: Allows for manual updates to the quote information in the quote table for a particular quote.
- **PUT** `/quote/iexMarketData`: Updates all the quotes in the table using the latest information from IEX.

### Trader Controller
The Trader controller handles creation and deletion of trader accounts along with handling funds deposit and withdrawals.
- **POST** `/trader/`: Allows for manual creation of a trader and an associated account with zero funds. Uses the Data Transfer Object (DTO) for manual entries of the trader object fields.
- **POST** `/trader/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}`: Create a trader and an associated account with zero funds. Specify the trader information through the API call or by filling out the SwaggerUI fields.
- **DELETE** `/trader/traderId/{traderId}`: Delete a trader by ID.
- **PUT** `/trader/deposit/traderId/{traderId}/amount/{amount}`: Deposits funds into the associated trader account.
- **PUT** `trader/withdraw/traderId/{traderId}/amount/{amount}`: Withdraws funds from the associated trader account.

# Test 
The application was tested at every layer (Repository, Service, Controller) using integration testing, API calls and UI interaction. Testing was implemented with high regard to line coverage (>80%) to evaluate working behaviour.

# Deployment
The Docker daemon listens for docker requests and manages the images, containers and networks. The docker CLI commands are used to make build the images, run the containers and create the network. When the docker containers are built, the psql database is initialized with the schema.sql for the relevant tables. Docker run makes the containers from the images with the user database parameters and they are connected using the trading-net network.

# Improvements
- Implement order controller to place orders on securities
- Implement limit orders for buying/selling stocks instead of just market orders
- Configure multiple accounts for one trader
- Implement dashboard controller to show trader and account information as well as positions
