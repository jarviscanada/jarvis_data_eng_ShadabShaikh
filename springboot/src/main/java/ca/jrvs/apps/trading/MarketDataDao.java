package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.util.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * MarketDataDao gets quotes from IEX
 */

@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {

  private static final String IEX_BATCH_PATH = "/stock/market/batch?symbols=%s&types=quote&token=";
  private final String IEX_BATCH_URL;

  private final Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
  private final HttpClientConnectionManager httpClientConnectionManager;
  private HttpClient httpClient;
  private static final int HTTP_OK = 200;
  private static final int HTTP_ERROR = 404;

  @Autowired
  public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
      MarketDataConfig marketDataConfig) {
    this.httpClientConnectionManager = httpClientConnectionManager;
    IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
  }

  /**
   * Execute a get and return http entity/body as a string
   * <p>
   * Uses EntityUtils.toString to process HTTP entity
   *
   * @param url resource URL
   * @return http response boy or Optional.empty for 404 response
   * @throws DataRetrievalFailureException if HTTP failed or status code is unexpected
   */

  private Optional<String> executeHttpGet(String url) {
    HttpGet httpGetRequest = new HttpGet(URI.create(url));
    HttpResponse httpResponse;
    try {
      httpResponse = getHttpClient().execute(httpGetRequest);
    } catch (IOException e) {
      throw new DataRetrievalFailureException("Error obtaining httpGet response", e);
    }

    int statusCode = httpResponse.getStatusLine().getStatusCode();

    if (statusCode == HTTP_ERROR) {
      return Optional.empty();
    } else if (statusCode == HTTP_OK) {
      try {
        return Optional.of(EntityUtils.toString(httpResponse.getEntity()));
      } catch (IOException e) {
        throw new DataRetrievalFailureException("Cannot read response", e);
      }
    } else {
      throw new DataRetrievalFailureException(
          "The status of the response is " + statusCode);
    }
  }


  /**
   * Borrow a HTTP client from the httpClientConnectionManager
   *
   * @return a httpClient
   */

  private CloseableHttpClient getHttpClient() {
    return HttpClients.custom()
        .setConnectionManager(httpClientConnectionManager)
        //prevent connectionManager shutdown when calling httpClient.close()
        .setConnectionManagerShared(true)
        .build();
  }

  @Override
  public Optional<IexQuote> findById(String ticker) {
    Optional<IexQuote> iexQuote;
    List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

    if (quotes.size() == 0) {
      return Optional.empty();
    } else if (quotes.size() == 1) {
      iexQuote = Optional.of(quotes.get(0));
    } else {
      throw new DataRetrievalFailureException("Unexpected Number of quotes");
    }
    return iexQuote;
  }


  @Override
  public List<IexQuote> findAllById(Iterable<String> tickers) {

    //Validate tickers
    tickers.forEach(ticker -> {
      if (ticker.length() > 5 || ticker.matches(".*\\d+.*")) {
        throw new IllegalArgumentException(
            "Error: Symbols cannot be more than 5 characters or contain digits.");
      }
    });

    String symbols = String.join(",", tickers);
    String url = String.format(IEX_BATCH_URL, symbols);

    //HTTP response
    String response = executeHttpGet(url)
        .orElseThrow(() -> new IllegalArgumentException("Invalid ticker"));

    //Array of JSON documents
    JSONObject IexQuotesJson = new JSONObject(response);

    //Get number of documents
    if (IexQuotesJson.length() == 0) {
      throw new IllegalArgumentException("Invalid ticker");
    }

    List<IexQuote> iexQuotes = new ArrayList<>();

    for (String ticker : tickers) {
      try {
        iexQuotes.add(JsonParser
            .toObjectFromJson(IexQuotesJson.getJSONObject(ticker).getJSONObject("quote").toString(),
                IexQuote.class));
      } catch (IOException e) {
        throw new RuntimeException("Error reading response.", e);
      }
    }
    return iexQuotes;
  }

  @Override
  public boolean existsById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Iterable<IexQuote> findAll() {
    throw new UnsupportedOperationException("Not implemented");
  }


  @Override
  public long count() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteById(String s) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(IexQuote entity) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends IexQuote> entities) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <S extends IexQuote> S save(S entity) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> entities) {
    throw new UnsupportedOperationException("Not implemented");
  }

}
