package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.TweetBuilder;
import com.google.gdata.util.common.base.PercentEscaper;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterControllerIntTest {

  private TwitterService service;
  private TwitterController controller;

  @Before
  public void setUp() throws Exception {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
    //set up dependency
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    //pass dependency
    TwitterDao dao = new TwitterDao(httpHelper);
    service = new TwitterService(dao);
    this.controller = new TwitterController(service);

  }

  @Test
  public void postTweet() {
    String hashTag = "#hash";
    String text = "@person2 message here " + hashTag + " " + System.currentTimeMillis();
    String lat = "1";
    String longt = "-2";
    String args[] = new String[]{"post", text, longt + ":" + lat};

    Tweet postedTweet = controller.postTweet(args);


    assertNotNull(postedTweet);

    assertEquals(text, postedTweet.getText());

    assertNotNull(postedTweet.getCoordinates());
    assertEquals(Float.parseFloat(lat), postedTweet.getCoordinates().getCoordinates()[0], 0.01);
    assertEquals(Float.parseFloat(longt), postedTweet.getCoordinates().getCoordinates()[1], 0.01);

    assertEquals("hash", postedTweet .getEntities().getHashtags()[0].getText());

  }

  @Test
  public void showTweet() {
    String id_str = "1378124119712096258";
    String[] args = {"show", id_str};
    Tweet findTweet = controller.showTweet(args);


    assertNotNull(findTweet);

    assertEquals(id_str, findTweet.getId_str());
    assertEquals("Person", findTweet.getEntities().getUser_mentions()[0].getName());
    assertEquals(1f, findTweet.getCoordinates().getCoordinates()[1], 0.01);

  }

  @Test
  public void deleteTweet() {
    String deleteID1 = "1379084976608702475";
    String deleteID2 = "1378935246301892613";
    String[] deleteIDs = {"delete", deleteID1 + "," + deleteID2};
    List<Tweet> tweets = controller.deleteTweet(deleteIDs);

    Tweet deletedTweet1 = tweets.get(0);
    Tweet deletedTweet2 = tweets.get(1);
    assertEquals(deleteID1, deletedTweet1.getId_str());
    assertEquals(1f, deletedTweet2.getCoordinates().getCoordinates()[0], 0.01);
  }
}