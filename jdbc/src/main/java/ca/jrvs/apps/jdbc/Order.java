package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataTransferObject;
import java.util.Date;

public class Order implements DataTransferObject{

  private long id;
  private Date creationDate;
  private float total_due;
  private String status;
  private String salespersonFirstName;
  private String salespersonLastName;
  private String salespersonEmail;
  private String Quantity;
 /* private String code;
  private String productName;
  private String productSize;
  private String productVariety;
  private String productVariety; */


  @Override
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public float getTotal_due() {
    return total_due;
  }

  public void setTotal_due(float total_due) {
    this.total_due = total_due;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSalespersonFirstName() {
    return salespersonFirstName;
  }

  public void setSalespersonFirstName(String salespersonFirstName) {
    this.salespersonFirstName = salespersonFirstName;
  }

  public String getSalespersonLastName() {
    return salespersonLastName;
  }

  public void setSalespersonLastName(String salespersonLastName) {
    this.salespersonLastName = salespersonLastName;
  }

  public String getSalespersonEmail() {
    return salespersonEmail;
  }

  public void setSalespersonEmail(String salespersonEmail) {
    this.salespersonEmail = salespersonEmail;
  }

  public String getQuantity() {
    return Quantity;
  }

  public void setQuantity(String quantity) {
    Quantity = quantity;
  }
}
