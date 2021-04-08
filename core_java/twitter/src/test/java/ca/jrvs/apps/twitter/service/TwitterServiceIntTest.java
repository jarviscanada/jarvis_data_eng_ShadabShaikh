package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetBuilder;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterServiceIntTest {

  Service service;

  private TwitterDao dao;

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
    CrdDao dao = new TwitterDao(httpHelper);
    this.service = new TwitterService(dao);
  }

  @Test
  public void postTweet() {
    String hashTag = "#hash";
    String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit,";
    String text = lorem + hashTag + " " + System.currentTimeMillis();
    //Test text length over 140 chars
    String text2 =
        lorem + lorem + lorem + lorem + lorem + hashTag + " " + System.currentTimeMillis();
    Float longt = -19f;
    Float lat = 72f;
    Tweet postTweet = new Tweet();
    postTweet = TweetBuilder.buildTweet(text, longt, lat);

    Tweet tweet = service.postTweet(postTweet);

    assertEquals(text, tweet.getText());

    assertNotNull(tweet.getCoordinates());
    assertEquals(longt, tweet.getCoordinates().getCoordinates()[0], 0.01);
    assertEquals(lat, tweet.getCoordinates().getCoordinates()[1], 0.01);

    assertEquals("hash", tweet.getEntities().getHashtags()[0].getText());

  }

  @Test
  public void showTweet() {
    String id_str = "1378124119712096258";
    Tweet findTweet = service.showTweet(id_str, null);
    System.out.println(findTweet.toString());
    assertNotNull(findTweet);

    assertEquals(id_str, findTweet.getId_str());
    assertEquals("Person", findTweet.getEntities().getUser_mentions()[0].getName());
    assertEquals(1f, findTweet.getCoordinates().getCoordinates()[1], 0.01);
  }

  @Test
  public void deleteTweets() {
    final int delNum = 2;
    String[] deleteID = {"1378185319594524675", "1378187944083722244"};
    List<Tweet> deletedTweets = service.deleteTweets(deleteID);

    for (int i = 0; i < delNum; i++) {
      Tweet thisTweet = deletedTweets.get(i);
      assertEquals(deleteID[i], thisTweet.getId_str());
      assertEquals(1f, thisTweet.getCoordinates().getCoordinates()[1], 0.01);
    }
  }
}