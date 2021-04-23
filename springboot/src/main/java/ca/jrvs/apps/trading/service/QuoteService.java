package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.controller.ResponseExceptionUtil;
import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class QuoteService {

  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

  private QuoteDao quoteDao;
  private MarketDataDao marketDataDao;

  @Autowired
  public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
    this.quoteDao = quoteDao;
    this.marketDataDao = marketDataDao;
  }

  /**
   * Update quote table against IEX source -get all quotes from the db foreach ticker get iexQuote
   * -convert iexQuote to quote entity -persist quote to db
   *
   * @throws org.springframework.dao.DataAccessException if unable to retrieve data
   * @throws IllegalArgumentException                    for invalid input
   */

  public void updateMarketData() {
    List<Quote> dbQuotes = findAllQuotes();
    try {
      dbQuotes.stream().forEach(q -> {
        IexQuote iexQuote = marketDataDao.findById(q.getId()).get();
        Quote convertedQuote = buildQuotefromIexQuote(iexQuote);
        quoteDao.save(convertedQuote);
      });
    } catch (IllegalArgumentException e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    } catch (DataRetrievalFailureException e) {
      throw new InvalidDataAccessResourceUsageException("Cannot retrieve data.");
    }
  }

  /**
   * Helper method. Map a IexQuote to a Quote entity. NoteL: `iexQuote.getLatestPrice() == null` if
   * the stock market is closed. Make sure set a default value for number field(s).
   */

  protected static Quote buildQuotefromIexQuote(IexQuote iexQuote) {
    Quote convertedQuote = new Quote();
    convertedQuote.setId(iexQuote.getSymbol());
    convertedQuote.setLastPrice(iexQuote.getLatestPrice());
    convertedQuote.setBidPrice(iexQuote.getIexBidPrice());
    convertedQuote.setBidSize(iexQuote.getIexBidSize());
    convertedQuote.setAskPrice(iexQuote.getIexAskPrice());
    convertedQuote.setAskSize(iexQuote.getIexAskSize());
    return convertedQuote;
  }

  /**
   * Validate (against IEX) and save given tickers to quote table.
   * <p>
   * Get iexQuotes Convert each iexQuote to Quote entity persist the quote to db
   *
   * @param tickers
   * @return
   * @throws IllegalArgumentException if ticker is not found from IEX
   */

  public List<Quote> saveQuotes(List<String> tickers) {
    List<Quote> savedList = new ArrayList<Quote>();
    try {
      tickers.stream().forEach(t -> savedList.add(saveQuote(t)));
    } catch (IllegalArgumentException ex) {
      logger.error("Error: Ticker not found from IEX", ex);
    }
    return savedList;
  }

  /**
   * Helped method
   */

  public Quote saveQuote(String ticker) {
    IexQuote iexQuote = marketDataDao.findById(ticker).get();
    Quote convertedQuote = buildQuotefromIexQuote(iexQuote);
    return quoteDao.save(convertedQuote);
  }

  /**
   * Find and IexQuote
   *
   * @param ticker id
   * @return IexQuote object
   * @throws IllegalArgumentException if ticker is invalid
   */
  public IexQuote findIexQuoteByTicker(String ticker) {
    return marketDataDao.findById(ticker)
        .orElseThrow(() -> new IllegalArgumentException(ticker + " is invalid"));
  }

  /**
   * Update a given quote to the quote table without validation
   *
   * @param quote entity
   * @return
   */
  public Quote saveQuote(Quote quote) {
    return quoteDao.save(quote);
  }

  /**
   * Find all quotes from the quote table
   *
   * @return
   */
  public List<Quote> findAllQuotes() {
    List<Quote> result = new ArrayList<Quote>();
    Iterable<Quote> iterable = quoteDao.findAll();
    iterable.forEach(result::add);
    return result;
  }
}
