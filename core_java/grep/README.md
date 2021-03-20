# Introduction
The Java Grep application is used to search plain-text data sets for lines that match a regular expression. This is similar to the functionality found in Unix-like operating systems. The application was was built with an interface and implemented with while/for loops along with an alternate implementation using Streams and Lambda expressions for efficiency. Project management was achived through Maven which also handled dependencies. The libraries used were java.io (File, FileReader/Writer, BufferedReader), java.util (ArrayList, LinkedList, Queue), java.nio (Files, Paths) and org.slf4j for logging functionality. A docker image was also created to run the application.

# Quick Start
The application can be run using the following methods:

1. Class path
```
$ mvn clean compile 
$ mvn package
$ java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp {regex} {rootPath} {outFile}
```

2. Docker
```
$ docker pull cursabre/grep  
$ docker run --rm -v {rootPath} -v {outFile} cursabre/grep {regex} {rootPath} {outFile}
```

# Implementation  
This application takes in the arguments for the search terms in regular expressions (Regex), the root path to search all subdirectories and the output file for the results.
## Pseudocode
The process method is explained as:
```
for (File file: listFiles(path)) 
  for (String line: readLines(file)) 
    if (containsPattern(line)) 
      matchedLines.add(line);
writeToFile(matchedLines);
```
Different methods are implemented from the interface at every step. For Lambda and Streams implementation a functional style of code was used along with File and Path classes.


## Performance Issues
Using Java Grep requires a lot of memory in the JVM especially when parsing all text to be checked by the containsPattern function. Minimum and Maximum memory can be defined by using the -Xms and -Xmx flags to set the memory sizes. 

```
java -Xms5m -Xmx30m -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp .*Romeo.*Juliet.* {rootPath} {outFile}
```
If the memory was 5Mb and a file was processed with 5Mb then a memory error would occur because there isn't enough memory to complete the process. 

The memory issues can be solved using a BufferReader or the Stream API. Both approaches were used in this application.

# Testing
The application was tested using test cases in IntelliJ where sample regex patterns where employed on different paths and compared against Ubuntu's grep command. 

# Deployment
An image was generated for the application with the compiled .jar file. The image can be found on Docker Hub which then runs the application in a container on a local machine.

# Improvement
* Java grep has relatively large memory requirements for reading and processing large files. Some buffer objects were used but the memory management could be improved further
* A filter could be added for the type of files to search (txt, sql etc.)
* Allow added features in the writing portion of the program (add in file name and path beside text)
