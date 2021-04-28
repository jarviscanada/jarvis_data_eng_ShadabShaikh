package ca.jrvs.apps.trading.model.domain;

public class Position  {

  private Integer account_Id;
  private String ticker;
  private Integer position;

  public Integer getAccount_Id() {
    return account_Id;
  }

  public void setAccount_Id(Integer account_Id) {
    this.account_Id = account_Id;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }
}
