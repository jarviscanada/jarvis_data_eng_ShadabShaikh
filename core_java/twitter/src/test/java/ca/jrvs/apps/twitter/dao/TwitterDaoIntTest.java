package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetBuilder;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

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
    this.dao = new TwitterDao(httpHelper);
  }

  @Test
  public void create() {
    String hashTag = "#hash";
    String text = "@person2 message here 4 " + hashTag + " " + System.currentTimeMillis();
    Float lat = 1f;
    Float longt = -2f;
    Tweet postTweet = new Tweet();
    postTweet = TweetBuilder.buildTweet(text, longt, lat);

    Tweet tweet = dao.create(postTweet);

    assertNotNull(postTweet);

    assertEquals(text, tweet.getText());

    assertNotNull(tweet.getCoordinates());
    assertEquals(longt, tweet.getCoordinates().getCoordinates()[0], 0.01);
    assertEquals(lat, tweet.getCoordinates().getCoordinates()[1], 0.01);

    assertEquals("hash", tweet.getEntities().getHashtags()[0].getText());
  }

  @Test
  public void findById() {
    String id_str = "1378124119712096258";
    Tweet findTweet = dao.findById(id_str);
    System.out.println(findTweet.toString());
    assertNotNull(findTweet);

    assertEquals(id_str, findTweet.getId_str());
    assertEquals("Person", findTweet.getEntities().getUser_mentions()[0].getName());
    assertEquals(1f, findTweet.getCoordinates().getCoordinates()[1], 0.01);


  }

  @Test
  public void deleteById() {
    String deleteID = "1378124119712096258";
    System.out.println(deleteID);
    Tweet delTweet = dao.deleteById(deleteID);

    assertEquals(deleteID, delTweet.getId_str());
    assertEquals(1f, delTweet.getCoordinates().getCoordinates()[1], 0.01);

  }
}