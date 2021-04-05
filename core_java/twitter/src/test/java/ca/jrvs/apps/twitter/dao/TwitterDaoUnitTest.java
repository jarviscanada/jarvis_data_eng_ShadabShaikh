package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonParser;
import ca.jrvs.apps.twitter.util.TweetBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

  @Mock
  HttpHelper mockHelper;

  @InjectMocks
  TwitterDao dao;

  //Make sample tweet JSON
  String tweetJson = "{\n"
      + "\"created_at\":\"Fri Apr 02 23:16:09 +0000 2021\",\n"
      + " \"id\":1378124119712096258,\n"
      + " \"id_str\":\"1378124119712096258\",\n"
      + " \"text\":\"@person message here #hash 1617405369222\",\n"
      + " \"entities\":{\n"
      + "     \"hashtags\":[],"
      + "     \"user_mentions\":[]"
      + " },\n"
      + " \"coordinates\":{\n"
      + "     \"coordinates\":[-2.0, 1.0],\n \"type\":\"Point\"},\n"
      + " \"retweet_count\":0,\n"
      + " \"favorite_count\":0,\n "
      + "\"favorited\":false,\n"
      + " \"retweeted\":false\n"
      + "}";


  @Test
  public void create() throws Exception {
    //test failed request
    String hashTag = "#hastag12";
    String text = "@person2 message here " + hashTag + " " + System.currentTimeMillis();
    Float lat = 1f;
    Float longt = -2f;
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("httpPost failure"));
    try {
      dao.create(TweetBuilder.buildTweet(text, longt, lat));
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    when(mockHelper.httpPost(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = JsonParser.toObjectFromJson(tweetJson, Tweet.class);
    //mock parseResponseBody
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());
    Tweet tweet = spyDao.create(TweetBuilder.buildTweet(text, longt, lat));
    assertNotNull(tweet);
    assertNotNull(tweet.getText());

  }

  @Test
  public void findById() throws Exception {

    String id_str = "1378124119712096258";
    when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("httpGet failure"));
    try {
      dao.findById(id_str);
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }

    verify(mockHelper).httpGet(isNotNull());

    when(mockHelper.httpGet(isNotNull())).thenReturn(null);
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = JsonParser.toObjectFromJson(tweetJson, Tweet.class);
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());

    Tweet findTweet = spyDao.findById(expectedTweet.getId_str());
    assertNotNull(findTweet);
    assertNotNull(findTweet.getText());
  }

  @Test
  public void deleteById() throws Exception {
    Tweet expectedTweet = JsonParser.toObjectFromJson(tweetJson, Tweet.class);
    TwitterDao spyDao = Mockito.spy(dao);
    //mock parseResponseBody
    doReturn(expectedTweet).when(spyDao).parseResponseBody(any(), anyInt());

    Tweet tweet = spyDao.deleteById(expectedTweet.getId_str());
    assertNotNull(tweet);
    assertNotNull(tweet.getText());

  }
}
