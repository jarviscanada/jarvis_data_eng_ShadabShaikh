package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

  private Quote savedQuote;


  @Autowired
  private QuoteService quoteService;

  @Autowired
  private QuoteDao quoteDao;

  @Before
  public void insertOne() {
    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10);
    savedQuote.setId("FB");
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);
  }

  @Test
  public void findIexQuoteByTicker() {

    IexQuote iexQuote = quoteService.findIexQuoteByTicker("FB");
    assertNotNull(iexQuote);
    assertEquals("FB", iexQuote.getSymbol());
    String iexQuoteCompanyName = iexQuote.getCompanyName();
    assertEquals("Facebook Inc - Class A",iexQuoteCompanyName);
  }

  @Test
  public void updateMarketData() {
    //Alter price and test if updateMarketData modifies them
    quoteService.updateMarketData();
  }

  @Test
  public void saveQuote() {
    String ticker = "AAPL";
    Quote quote = quoteService.saveQuote(ticker);
    assertEquals(ticker, quoteDao.findById(ticker).get().getTicker());
  }

  @Test
  public void saveQuotes() {
    quoteDao.deleteAll();
    List<String> tickers = Arrays.asList("FB", "AAPL", "GOOG");
    List<Quote> savedQuotes = quoteService.saveQuotes(tickers);
    assertEquals("FB",quoteDao.findById(savedQuotes.get(0).getTicker()).get().getTicker());
  }


  @Test
  public void findAllQuotes() {
    quoteDao.deleteAll();
    List<String> tickers = Arrays.asList("FB", "AAPL", "GOOG");
    List<Quote> savedQuotes = quoteService.saveQuotes(tickers);
    List<Quote> findQuotes = quoteService.findAllQuotes();
    assertEquals(savedQuotes.get(1).getTicker(), findQuotes.get(1).getTicker());

  }

  @After
  public void tearDown() throws Exception {
    quoteDao.deleteAll();
  }
}