package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
import java.util.ArrayList;
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
public class PositionDaoIntTest {


  @Autowired
  private AccountDao accountDao;

  @Autowired
  private TraderDao traderDao;

  @Autowired
  private SecurityOrderDao securityOrderDao;

  @Autowired
  private QuoteDao quoteDao;

  @Autowired
  private PositionDao positionDao;

  private Account savedAccount;
  private Account savedAccount2;

  private SecurityOrder savedSecurityOrder;
  private SecurityOrder savedSecurityOrder2;

  private Trader savedTrader;
  private Trader savedTrader2;

  private Quote savedQuote;
  private Quote savedQuote2;

  private Position savedPosition;

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
    savedAccount.setAmount(500.20d);

    savedAccount2 = new Account();
    savedAccount2.setTrader_id(2);
    savedAccount2.setAmount(200.10d);
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

    savedQuote2 = new Quote();
    savedQuote2.setAskPrice(10.6d);
    savedQuote2.setAskSize(10);
    savedQuote2.setBidPrice(15.2d);
    savedQuote2.setBidSize(10);
    savedQuote2.setId("FB");
    savedQuote2.setLastPrice(10.1d);
    quoteDao.save(savedQuote2);

    savedSecurityOrder = new SecurityOrder();
    savedSecurityOrder.setAccount_id(1);
    savedSecurityOrder.setStatus("FILLED");
    savedSecurityOrder.setTicker("AAPL");
    savedSecurityOrder.setSize(2);
    savedSecurityOrder.setPrice(10.2);
    savedSecurityOrder.setNotes("apple");
    securityOrderDao.save(savedSecurityOrder);

    savedSecurityOrder2 = new SecurityOrder();
    savedSecurityOrder2.setAccount_id(2);
    savedSecurityOrder2.setStatus("FILLED");
    savedSecurityOrder2.setTicker("FB");
    savedSecurityOrder2.setSize(3);
    savedSecurityOrder2.setPrice(15.7);
    savedSecurityOrder2.setNotes("apple");
    securityOrderDao.save(savedSecurityOrder2);

    savedPosition = new Position();
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
  public void existsById() {

  }

  @Test
  public void findById() {
    Position position = positionDao.findById(savedSecurityOrder.getId()).get();
    assertEquals(savedSecurityOrder.getTicker(), position.getTicker());
  }

  @Test
  public void findAllById() {
    List<Integer> ids = new ArrayList<Integer>();
    ids.add(savedSecurityOrder.getId());

    assertEquals(savedSecurityOrder.getTicker(), positionDao.findAllById(ids).get(0).getTicker());
  }

  @Test
  public void findAll() {
    List<Position> positions = (List<Position>) positionDao.findAll();

    assertEquals(2, positions.size());
    assertEquals(savedSecurityOrder.getSize(), positions.get(0).getPosition(), 0.001);

  }

  @Test
  public void count() {
    assertEquals(2,positionDao.count());
  }
}