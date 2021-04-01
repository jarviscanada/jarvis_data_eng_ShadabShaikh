package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import java.net.URI;
import sun.net.www.http.HttpClient;

public class TwitterHttpHelper implements HttpHelper {

  private OAuthConsumer consumer;
  private HttpClient httpClient;

  @Override
  public HttpResponse httpPost(URI uri) {
    return null;
  }

  @Override
  public HttpResponse httpGet(URI uri) {
    return null;
  }
}
