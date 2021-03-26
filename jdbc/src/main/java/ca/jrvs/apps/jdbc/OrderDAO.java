package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAO extends DataAccessObject<Order>{

  private static final String GET_ONE = "SELECT\n"
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
      + "where o.order_id = ?;";


  public OrderDAO(Connection connection) {
    super(connection);
  }

  @Override
  public Order findById(long id) {
    Order order = new Order();
    try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
      statement.setLong(1,id);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        order.setId(rs.getLong("order_id"));
        order.setCreationDate(rs.getDate("creation_date"));
        order.setTotal_due(rs.getFloat("total_due"));
        order.setStatus(rs.getString("status"));
        order.setSalespersonFirstName(rs.getString("sales_first_name"));
      }

    }catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    return null;
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
