#! /bin/bash
# Script usage
#./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
# Example
#./scripts/host_info.sh "localhost" 5432 "host_agent" "postgres" "mypassword"
usage="./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password"

#validate arguments
if [ "$#" -ne 5 ]; then
    echo -e "Error: Illegal number of parameters \n$usage"
    exit 1
fi

#setup arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
export PGPASSWORD=$5

#save hostname to a variable and prepare data for parsing with lscpu
hostname=$(hostname -f)
lscpu_out=`lscpu`

#generate host info from cpu specs readied above
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model name:" | cut -d ":" -f 2 | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "^CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out"  | egrep "^L2 cache:" | awk '{print $3}' | sed 's/K//' | xargs)
total_mem=$(cat /proc/meminfo | egrep "^MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(date "+%Y-%m-%d %H:%M:%S")

#Insert data into tables
insert_statement="INSERT INTO host_info (hostname,cpu_number,cpu_architecture,cpu_model,cpu_mhz,L2_cache,total_mem,timestamp)
                        VALUES('$hostname','$cpu_number','$cpu_architecture','$cpu_model','$cpu_mhz','$l2_cache','$total_mem','$timestamp');"

psql -h $psql_host -U $psql_user -d $db_name -p $psql_port -c "$insert_statement"
exit $?

