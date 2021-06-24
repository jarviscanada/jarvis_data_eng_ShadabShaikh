// Databricks notebook source
// MAGIC %md
// MAGIC # Retail Data Wrangling and Analytics

// COMMAND ----------

val file = "/FileStore/tables/retail.csv"

// COMMAND ----------

// Define Schema since default schema interprets certain fields wrong

import org.apache.spark.sql.types.{IntegerType,StringType,DoubleType,StructType, DateType,StructField}

val retailSchema = StructType(Array(
    StructField("invoice_no",StringType,true),
    StructField("stock_code",StringType,true),
    StructField("description",StringType,true),
    StructField("quantity", IntegerType, true),
    StructField("invoice_date", DateType, true),
    StructField("unit_price", DoubleType, true),
    StructField("customer_id", StringType, true),
    StructField("country", StringType, true)
  ))

// COMMAND ----------

//Read in csv
var df = spark.read.format("csv") 
  .option("header", "true") 
  .option("sep", ",") 
  .schema(retailSchema)
  .load(file)

// COMMAND ----------

display(df)

// COMMAND ----------

df.summary().show()

// COMMAND ----------

// Make table queryable by SQL through a Temporary View
df.createOrReplaceTempView("retail")

// COMMAND ----------

// MAGIC %sql
// MAGIC 
// MAGIC SELECT * FROM retail LIMIT(5)

// COMMAND ----------

// MAGIC %md
// MAGIC # Load CSV into Dataframe
// MAGIC Alternatively, the LGS IT team also dumped the transactional data into a [CSV file](https://raw.githubusercontent.com/jarviscanada/jarvis_data_eng_demo/feature/data/python_data_wrangling/data/online_retail_II.csv). However, the CSV header (column names) doesn't follow the snakecase or camelcase naming convention (e.g. `Customer ID` instead of `customer_id` or `CustomerID`). As a result, you will need to use Pandas to clean up the data before doing any analytics. In addition, unlike the PSQL scheme, CSV files do not have data types associated. Therefore, you will need to cast/convert certain columns into correct data types (e.g. DateTime, numbers, etc..)
// MAGIC 
// MAGIC **Data Preperation**
// MAGIC 
// MAGIC - Read the `data/online_retail_II.csv` file into a DataFrame
// MAGIC - Rename all columns to upper camelcase or snakecase

// COMMAND ----------

// MAGIC %md
// MAGIC # Total Invoice Amount Distribution

// COMMAND ----------

// Calculate Amount column

var inv_amount = df.withColumn("amount", $"quantity" * $"unit_price")

var amount_df = inv_amount
                  .filter($"amount" > 0)
                  .groupBy("invoice_no").sum("amount")



// COMMAND ----------

amount_df.summary().show()

// COMMAND ----------

// MAGIC %md
// MAGIC # Monthly Placed and Canceled Orders

// COMMAND ----------

import org.apache.spark.sql.functions._
//Prep Dataframe grouped by invoice and year.
var date_df = df
  .withColumn("YYYYMM", date_format($"invoice_date","yyyyMM"))
  .groupBy("invoice_no", "YYYYMM")
  .count()
  

// COMMAND ----------

//Cancelled orders
var cancelled_df = date_df
  .filter($"invoice_no".startsWith("C"))
  .groupBy("YYYYMM")
  .count()
  .withColumnRenamed("count", "Cancelled")
  .withColumnRenamed("YYYYMM", "CYYYYMM")
  .orderBy("CYYYYMM")


// COMMAND ----------

//Total orders
var total_orders_df = date_df
  .groupBy("YYYYMM")
  .count()
  .withColumnRenamed("count", "Total")
  .orderBy("YYYYMM")

var placed_df = total_orders_df
  .join(cancelled_df, total_orders_df("YYYYMM") === cancelled_df("CYYYYMM"))
  .withColumn("Placed", $"Total" - $"Cancelled" * 2)
  .drop("CYYYYMM")
  .orderBy("YYYYMM")

placed_df.show()

// COMMAND ----------

display(placed_df)

// COMMAND ----------

//Test random date
placed_df.filter($"YYYYMM" === 200912).show()

// COMMAND ----------

// MAGIC %md
// MAGIC # Monthly Sales

// COMMAND ----------

var monthly_sales = inv_amount
      .withColumn("YYYYMM", date_format($"invoice_date","yyyyMM"))
      .groupBy("YYYYMM")
      .sum("amount")
      .withColumnRenamed("sum(amount)", "monthly_sales")

monthly_sales.orderBy("YYYYMM").show(10)

// COMMAND ----------

display(monthly_sales.orderBy("YYYYMM"))

// COMMAND ----------

// MAGIC %md
// MAGIC # Monthly Sales Growth

// COMMAND ----------

import org.apache.spark.sql.expressions.Window

var window = Window.orderBy("YYYYMM")
val lagdf = monthly_sales.withColumn("delta", lag("monthly_sales", 1, 0).over(window))
val delta_df = lagdf
  .withColumn("Percentage Change", (lagdf("monthly_sales") - lagdf("delta"))/100)
  .select("YYYYMM", "Percentage Change")

display(delta_df)

// COMMAND ----------

// MAGIC %md
// MAGIC # Monthly Active Users

// COMMAND ----------

//Create a workable formatted dataframe with the date column in YYYYMM format

var retaildf = df
  .withColumn("YYYYMM", date_format($"invoice_date","yyyyMM"))

// COMMAND ----------

//Groupby YYYYMM with distinct customer ids

import org.apache.spark.sql.functions.countDistinct

var monthly_active_users = retaildf
    .groupBy("YYYYMM")
    .agg(countDistinct("customer_id"))



monthly_active_users.orderBy("YYYYMM").show(5)

// COMMAND ----------

display(monthly_active_users.orderBy("YYYYMM"))

// COMMAND ----------

// MAGIC %md
// MAGIC # New and Existing Users

// COMMAND ----------

// Monthly new users

var monthly_new_users_df = retaildf
    .groupBy("customer_id")
    .agg(min("YYYYMM"))
    .withColumnRenamed("min(YYYYMM)", "YYYYMM")
    .groupBy("YYYYMM")
    .count()
    .withColumnRenamed("count", "new")

// Monthly existing users

var monthly_returning_users_df = monthly_active_users
    .join(monthly_new_users_df, Seq("YYYYMM"),"inner")
    .withColumn("returning", $"count(customer_id)" - $"new")
    .drop("count(customer_id)")
    .drop("new")
    .orderBy("YYYYMM")

// Fix initial value (should be 0) 

var monthly_modified_returning = monthly_returning_users_df
  .filter(!($"YYYYMM" === 200912))

val initialvalue = Seq(("200912",0.0)).toDF("YYYYMM", "returning")
monthly_modified_returning = initialvalue.union(monthly_modified_returning)

//Join DFs for final DF

var newReturningCx = monthly_new_users_df
    .join(monthly_modified_returning, Seq("YYYYMM"),"inner")    

newReturningCx.show(20)

// COMMAND ----------

display(newReturningCx)

// COMMAND ----------

// MAGIC %md
// MAGIC ## Finding RFM
// MAGIC 
// MAGIC RFM is a method used for analyzing customer value. It is commonly used in database marketing and direct marketing and has received particular attention in the retail and professional services industries. ([wikipedia](https://en.wikipedia.org/wiki/RFM_(market_research)))
// MAGIC 
// MAGIC Optional Reading: [Making Your Database Pay Off Using Recency Frequency and Monetary Analysis](http://www.dbmarketing.com/2010/03/making-your-database-pay-off-using-recency-frequency-and-monetary-analysis/)
// MAGIC 
// MAGIC 
// MAGIC RFM stands for three dimensions:
// MAGIC 
// MAGIC - Recency – How recently did the customer purchase?
// MAGIC 
// MAGIC - Frequency – How often do they purchase?
// MAGIC 
// MAGIC - Monetary Value – How much do they spend?
// MAGIC 
// MAGIC Note: To simplify the problem, let's keep all placed and canceled orders.
// MAGIC 
// MAGIC 
// MAGIC **Sample RFM table**
// MAGIC 
// MAGIC ![](https://i.imgur.com/sXFIg6u.jpg)

// COMMAND ----------

//RFM Monetary

var rfm_monetary = inv_amount
    .groupBy("customer_id")
    .sum()
    .withColumn("sum(amount)", round(col("sum(amount)"), 2))
    .withColumnRenamed("sum(amount)", "monetary")
    .select($"customer_id", $"monetary")

rfm_monetary.show()

// COMMAND ----------

//RFM Frequency (number of invoices)

var rfm_freq = inv_amount
    .groupBy("customer_id")
    .agg(countDistinct("invoice_no"))
    .withColumnRenamed("count(invoice_no)", "frequency")

rfm_freq.show()

// COMMAND ----------

//RFM Recency (latest invoice date)

var recency_date = df
    .groupBy("customer_id")
    .agg(max("invoice_date"))
    .withColumnRenamed("max(invoice_date)", "recent_inv_date")

var rfm_recency = recency_date.select(
    $"customer_id",
    $"recent_inv_date",
    current_date().as("current_date"),
    datediff(current_date(),$"recent_inv_date").as("recency"))

rfm_recency.show(25)

// COMMAND ----------

//Join tables together

var rfm_df = rfm_recency
    .join(rfm_freq,"customer_id")
    .join(rfm_monetary,"customer_id")
    .drop("recent_inv_date")
    .drop("current_date")
    .orderBy("customer_id")

rfm_df.show(25)


// COMMAND ----------


