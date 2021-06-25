# Spark/Scala Project
# Introduction

Following the success of the [Python Data Wrangling PoC project](../python_data_wrangling) to provide customer insights to the London Gift Shop (LGS) marketing team, the LGS company wants to invest more money in the project and implement the data strategy across the company. This project aimed to apply the data solution with Apache Spark which is dedicated to big data processing to work with LGS' large datasets. Two approaches were evaluated: Google Cloud Platform + Zeppelin and Microsoft Azure + Databricks and the latter was ultimately used alongside Scala. Databricks maintains a cloud platform built around spark and offers features such as computation clusters along with interactive workspace for exploration and visualization which were used to reimplement the python project and provide data analytics to the LGS marketing team for their marketing strategies. Through use of structured APIs, the details are abstracted in relation to fragmentation of data over the cluster and it allows the user to interact with the data tables in a way similar to that of Pandas. The project successfully implemented the Python data analytics project using Scala and Apache Spark.



# Databricks and Hadoop Implementation

The data set used in this project was retail data from the London Gift Shop in the form of a .csv. The schema of the data is as follows:


| Field Name        | Data Type          | Description  |
| ------------- |:-------------:| -----:|
| invoice_no      | String | The number corresponding the invoice |
| stock_code      | String      |   The retail code associated with the item |
| description | String      |   Description of the item |
| quantity      | Integer | Quantity |
| invoice_date | Date      |    Date of the invoice |
| unit_price      | Double | Unit price of the item |
| customer_id      | String | ID of the customer who made the transaction |
| country      | String | Country the purchase was made in |

The schema was constructed beforehand to get specific assignments (invoice_no and invoice_date) which were inferred as other object types. The data set was previously used in the [Python Project](../python_data_wrangling) and most of the extraction from PostgreSQL to a .csv format was already done.

## Architecture and Workflow
![Databricks arc](/assets/spark-architecture.jpg)

The main components are Databricks Workspace, Azure Storage and Azure Virtual Machine. The Databricks workspace provides setup of data, clusters and a notebook development environment along with the implementation of Apache Spark on the data clusters. The Storage can be handled from potentially different sources, namely the Databricks File System (DBFS). The DBFS is an abstraction on top of scalable object storage while still providing access with simple pathnames to files ("dbfs://...") or directory and file semantics regardless if the storage system is HDFS or S3 etc. Also gives the added benefit of persisting files to object storage so data is not lost after the cluster is terminated.

The Azure Virtual Machines are based around the Spark architecture. The Spark Context has a driver which interacts with the cluster manager in charge of manager jobs and allocates resources to applications. The sent jobs are sent out to the worker nodes. The worker nodes execute the work assigned and report the state of the computation back to the driver. 

After creating a Microsoft Azure account, access was gained to the Databricks workspace which allowed for a specified cluster to be provisioned. Databricks UI was used in creation of the cluster as well as the storage of the retail data .csv into Databricks File System (DBFS). Spark uses a Hive Metastore by default to work with database objects. The data was imported in the Databricks notebook with the above defined schema into a DataSet and then persisted to a table to run SQL queries if needed. The data was queried to produce data analytics and answer business questions such as:

- Monthly placed orders vs. canceled orders    
- Monthly sales and Monthly sales growth    
- Total monthly orders by new and existing customer    
- Recency, Frequency and Monetary Analysis    

## Databricks Notebook
[Databricks Notebook link](notebook/retail_data_wrangling.dbc)

![DBScreen](assets/databricks_screenshot.png)

# Zeppelin and Hadoop Implementation
Google Cloud Platform (GCP) was utilized with Zeppelin once again to run Apache Spark in a cluster of provisioned nodes to work with the previously used World Development Index dataset. This is a 2.5Gb .csv file that has been stored in parquet for optimized data access. The schema is as follows:

| Field Name        | Data Type          | Description  |
| ------------- |:-------------:| -----:|
| year      | Integer | Year the indicatorValue corresponds to |
| countryName      | String      |   Name of country |
| countryCode | String      |    Country code shortform |
| indicatorName      | String | Description of what the indicatorValue represents |
| indicatorCode | String      |    Code corresponding to what the indicatorValue represents|
| indicatorValue      | Float | The value of interest |

The aim of this project was to evaluate GCP and Zeppelin and this was done through analysis of results running SQL and scala queries focused around the GDP information. The queries in the notebook show:

- Historical GDP for Canada
- GDP for each country sorted by year
- Highest GDP for each country (among all their years)

## Architecture
![Hadoop arc](../hadoop/assets/hadoop-architecture.jpg)

The setup for the Hadoop cluster and Zeppelin UI was completed in a previous [Hadoop project](../hadoop/). The project used 1 master node and 2 worker nodes for the setup. The nodes were 2 vCPUs with 12GB memory and 100GB disk size to accommodate imported data and multiple formatted tables. The nodes were hosted in Google Dataproc as part of the cluster and interactions with the cluster was done through SSH and Web UI (Tez, YARN job monitoring and Zeppelin). A bucket was created in GCP under the project for storage. 
Metadata was stored in the Hive Metastore and the dataset was stored in the Hadoop Distributed File System (HDFS) as parquet for quicker parallel data access and fault tolerance through data replication. Zeppelin offers data ingestion, discovery and analytics through multiple language/data-processing backend plugins. This project used the Apache Spark plugin with scala as the language of choice. 

## Zeppelin Notebook
[Zeppelin Notebook link](notebook/sparkDataframe-WDIdataAnalytics.ipynb)

![ZeppScreen](assets/gdp_zeppelin_screenshot.png)

# Future Improvements
- Explore more of Apache Spark - including batch processing and Spark Streaming
- Enact Spark's built-in Machine Learning libraries to create advanced analytics
- Create RFM segmentation categories similar to Python project
