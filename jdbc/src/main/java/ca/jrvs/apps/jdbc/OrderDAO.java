package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderDAO extends DataAccessObject<Order> {

  final Logger logger = LoggerFactory.getLogger(OrderDAO.class);

  private static final String GET_ID = "SELECT\n"
      + "  c.first_name, c.last_name, c.email, o.order_id,\n"
      + "  o.creation_date, o.total_due, o.status,\n"
      + "  s.first_name, s.last_name, s.email,\n"
      + "  ol.quantity,\n"
      + "  p.code, p.name, p.size, p.variety, p.price\n"
      + "from orders o\n"
      + "  join customer c on o.customer_id = c.customer_id\n"
      + "  join salesperson s on o.salesperson_id=s.salesperson_id\n"
      + "  join order_item ol on ol.order_id=o.order_id\n"
      + "  join product p on ol.product_id = p.product_id\n"
      + "where o.order_id = ?";


  public OrderDAO(Connection connection) {
    super(connection);
  }

  @Override
  public Order findById(long id) {
    Order order = new Order();
    try (PreparedStatement statement = this.connection.prepareStatement(GET_ID)) {
      statement.setLong(1, id);
      ResultSet rs = statement.executeQuery();
      boolean runOnceFlag = false; //Run customer, sales information once during while loop for rest of the orders
      List<OrderLine> orderLines = new ArrayList<OrderLine>();
      while (rs.next()) {
        if (runOnceFlag == false) {
          order.setCustomerFirstName(rs.getString(1));
          order.setCustomerLastName(rs.getString(2));
          order.setCustomerEmail(rs.getString(3));
          order.setId(rs.getLong(4));
          // orderId = order.getId();
          order.setCreationDate(rs.getDate(5));
          order.setTotal_due(rs.getFloat(6));
          order.setStatus(rs.getString(7));
          order.setSalespersonFirstName(rs.getString(8));
          order.setSalespersonLastName(rs.getString(9));
          order.setSalespersonEmail(rs.getString(10));
          runOnceFlag = true;
        }
        OrderLine orderLine = new OrderLine(); //Initialize OrderLine object

        orderLine.setQuantity(rs.getInt(11));
        orderLine.setProductCode(rs.getString(12));
        orderLine.setProductName(rs.getString(13));
        orderLine.setProductSize(rs.getInt(14));
        orderLine.setProductVariety(rs.getString(15));
        orderLine.setProductPrice(rs.getFloat(16));
        orderLines.add(orderLine);
      }
      order.setOrderLines(orderLines);
    } catch (SQLException e) {
      logger.error("Prepared Statement or SQL execution with result set had issues.", e);
    }
    return order;
  }

  @Override
  public List<Order> findAll() {
    return null;
  }

  @Override
  public Order update(Order dto) {
    return null;
  }

  @Override
  public Order create(Order dto) {
    return null;
  }

  @Override
  public void delete(long id) {

  }
}
