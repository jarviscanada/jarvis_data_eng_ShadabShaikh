# Table of contents
* [Introduction](#Introduction)
* [Hadoop Cluster](#Hadoop-Cluster)
* [Hive Project](#Hive-Project)
* [Improvements](#Improvements)

# Introduction
The Jarvis data analytics team want to process big data using newer platforms like Apache Hadoop. Hadoop is a large ecosystem of open-source software made for reliable, scalable and distributed computing. The project's scope was to understanding Hadoop's main components and apply the concepts practically. Google Cloud Platform (GCP) was used to import a Big Data set into Hadoop Distributed Filesystem (HDFS) and Map Reduce was used on a 3-node cluster to answer questions by performing data analytics. Once the data was stored in the HDFS, high-level application were used through web interfaces such as Apache Hive and Tez to run queries against the database. Apache Hive interacted with the database through both Beeline CLI along with Zeppelin to produce a structured notebook that can run queries and explain the data flow. The data was a 1.8Gb dataset containing 2016 World Development Indicator information and was imported into GCP's bucket while utilizing compression for quicker processing. The data was optimized further through partitioning, parquet storage with columnar files in order to speed up queries. The technologies and concepts used in this project are:

- Hadoop HDFS
- YARN
- MapReduce
- Google Cloud Platform
- Google Dataproc
- Apache Hive
- Tez
- Spark
- OpenCSVserde
- Beeline CLI
- Zeppelin Notebook

# Hadoop Cluster
![Alt text](assets/hadoop-architecture.jpg?raw=true "Title")  

## Hardware specifications
The project used 1 master node and 2 worker nodes for the setup. The nodes were 2 vCPUs with 12GB memory and 100GB disk size to accommodate imported data and multiple formatted tables. The nodes were hosted in Google Dataproc as part of the cluster and interactions with the cluster was done through SSH and Web UI (Tez, YARN job monitoring and Zeppelin). A bucket was created in GCP under the project for storage.

## YARN
YARN (Yet Another Resource Manager) is a rewritten architecture of Hadoop that allows for processing and running data for batch, stream and interactive processing. YARN's Resource Manager provisions computational resources for Application Masters which are in charge of running application tasks. YARN forwards processing requests to the corresponding node manager and allocates resources for the request. The worker nodes have their node managers that handle YARN requests on that particular node.

## HDFS
Hadoop Distributed File System is where the data is stored in GCP and is leveraged for quicker parallel data access and fault tolerance through data replication. HDFS offers a scalable file storage system by splitting files into chunks and storing them in multiple nodes. When our query requests data, the namenode manages the request by directing to the proper datanode where the data is read and a direction is obtained for the next piece of data in the series. 

## Hive
Hive interacts with the HDFS through HiveQL which are SQL-like queries that execute on the database similar to an RDBMS. Hive converts the HiveQL to MapReduce jobs using Tez, Spark etc. Hive manages tables containing relative metadata in the metastore and is fast, scalable and extensible. In the project, Hive was used with both Command Line and Web UI to send queries to the JDBC driver to execute.

## Zeppelin
Apache Zeppelin is used for data ingestion, discovery and analytics through multiple language/data-processing backend plugins some of which include Google BigQuery, Flink, Hive and PostgreSQL. Zeppelin uses the Hive interpreter in a notebook styled setting. It also comes with built-in Apache Spark integration so no need for a separate module or library for usage. Zeppelin was used to ultimately produce the outputs of the queries to answer questions.

# Hive Project
The project used World Development Indicator data which was imported into HDFS for analysis. The Zeppelin notebook shown below details the main exploratory concepts and queries which were performed to answer questions relating to the highest GDP growth of certain countries. Data parsing was done using different Serial Deserializers to accurate import the data into tables. OpenCSVserde was ultimately used to properly parse one of the columns with its intended layout. Comparisons are drawn to show performance differences of query times based on bash scripts for row counting and MapReduce. There are also comparisons on performance using file optimization in partitions and parquet along with Tez vs. Spark.

![Alt text](assets/zeppelin-notebook.png?raw=true "Title")  

# Improvements
- Additional query performance comparisons between Tez and Spark
- Apply further storage and performance optimization through map joins, buckets and CLUSTER BY
- Apply Zookeeper to project to have a hot namenode for backup reasons
