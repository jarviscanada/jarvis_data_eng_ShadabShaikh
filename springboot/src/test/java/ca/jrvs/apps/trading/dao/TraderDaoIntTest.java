package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.Trader;
import com.google.common.collect.Lists;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
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
public class TraderDaoIntTest {

  @Autowired
  private TraderDao traderDao;

  private Trader savedTrader;
  private Trader savedTrader2;

  @Before
  public void insertOne() throws Exception {
    savedTrader = new Trader();
    savedTrader.setFirst_name("John");
    savedTrader.setLast_name("Smith");
    savedTrader.setCountry("Canada");
    savedTrader.setDob(new Date(System.currentTimeMillis()));
    savedTrader.setId(1);
    savedTrader.setEmail("test@gmail.com");
    traderDao.save(savedTrader);

    savedTrader2 = new Trader();
    savedTrader2.setFirst_name("Usher");
    savedTrader2.setLast_name("Dale");
    savedTrader2.setCountry("Japan");
    savedTrader2.setDob(Date.valueOf("1980-05-12"));
    savedTrader2.setEmail("therealone@gmail.com");
    traderDao.save(savedTrader2);
  }

  @After
  public void deleteOne() throws Exception {
    traderDao.deleteAll();
  }

  @Test
  public void findAllById() {
    List<Trader> traders = Lists.
        newArrayList(traderDao.findAllById(Collections.singletonList(savedTrader.getId())));
    assertEquals(1,traders.size());
    assertEquals(1,traders.get(0).getId().intValue());
    assertEquals(savedTrader.getCountry(),traders.get(0).getCountry());
  }

  @Test
  public void counterTest() {
    long count = traderDao.count();
    assertEquals(2, count);
  }

  @Test
  public void findallTest() {
    List<Trader> allQuotes = (List<Trader>) traderDao.findAll();
    assertEquals(savedTrader.getId(), allQuotes.get(0).getId());
    assertEquals(savedTrader2.getId(), allQuotes.get(1).getId());
  }

  @Test
  public void deleteById() {
    traderDao.deleteById(2);
    long count = traderDao.count();
    assertEquals(1, count);
  }
}