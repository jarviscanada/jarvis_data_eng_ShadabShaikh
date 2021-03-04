#!/bin/bash

#script pseudocode
#check and run docker if it's not already running
sudo systemctl status docker || systemctl start docker

cmdd=$1
db_username=$2
db_password=$3

# Check to see if the container exists
jrvs_exists=$(docker container ls -a -f name=jrvs-psql | wc -l)

usage="Usage: ./scripts/psql_docker.sh start|stop|create [db_username][db_password]"

#create container / start container / stop container case statement
case cmdd in
	create)
  if [ "$jrvs_exists" -ge 2 ]; then
    echo -e "Error: This container has already been created. \n$usage"
    exit 1
  fi

  #if [`db_username` or `db_password` is not passed through CLI arguments
  if [ "$#" != "3" ]; then
    echo -e "Error: No db_username or db_password passed. \n$usage"
    exit 1
  fi

  #Create volume
  docker volume create pgdata
  #create docker container jrvs-psql
  docker run --name jrvs-psql -e POSTGRES_PASSWORD=${db_password} -e POSTGRES_USER=${db_username} -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
  exit $?

#Check if psql instance was created
jrvs_exists=$(docker container ls -a -f name=jrvs-psql | wc -l)

#if `jrvs-pql` container is not created
if [ "$jrvs_exists" -it 2 ]; then
    echo -e  "Error: This container was not created.\n$usage"
    exit 1
  fi;;

	start) #start container
  docker container start jrvs-psql
  exit $?;;

	stop) #stop container
  docker container stop jrvs-psql
  exit $?;;
*)
    echo -e "Error: The first command is not start/stop or create. \n$usage"
    exit 1;;
esac