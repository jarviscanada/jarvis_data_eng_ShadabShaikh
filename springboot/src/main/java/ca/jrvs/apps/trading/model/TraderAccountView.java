package ca.jrvs.apps.trading.model;

import java.sql.Date;

public class TraderAccountView {

  private Integer trader_id;
  private String first_name;
  private String last_name;
  private Date dob;
  private String country;
  private String email;

  private Integer account_id;
  private Double amount;

  public TraderAccountView(Integer trader_id, String first_name, String last_name,
      Date dob, String country, String email, Integer account_id, Double amount) {
    this.trader_id = trader_id;
    this.first_name = first_name;
    this.last_name = last_name;
    this.dob = dob;
    this.country = country;
    this.email = email;
    this.account_id = account_id;
    this.amount = amount;
  }

  public Integer getTrader_id() {
    return trader_id;
  }

  public String getFirst_name() {
    return first_name;
  }

  public String getLast_name() {
    return last_name;
  }

  public Date getDob() {
    return dob;
  }

  public String getCountry() {
    return country;
  }

  public String getEmail() {
    return email;
  }

  public Integer getAccount_id() {
    return account_id;
  }

  public Double getAmount() {
    return amount;
  }
}
