/* Group hosts by CPU number and sort by their memory size in descending order */
SELECT
  cpu_number, host_id, total_mem
FROM
  ( SELECT
	cpu_number, id AS host_id, total_mem,
	rank() over( PARTITION BY cpu_number
        ORDER BY
          total_mem desc ) ordr
    FROM host_info
    GROUP BY cpu_number, id
  )as subquery;

/* Average used memory in percentage over 5 mins interval for each host */
SELECT hu.host_id, hi.hostname, (
   date_trunc('hour', hu.timestamp) + date_part('minute', hu.timestamp):: int / 5 * interval '5 min'
    ) AS timestamp2, avg(hi.total_mem - hu.memory_free) AS avg_used_mem_percentage
FROM host_usage hu
INNER JOIN host_info hi
ON hu.host_id = hi.id
GROUP BY timestamp2, hu.host_id, hi.hostname;

/* Detect host failure by querying host with less than 3 entries per 5 minute interval */
SELECT host_id,(
	date_trunc('hour', timestamp) + date_part('minute', timestamp):: int / 5 * interval '5 min') AS adj_timestamp, COUNT (*) AS num_data_points
FROM host_usage
GROUP BY host_id, adj_timestamp
HAVING COUNT(*) < 3;
