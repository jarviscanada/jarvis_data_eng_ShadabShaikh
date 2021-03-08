# Linux Cluster Monitoring Agent
The Linux Cluster Monitoring Agent is a tool that is used to monitor the hardware along with the resources of a cluster of Linux computers connected in a network. The program sets up a psql docker instance with scripts to populate the host information and usage to a database. Using crontab the program can automate to add to the most current up to date information about disk and memory usage every minute. The database can be queried to determine statistics and health about the cluster. 

The monitoring agent can be used by the infrastructure team to generate reports for future resource planning purposes and to detect failures of any nodes. 

The linux cluster was running CentOS and the scripts were compiled using bash for initializing docker, creating the database and tables, and populating the tables with data. Postgres was used for the RDBMS through Docker and a Postgres instance was created with a customized bash script for setup. The scripts utilize regular expressions to isolate the desired data from the unix system. Version control was maintained using git on the local repository and GitHub for the remote repository. 

# Quick Start

- Start a psql instance using psql_docker.sh  
The create command makes the container and volume. Use start or stop commands to start or stop the psql instance.

``` 
$ ./scripts/psql_docker.sh start|stop|create [db_username][db_password]
```

- Create tables using ddl.sql  
Create tables for `host_info` and `host_usage` into the database `host_agent` by running:

```
$ psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
```

- Insert hardware specs data into the db using `host_info.sh`. 
Populate the data in the host_info table. This scripts only runs once.  
```
$ ./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
```

- Insert hardware usage data into the db using `host_usage.sh`  
Populate the data in the host_usage table.

```
$ ./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```


- Crontab setup  
Crontab can set up to automate the host_usage information into the database every minute. The crontab job file can be modified using:

```
$ crontab -e
```
Add this to the crontab file:

```
* * * * * bash /home/centos/dev/jrvs/bootcamp/linux_sql/host_agent/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```

- (Optional) Some sample queries can be run using the `queries.sql` file

```
$ psql -h localhost -U postgres -d host_agent -f sql/queries.sql

```

# Implementation  
The linux cluster is connected through a network switch through internal IPv4 addresses. For the Linux Cluster Administration (LCA) team to record and identify resources of the nodes/servers a simple system was created to quickly set up the docker container, database and tables.  

The `psql_docker.sh` script creates the PostgreSQL container and allows the user to start and stop the instance. After connecting to the database the `ddl.sql` script creates the tables for host_info and host_usage. The host_info table contains the host id along with core hardware information like CPU architecture and total memory. The host_usage table is the main table where the monitoring queries will be executed on. It provides the current cpu_idle, disk_io values along with free memory information.

These tables are populated using the `host_info.sh` and `host_usage.sh` scripts which uses regular expressions and bash commands (awk, sed, grep) to obtain the desired data for the tables. Once the crontab job is set up the host_usage table provides a new row in the database every minute with updated information. This table can be viewed and queried to determine the state of the nodes. Some sample queries are attached in the `queries.sql` file which show a sorted table of machines by total memory or the number of crontab entries in 5 minute intervals to determine uptime.


## Architecture
![Alt text](assets/linux_sqldiagram.png?raw=true "Title")

## Scripts
### psql_docker.sh
The script starts docker and makes the PostgreSQL container if it doesn't already exist. It can be used to create, start and stop the container.

```
# create a psql docker container with the given username and password.
$ ./scripts/psql_docker.sh create db_username db_password
#start container
$ ./scripts/psql_docker.sh start
#stop container
$ ./scripts/psql_docker.sh stop
```
### ddl.sql  
 Creates the tables to be used for host_info and host_usage. 
```
#Usage
$ psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
```

### host_info.sh
Inserts the core host information into the database for the specific machine.

```
$ ./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
```
- Commands/Arguments:
  - `psql_host`: Name of the host (localhost) 
  - `psql_port`: Port for the psql instance (common: 5432)
  - `db_name`: The name of the database 
  - `psql_user`: The username for the instance (psql)
  - `psql_password`: The password for the instance (password)

### host_usage.sh
Inserts the host usage information into the database for the specific machine.

```
$ ./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```
- Commands/Arguments:
  - `psql_host`: Name of the host (localhost) 
  - `psql_port`: Port for the psql instance (common: 5432)
  - `db_name`: The name of the database 
  - `psql_user`: The username for the instance (psql)
  - `psql_password`: The password for the instance (password)

### crontab
Utilized to run host_usage.sh every minute.

### queries.sql
An sql file with queries for three questions:
1. Which host has the most total memory?
2. What is the average used memory in percentage for each host in 5 minute intervals?
3. How can a host failure be detected by querying and crontab?

## Database Modeling
The schema for the tables in the database and their types of values are as follows:
- `host_info`

| Column           | Data type | Description                                   |
|------------------|-----------|-----------------------------------------------|
| id               | Serial    | Host id                                       |
| hostname         | Varchar   | The name of the host machine                  |
| cpu_number       | Varchar   | The number of cpus                            |
| cpu_architecture | Varchar   | Architecture of cpu of host                   |
| cpu_model        | Varchar   | The model of the cpu                          |
| cpu_mhz          | Float     | The speed of the cpu in mhz                   |
| L2_cache         | Integer   | L2_cache storage in kB                        |
| total_mem        | Integer   | Total memory of the host in kB                |
| timestamp        | Timestamp | Time that host info was entered into database |


- `host_usage`

| Column         | Data type | Description                                     |
|----------------|-----------|-------------------------------------------------|
| timestamp      | Timestamp | Timestamp when data was entered into host_usage |
| memory_free    | Integer   | Free memory of host in MB                       |
| host_id        | Serial    | Id of host                                      |
| cpu_idle       | Integer   | Unused cpu processing in percentage             |
| cpu_kernel     | Integer   | Percentage of cpu kernel used                   |
| disk_io        | Integer   | Number of disk I/O                              |
| disk_available | Integer   | Available space on the disk                     |


# Test
The scripts were tested individually using bash -x to look for any script failures. Exit codes were placed in the scripts to ensure successful completion of each script and they were tested together with other scripts with no errors to provide accurate data.
Sql queries were also run against the database to obtain outputs that matched the desired outcomes.

# Improvements
- Write more specific error messages in case of script failures
- Create a script to automate crontab along the lines of the other bash scripts
- Add more entries to be monitored in host_usage
