# Introduction
This project aims to assist London Gift Shop's (LGS) marketing team to utilize data analytics to understand their customers and develop sales to increase profits. This project is a proof-of-concept for the LGS team to showcase customer shopping behaviour and aid in developing targeted marketing campaigns for brand engagement and increasing revenue generation. Python was used for development through Jupyter notebook to produce analytics and charts with Pandas Dataframes and Numpy as the core libraries for data exploration and assessment. The data was obtained through a provided sql database that was extracted from the LGS data warehouse (OLTP). A Postgres docker instance was initialized with this sql in order to provide data for the dataframe. The data was also provided as a hosted csv file which was obtained, cleaned and parsed before analysis. The work environment was set up using both Jupyter docker container with a bridge to the psql docker instance hosting the database. But ultimately through VSCode and the parsed CSV with the jupyter notebook for convenience and access to VSCode features. The python environment was a virtual environment made through anaconda with relevant data science libraries installed.


# Implementation
## Project Architecture
![Alt text](assets/python_project_architecture.jpg?raw=true "Title")    
The LGS IT team handles the front end and backend of their system under the Azure environment. Clients access the website from their browser which gets served by a CDN and webpages. Clients can make orders through this front-end and the data is then routed through the AKS Cluster and stored in the backend data warehouse which is the Azure SQL Server. Through ETL, that data is scrubbed of personal information and then provided to the Jarvis team as retail.sql or csv. The Jarvis Consulting team use a local postgres instance and a bridged jupyter docker containter to run queries against the database. The data is also ported to CSV and handled through the Pandas dataframe. 

## Data Analytics and Wrangling  
[Data analytics notebook](./retail_data_analytics_wrangling.ipynb)
- Monthly placed orders vs. canceled orders    
  Gain understanding on order placements through each month along with the patterns on cancelled orders. 
- Monthly sales and Monthly sales growth    
  Showcases the sales per month to target peak times or supplement lower sales times. Sales growth provides a month to month analysis on sales % for analysis on confirming working strategies
- Total monthly orders by new and existing customer    
  Showcases the monthly orders made with a breakdown of the types of customers making orders. Further analysis can be made on specific months to formulate marketing strategies that attracted more new customers or kept existing customers making orders
- Recency, Frequency and Monetary Analysis     
  Commonly used in database marketing and direct marketing for retail industries. Leverages data to identify and understand the different types of customers to better server their needs. Breaks down Monetary spending against frequency of sales (larger purchases) and recency of sales. The customer base is divided into parts based on the RFM score and tabulated with averages for each customer category. This allows the marketing team to target specific customer groups with campaigns such as a discounts or gifts. There can be separate campaigns for customers with lower recency values for better brand engagement. Customers with high RFM scores are highlighted in this table (champions) and campaigns can be made to ensure sales continuity.

# Improvements
- Optimize code for lesser memory/cpu impact and faster results
- Add prediction models for future sales figures
- Add more analysis metrics and models
