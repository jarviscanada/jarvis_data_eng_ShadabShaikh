package ca.jrvs.apps.twitter.dao.helper;

import java.io.IOException;
import java.net.URI;
import org.apache.http.HttpResponse;

public interface HttpHelper {

  /**
   * Execute a HTTP Post call
   *
   * @param uri
   * @return
   */
  HttpResponse httpPost(URI uri) throws IOException;

  /**
   * Execute a HTTP Get call
   *
   * @param uri
   * @return
   */
  HttpResponse httpGet(URI uri) throws IOException;
}
