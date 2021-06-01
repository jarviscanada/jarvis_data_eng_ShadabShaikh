-- Show table schema 
\d+ retail;

-- Show first 10 rows
select *
from retail r 
limit 10;

-- Check # of records
select count(*) 
from retail r;

-- number of clients (e.g. unique client ID)
select count(distinct customer_id)
from retail r;

-- invoice date range
select MIN(invoice_date), max(invoice_date) 
from retail r;

-- number of SKU/merchants
select count(distinct stock_code) 
from retail r;

--Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)
select avg(invoice_total) as average_invoice_cost
from (select sum(unit_price * quantity) as invoice_total 
from retail r 
group by invoice_no 
having sum(unit_price * quantity) > 0) as invoice;

-- Calculate total revenue (e.g. sum of unit_price * quantity)
select sum(unit_price * quantity)
from retail r;

-- Calculate total revenue by YYYYMM 
select to_char(invoice_date, 'YYYYMM') AS invoice_month, sum(unit_price * quantity)
from retail r
group by to_char(invoice_date, 'YYYYMM')
order by invoice_month asc ;