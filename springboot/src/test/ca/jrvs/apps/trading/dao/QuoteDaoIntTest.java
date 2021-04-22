package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.hibernate.engine.jdbc.env.spi.QualifiedObjectNameFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes =  {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteDaoIntTest {

  @Autowired
  private QuoteDao quoteDao;

  private Quote savedQuote;
  private Quote savedQuote2;
  private Quote savedQuote3;

  @Before
  public void insertOne() {
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("appl");
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);

    savedQuote2.setAskPrice(10d);
    savedQuote2.setAskSize(10);
    savedQuote2.setBidPrice(10.2d);
    savedQuote2.setBidSize(10);
    savedQuote2.setId("appl");
    savedQuote2.setLastPrice(10.1d);
    //quoteDao.save(savedQuote2);
  }

  @Test
  public void quoteDaoIntTest() {

    assertEquals(savedQuote.getTicker(),quoteDao.findById("aapl").get().getTicker());

  }

  @After
  public void deleteOne() {
    quoteDao.deleteById(savedQuote.getId());
  }
}