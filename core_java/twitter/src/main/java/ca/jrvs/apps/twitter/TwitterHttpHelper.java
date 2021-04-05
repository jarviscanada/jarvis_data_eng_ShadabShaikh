package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterHttpHelper implements HttpHelper {

  private OAuthConsumer consumer;
  private HttpClient httpClient;
  final Logger logger = LoggerFactory.getLogger(TwitterHttpHelper.class);

  public TwitterHttpHelper(String consumerKey, String consumerSecret, String accessToken, String tokenSecret) {
    consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    consumer.setTokenWithSecret(accessToken, tokenSecret);

    httpClient = HttpClientBuilder.create().build();
  }

  //Default constructor
  public TwitterHttpHelper() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    consumer.setTokenWithSecret(accessToken, tokenSecret);

    httpClient = HttpClientBuilder.create().build();
  }

  private void validate(Object request) {
    try {
      consumer.sign(request);
    } catch (OAuthExpectationFailedException e) {
      logger.error("Error: with HTTP header expectation.", e);
    } catch (OAuthMessageSignerException e) {
      logger.error("Error: with signature.", e);
    } catch (OAuthCommunicationException e) {
      logger.error("Error: OAuth communication. Recheck SSL certificates.", e);
    }
  }


  @Override
  public HttpResponse httpPost(URI uri) throws IOException {
    HttpPost request = new HttpPost(uri);
    validate(request);
    return httpClient.execute(request);
  }

  @Override
  public HttpResponse httpGet(URI uri) throws IOException {
    HttpGet httpGetRequest = new HttpGet(uri);
    validate(httpGetRequest);
    return httpClient.execute(httpGetRequest);
  }
/*
  public static void main(String[] args) throws Exception{
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
    HttpResponse response = httpHelper.httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=httphelper_tweet3"));
    System.out.println(EntityUtils.toString(response.getEntity()));

  }*/


}
