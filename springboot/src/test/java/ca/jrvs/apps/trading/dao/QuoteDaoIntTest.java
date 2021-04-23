package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
public class QuoteDaoIntTest {

  @Autowired
  private QuoteDao quoteDao;

  private Quote savedQuote;
  private Quote savedQuote2;
  private Quote savedQuote3;

  @Before
  public void insertOne() {
    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("AAPL");
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);

    savedQuote2 = new Quote();
    savedQuote2.setAskPrice(10d);
    savedQuote2.setAskSize(10);
    savedQuote2.setBidPrice(10.2d);
    savedQuote2.setBidSize(10);
    savedQuote2.setId("FB");
    savedQuote2.setLastPrice(10.1d);
    quoteDao.save(savedQuote2);

    savedQuote3 = new Quote();
    savedQuote3.setAskPrice(11d);
    savedQuote3.setAskSize(11);
    savedQuote3.setBidPrice(11.2d);
    savedQuote3.setBidSize(11);
    savedQuote3.setId("AMD");
    savedQuote3.setLastPrice(11.1d);
  }

  @Test
  public void quoteDaoIntTest() {

    boolean exists = quoteDao.existsById("AAPL");
    assertTrue(exists);

    long count = quoteDao.count();
    assertEquals(2, count);

    List<Quote> allQuotes = (List<Quote>) quoteDao.findAll();
    assertEquals(savedQuote.getTicker(), allQuotes.get(0).getTicker());
    assertEquals(savedQuote2.getTicker(), allQuotes.get(1).getTicker());

    savedQuote.setBidPrice(11.2d);
    quoteDao.save(savedQuote);
    assertEquals(savedQuote.getBidPrice(),quoteDao.findById("AAPL").get().getBidPrice());

    quoteDao.saveAll(Arrays.asList(savedQuote, savedQuote, savedQuote3));

    assertEquals(savedQuote3.getTicker(), quoteDao.findById("AMD").get().getTicker());

    Quote foundQuote = quoteDao.findById("AAPL").get();
    assertThat(savedQuote).isEqualToComparingFieldByField(foundQuote);

    quoteDao.deleteById(savedQuote.getId());
    quoteDao.delete(savedQuote2);
    try {
      quoteDao.deleteById(savedQuote.getId());
      fail("Exception not thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("ID was not found.", e.getMessage());
    }
  }

  @After
  public void delete() {
    quoteDao.deleteAll();
  }
}