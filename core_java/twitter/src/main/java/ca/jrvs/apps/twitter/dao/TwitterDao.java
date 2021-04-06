package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String> {

  //URI constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";
  //URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";
  //Response code
  private static final int HTTP_OK = 200;
  final Logger logger = LoggerFactory.getLogger(TwitterDao.class);
  private final HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }


  @Override
  public Tweet create(Tweet tweet) {
    Tweet tweetResponse = null;
    String tweetText = tweet.getText();
    String longitude = String.valueOf(tweet.getCoordinates().getCoordinates()[0]);
    String latitude = String.valueOf(tweet.getCoordinates().getCoordinates()[1]);

    try {
      URI constructedURI = new URI(
          API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL + tweetText + AMPERSAND + "long"
              + EQUAL + longitude + AMPERSAND + "lat" + EQUAL + latitude);
      HttpResponse httpResponse = httpHelper.httpPost(constructedURI);

      tweetResponse = parseResponseBody(httpResponse, HTTP_OK);
    } catch (IOException e) {
      logger.error("Error: Communication with JSon parser or twitter API call.", e);
    } catch (URISyntaxException e) {
      logger.error("Error: URI syntax produces issues.", e);
    }
    return tweetResponse;
  }


  @Override
  public Tweet findById(String s) {
    Tweet tweetResponse = null;
    try {
      URI constructedURI = new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + s);
      HttpResponse httpResponse = httpHelper.httpGet(constructedURI);

      tweetResponse = parseResponseBody(httpResponse, HTTP_OK);
    } catch (IOException e) {
      logger.error("Error: Communication with JSon parser or twitter API call.", e);
    } catch (URISyntaxException e) {
      logger.error("Error: URI syntax produces issues.", e);
    }
    return tweetResponse;
  }

  @Override
  public Tweet deleteById(String s) {
    Tweet tweetResponse = null;
    try {
      URI deleteURI = new URI(API_BASE_URI + DELETE_PATH + "/" + s + ".json");
      HttpResponse httpResponse = httpHelper.httpPost(deleteURI);

      tweetResponse = parseResponseBody(httpResponse, HTTP_OK);
    } catch (IOException e) {
      logger.error("Error: Communication with JSon parser or twitter API call.", e);
    } catch (URISyntaxException e) {
      logger.error("Error: URI syntax produces issues.", e);
    }
    return tweetResponse;
  }

  protected Tweet parseResponseBody(HttpResponse response, Integer statusCode) {
    Tweet tweet = null;

    int responseStatusCode = response.getStatusLine().getStatusCode();

    if (responseStatusCode != statusCode) {
      throw new RuntimeException("Unexpected status: " + responseStatusCode);
    }
    if (response.getEntity() == null) {
      throw new RuntimeException("Response body empty");
    }

    //Convert response entity to String
    String jsonResponse;
    try {
      jsonResponse = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException("Cannot convert Entity to String");
    }

    //Convert JSON string to Tweet object
    try {
      tweet = JsonParser.toObjectFromJson(jsonResponse, Tweet.class);
    } catch (IOException e) {
      throw new RuntimeException("Cannot convert JSON string to object");
    }
    return tweet;
  }
}

