package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteDao implements CrudRepository<Quote, String> {

  private static final String TABLE_NAME = "quote";
  private static final String ID_COLUMN_NAME = "ticker";

  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public QuoteDao(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
    simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);

  }

  @Override
  public Quote save(Quote quote) {
    if (existsById(quote.getId())) {
      int updatedRowNo = updateOne(quote);
      if (updatedRowNo != 1) {
        throw new DataRetrievalFailureException("Unable to update quote");
      }
    } else {
      addOne(quote);
    }
    return quote;
  }

  //Helper method to save one quote
  private void addOne(Quote quote) {
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(quote);
    int row = simpleJdbcInsert.execute(parameterSource);
    if (row != 1) {
      throw new IncorrectResultSizeDataAccessException("Failed to insert", 1, row);
    }
  }

  //Helper method to update one quote
  private int updateOne(Quote quote) {
    String update_sql =
        "UPDATE quote SET last_price=?, bid_price=?, bid_size=?,ask_price=?, ask_size=? WHERE "
            + "ticker=?";
    return jdbcTemplate.update(update_sql, makeUpdateValues(quote));
  }

  //Helper method that makes sql update values objects
  private Object[] makeUpdateValues(Quote quote) {
    Object[] fields = {quote.getLastPrice(),
        quote.getBidPrice(),
        quote.getBidSize(),
        quote.getAskPrice(),
        quote.getAskSize(),
        quote.getId()};
    return fields;
  }


  @Override
  public <S extends Quote> List<S> saveAll(Iterable<S> quotes) {
    Iterator<S> saveQuotes = quotes.iterator();
    List<S> quoteList = new LinkedList<>();
    while (saveQuotes.hasNext()) {
      quoteList.add((S) save(saveQuotes.next()));
    }
    return quoteList;
  }

  @Override
  public Optional<Quote> findById(String ticker) {
    String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + " =?";
    try {
      Quote quote = jdbcTemplate
          .queryForObject(selectSql, BeanPropertyRowMapper.newInstance(Quote.class), ticker);
      return Optional.of(quote);
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Result size of SQL query was not as expected.", e);
      return Optional.empty();
    }

  }

  @Override
  public boolean existsById(String ticker) {
    String existSql = "SELECT COUNT(*) FROM " + TABLE_NAME
        + " WHERE ticker =?";
    int count = jdbcTemplate.queryForObject(existSql, Integer.class, ticker);
    return count > 0;
  }

  @Override
  public Iterable<Quote> findAll() {
    String selectSql = "SELECT * FROM " + TABLE_NAME;
    List<Quote> quotes = jdbcTemplate
        .query(selectSql, BeanPropertyRowMapper.newInstance(Quote.class));
    return quotes;
  }

  @Override
  public Iterable<Quote> findAllById(Iterable<String> tickers) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public long count() {
    String countString = "SELECT COUNT(*) FROM " + TABLE_NAME;
    Long count = jdbcTemplate.queryForObject(countString, Long.class);
    if (count == null) {
      throw new NullPointerException("SQL Count Null returned");
    }
    return count;
  }

  @Override
  public void deleteById(String ticker) {
    if (!findById(ticker).isPresent()) {
      throw new IllegalArgumentException("ID was not found.");
    }
    String deleteSql = "DELETE FROM " + TABLE_NAME + " WHERE ticker =?";
    jdbcTemplate.update(deleteSql, ticker);
  }

  @Override
  public void delete(Quote quote) {
    deleteById(quote.getId());
  }

  @Override
  public void deleteAll(Iterable<? extends Quote> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll() {
    String deleteSql = "DELETE FROM " + TABLE_NAME;
    jdbcTemplate.update(deleteSql);
  }
}
