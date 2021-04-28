package ca.jrvs.apps.trading.model.domain;

public class Account implements Entity<Integer> {

  private int id;
  private int trader_id;
  private float amount;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  public int getTrader_id() {
    return trader_id;
  }

  public void setTrader_id(int trader_id) {
    this.trader_id = trader_id;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

}
