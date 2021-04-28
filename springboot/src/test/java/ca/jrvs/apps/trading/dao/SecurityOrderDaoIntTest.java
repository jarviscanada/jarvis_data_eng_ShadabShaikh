package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import com.google.common.collect.Lists;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class SecurityOrderDaoIntTest {

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private TraderDao traderDao;

  @Autowired
  private SecurityOrderDao securityOrderDao;

  @Autowired
  private QuoteDao quoteDao;

  private Account savedAccount;
  private Account savedAccount2;

  private SecurityOrder savedSecurityOrder;
  private SecurityOrder savedSecurityOrder2;

  private Trader savedTrader;
  private Trader savedTrader2;

  private Quote savedQuote;

  @Before
  public void setUp() throws Exception {
    savedTrader = new Trader();
    savedTrader.setFirst_name("John");
    savedTrader.setLast_name("Smith");
    savedTrader.setCountry("Canada");
    savedTrader.setDob(new Date(System.currentTimeMillis()));
    savedTrader.setId(1);
    savedTrader.setEmail("test@gmail.com");
    traderDao.save(savedTrader);

    savedTrader2 = new Trader();
    savedTrader2.setFirst_name("Mary");
    savedTrader2.setLast_name("Smith");
    savedTrader2.setCountry("Canada");
    savedTrader2.setDob(new Date(System.currentTimeMillis()));
    savedTrader2.setId(2);
    savedTrader2.setEmail("test2@gmail.com");
    traderDao.save(savedTrader2);


    savedAccount = new Account();
    savedAccount.setTrader_id(1);
    savedAccount.setAmount(500.20);

    savedAccount2 = new Account();
    savedAccount2.setTrader_id(2);
    savedAccount2.setAmount(200.10);
    List<Account> accounts = new ArrayList<>();
    accounts.add(savedAccount);
    accounts.add(savedAccount2);
    accountDao.saveAll(accounts);

    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("AAPL");
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);

    savedSecurityOrder = new SecurityOrder();
    savedSecurityOrder.setAccount_id(1);
    savedSecurityOrder.setStatus("FILLED");
    savedSecurityOrder.setTicker("AAPL");
    savedSecurityOrder.setSize(2);
    savedSecurityOrder.setPrice(10.2);
    savedSecurityOrder.setNotes("apple");
    securityOrderDao.save(savedSecurityOrder);
  }

  @After
  public void tearDown() throws Exception {
    //Must be deleted in order due to constraints
    securityOrderDao.deleteAll();
    quoteDao.deleteAll();
    accountDao.deleteAll();
    traderDao.deleteAll();
  }

  @Test
  public void findAllById() {
    List<SecurityOrder> securityOrders = Lists
        .newArrayList(securityOrderDao.findAllById(Arrays.asList(savedSecurityOrder.getId())));
    assertEquals(1, securityOrders.size());
    assertEquals(savedSecurityOrder.getTicker(), securityOrders.get(0).getTicker());
  }

  @Test
  public void counterTest() {
    long count = securityOrderDao.count();
    assertEquals(1, count);
  }

  @Test
  public void findallTest() {
    List<SecurityOrder> allOrders = (List<SecurityOrder>) securityOrderDao.findAll();
    assertEquals(savedSecurityOrder.getSize(), allOrders.get(0).getSize());
    assertEquals(savedSecurityOrder.getNotes(), allOrders.get(0).getNotes());
  }

}