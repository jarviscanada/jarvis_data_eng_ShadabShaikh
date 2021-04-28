package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.TraderAccountView;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class TraderAccountServiceIntTest {

  private TraderAccountView traderAccountView;

  @Autowired
  private TraderAccountService traderAccountService;

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private TraderDao traderDao;

  private Trader savedTrader;
  private Trader savedTrader2;

  private TraderAccountView savedTraderAccountView;

  @Before
  public void setUp() throws Exception {
    savedTrader = new Trader();
    savedTrader.setFirst_name("John");
    savedTrader.setLast_name("Smith");
    savedTrader.setCountry("Canada");
    savedTrader.setDob(new Date(System.currentTimeMillis()));
    savedTrader.setId(1);
    savedTrader.setEmail("test@gmail.com");
    traderAccountService.createTraderAndAccount(savedTrader);
  }

  @After
  public void tearDown() throws Exception {
    Account delPrep = accountDao.findById(savedTrader.getId()).get();
    if(delPrep.getAmount() != 0){
      delPrep.setAmount(0d);
      accountDao.save(delPrep);
    }
    traderAccountService.deleteTraderById(savedTrader.getId());
  }

  @Test
  public void createTraderAndAccount() {
    savedTrader2 = new Trader();
    savedTrader2.setFirst_name("Mary");
    savedTrader2.setLast_name("Smith");
    savedTrader2.setDob(new Date(System.currentTimeMillis()));
    savedTrader2.setId(2);
    savedTrader2.setEmail("mary@gmail.com");
    //Empty value
    savedTrader2.setCountry("");

    //Empty value validation check
    try {
      traderAccountService.createTraderAndAccount(savedTrader2);
      fail("Error: trader account could not be validated");
    } catch (Exception e) {
      assertTrue(true);
    }

    savedTrader2.setCountry("Argentina");

    savedTraderAccountView = traderAccountService.createTraderAndAccount(savedTrader2);

    assertEquals(savedTrader2.getId(), savedTraderAccountView.getAccount_id());
    assertEquals(savedTrader2.getCountry(), savedTraderAccountView.getCountry());
  }

  @Test
  public void deleteTraderById() {
    assertNotNull(savedTrader.getEmail());
    traderAccountService.deleteTraderById(savedTrader.getId());
    assertFalse(traderDao.findById(savedTrader.getId()).isPresent());
    traderAccountService.createTraderAndAccount(savedTrader);
  }

  @Test
  public void deposit() {
    //Deposit 100 dollars
    traderAccountService.deposit(savedTrader.getId(), 100d);
    assertEquals(100d, accountDao.findById(savedTrader.getId()).get().getAmount(), 0.001);
    try {
      traderAccountService.deposit(savedTrader.getId(), -200d);
      fail();
    } catch (Exception e) {
      assertTrue(true);
    }
  }

  @Test
  public void withdraw() {
    //Deposit 300 dollars
    traderAccountService.deposit(savedTrader.getId(), 300d);
    traderAccountService.withdraw(savedTrader.getId(),100d);

    assertEquals(200d, accountDao.findById(savedTrader.getId()).get().getAmount(),0.001);

    try {
      traderAccountService.withdraw(savedTrader.getId(), 400d);
      fail();
    } catch (Exception e) {
      assertTrue(true);
    }
  }
}