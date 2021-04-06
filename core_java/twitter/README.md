# Introduction
The Twitter application allows the user to post, view and delete tweets from the command line interface. The project interacts with the Twitter REST API to send and recieve requests for tweets using HttpClient and OAuth 1.0 credentials. The project uses Data Access Objects, Jackson for JSON, Maven for project management and Docker for deployment.

# Quick Start
1. Set up the environment variables used for authorization with twitter



There are two approaches for running the application:
2. Maven
After cloning the repo, navigate to the root directory of the project and do

`mvn clean package -Dmaven.test.skip=true`

to produce a .jar file that can accept commands from the command line.

3. Docker


Add 

# Design
## UML diagram
## explain each component(app/main, controller, service, DAO) (30-50 words each)
## Models
Talk about tweet model
## Spring
- How you managed the dependencies using Spring?

# Test
How did you test you app using Junit and mockito?

## Deployment
How did you dockerize your app.

# Improvements
- Imporvement 1
- Imporvement 2
- Imporvement 3
