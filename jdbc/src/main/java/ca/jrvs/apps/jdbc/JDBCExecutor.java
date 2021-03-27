package ca.jrvs.apps.jdbc;


import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCExecutor {

  public static void main(String... args) {

    final Logger logger = LoggerFactory.getLogger(JDBCExecutor.class);

    DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
        "hplussport", "postgres", "password");
    try {
      Connection connection = dcm.getConnection();
      OrderDAO orderDAO = new OrderDAO(connection);
      Order order = orderDAO.findById(1000);
      System.out.println(order);
    } catch (SQLException e) {
      logger.error("Connection error or error inputting/retrieving information from orderDAO.", e);
    }
  }
}
