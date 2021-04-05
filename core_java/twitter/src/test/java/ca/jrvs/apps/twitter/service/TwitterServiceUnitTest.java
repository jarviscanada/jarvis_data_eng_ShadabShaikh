package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetBuilder;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

  @Mock
  CrdDao dao;

  @InjectMocks
  TwitterService service;

  @Test
  public void postTweet() {
    when(dao.create(any())).thenReturn(TweetBuilder.buildTweet("Welcome", 18f, 32f));

    try {
      service.postTweet(TweetBuilder.buildTweet("bad tweet", 188f, 321f));
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    Tweet tweet = service.postTweet(TweetBuilder.buildTweet("Welcome", 18f, 32f));
    assertNotNull(tweet);
    assertNotNull(tweet.getText());


  }

  @Test
  public void showTweet() {
    when(dao.findById(any())).thenReturn(TweetBuilder.buildTweet("Welcome", 18f, 32f));

    try {
      service.showTweet("1378124119712f096258", null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    Tweet displayedTweet = service.showTweet("1378124119712096258", null);
    assertEquals("Welcome", displayedTweet.getText());
    assertEquals(18f, displayedTweet.getCoordinates().getCoordinates()[0], 0.01);
    assertEquals(32f, displayedTweet.getCoordinates().getCoordinates()[1], 0.01);
  }

  @Test
  public void deleteTweets() {
    int delNum = 2;
    String[] goodDeleteID = {"1378185319594524675", "1378187944083722244"};
    String[] badDeleteID = {"1378185319594f524675", "13781879440g83722244"};
    when(dao.deleteById(any())).thenReturn(TweetBuilder.buildTweet("Welcome", 18f, 32f));

    try {
      service.deleteTweets(badDeleteID);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    List<Tweet> deletedTweets = service.deleteTweets(goodDeleteID);
    Tweet deletedTweet = deletedTweets.get(0);
    assertEquals("Welcome", deletedTweet.getText());
    assertEquals(18f, deletedTweet.getCoordinates().getCoordinates()[0], 0.01);
    assertEquals(32f, deletedTweet.getCoordinates().getCoordinates()[1], 0.01);


  }
}