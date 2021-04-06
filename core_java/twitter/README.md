# Introduction
The Twitter application allows the user to post, view and delete tweets from the command line interface. The project interacts with the Twitter REST API to send and receive requests for tweets using HttpClient and OAuth 1.0 credentials. The project uses Data Access Objects, Jackson for JSON, Maven for project management and Docker for deployment.

# Quick Start
### Set up the environment variables used for authorization with twitter

`$ export consumerKey=consumer_key_value`

For the following: `consumerKey, consumerSecret, accessToken, tokenSecret`


There are two approaches for running the application:
### 1. Maven
After cloning the repo, navigate to the root directory of the project and do

`$ mvn clean package -DskipTests`



to produce a .jar file that can accept commands from the command line. The same run commands apply as Docker.

#### Run commands

`$ java -jar [.jar file] post|show|delete [options]`

### 2. Docker

`$ docker pull cursabre/twitter`

#### Run command

`docker run --rm \    
-e consumerKey=YOUR_VALUE \    
-e consumerSecret=YOUR_VALUE \    
-e accessToken=YOUR_VALUE \    
-e tokenSecret=YOUR_VALUE \    
cursabre/twitter post|show|delete [options]`

#### Post tweets

`docker run --rm \    
-e consumerKey=YOUR_VALUE \    
-e consumerSecret=YOUR_VALUE \    
-e accessToken=YOUR_VALUE \    
-e tokenSecret=YOUR_VALUE \   
$ twitter post "tweet_text" latitude:longitude`

#### Show tweets

`docker run --rm \    
-e consumerKey=YOUR_VALUE \    
-e consumerSecret=YOUR_VALUE \    
-e accessToken=YOUR_VALUE \    
-e tokenSecret=YOUR_VALUE \   
$ twitter show tweet_id [field_to_filter, field_to_filter2]`

#### Delete tweets

`docker run --rm \    
-e consumerKey=YOUR_VALUE \    
-e consumerSecret=YOUR_VALUE \    
-e accessToken=YOUR_VALUE \    
-e tokenSecret=YOUR_VALUE \   TwitterApp delete [id1,id2,..]`

# Design
## UML diagram

The project applies the MVC design pattern with different layers represented by the classes. There is the Controller layer, Service layer and Data Access Object (DAO) layer. There is also an Application layer that interfaces between the CLI and the other components. The components include:

### TwitterCLIApp

Initializes all other components and dependencies and passes the command from the CLI to the controller layer. This class contains the main method and the important run and print commands to pass along the arguments and return the relevant tweets to the user in JSON format.

### TwitterController

Parses the CLI arguments into the proper functions for posting, viewing or deleting tweets. Does initial validation checks on number of arguments and input types. If a post function is called, the controller extracts the status update and coordinates data to pass on to the Service layer for further processing.

### TwitterService

Handles the "business logic" of the application and is considered the core of the application to pass data to the repositories. TwitterService relies on the controller to interpret the user inputs and tell it what functions to execute. The inputs are further validated to ensure the tweet object parameters are met (text length, validity of coordinates, proper status id format) and passes the checked data to the Data Access Objects.

### TwitterDAO

Accepts Data Access Objects from the Service layer, extracts the relevant data, and interacts with the proper data resource. TwitterDAO constructs the API URIs with the appropriate functionality (post, show, delete) and utilizes HttpClient objects to interact with the REST API. The processed data is parsed and returned as DAOs using the Model classes. Also handles the interaction in returning tweets with JSON using JSON Parser.

## Models

Twitter's API uses Tweet objects with subcategories of data fields to contain information about the tweet's body content, creation date, id, entities, coordinates, retweet_count, favourites etc. The Tweet model in the project focuses on some core attributes and leverages other models such as Coordinates, Entities, Hashtag and UserMentions to contain the relevant data.

Twitter's documentation on the objects is available here: https://developer.twitter.com/en/docs/twitter-api/v1/data-dictionary/object-model/tweet

The Models were as follows:

* Tweet (Tweet object, contains Entities, Coordinates objects)
* Entities (Entities object, contains hashtags and user_mentions objects)
* Hashtags (Hashtags object)
* UserMentions (user_mentions object)
* Coordinates (coordinates object)


## Spring

The project utilizes Spring for dependency management and was implemented with the aid of Maven. Spring Boot was implemented to automate the dependencies with the use of annotations in the class files. The steps are as follows:

* Added `spring-boot-starter-parent` and `spring-boot-maven-plugin` to the pom.xml file
* Wrote a Spring Boot configuration file using `CommandLineRunner` interface with a package scanning of the current project package with `@SpringBootApplication(scanBasePackages = "ca.jrvs.apps.twitter")`
* Classes were annotated with `@Component` `@org.springframework.stereotype.Service` `@Repository` and `@org.springframework.stereotype.Controller` flags to indicate the Beans to the Inversion of Control (IoC) container. Spring then injects the dependencies.
* Ran the application with Spring Boot

# Test

The project was built from the core and outwards to the user interface so test cases were important in every step of the process. Each component had integration test files made using JUnit with the relevant variables and initializations of dependencies and the test methods were run with Twitter dev authorization tokens set as environment variables. 

The components were also tested with Unit tests using Mockito to mock other components and ensure that the current layer is behaving properly for valid and invalid arguments. Mockito allowed for isolation of the current component for testing purposes and ensured that the input from other components was always as expected.

## Deployment

The project was deployed with Docker and pushed to the Docker Hub for convenience. Maven clean package was run to package the project and its dependencies into a jar file. Building the Docker image required a docker file with the following:

`FROM openjdk:8-alpine
COPY target/twitter*.jar /usr/local/app/twitter/lib/twitter.jar
ENTRYPOINT ["java","-jar","/usr/local/app/twitter/lib/twitter.jar"]`

And built using the command:

`docker build -t ${docker_user}/twitter`

The image can be pulled from docker hub for usage.

# Improvements
- Pull a list of recent tweets by an id
- Further develop the application output to be more visually appealing and readable than JSON.
- Add more DAOs for other twitter objects and fields
