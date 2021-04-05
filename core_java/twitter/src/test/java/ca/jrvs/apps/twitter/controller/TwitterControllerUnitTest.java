package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  @Mock
  Service service;

  @InjectMocks
  TwitterController controller;

  @Test
  public void postTweet() {
    when(service.postTweet(notNull()))
        .thenReturn(TweetBuilder.buildTweet("Welcome to the party", 18f, 32f));

    try {
      controller.postTweet(new String[]{"post", "text only"});
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    try {
      controller.postTweet(new String[]{"post", "text", "18:32:12"});
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    try {
      controller.postTweet(new String[]{"post", "text", "18:32:12"});
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    try {
      controller.postTweet(new String[]{"post", "text", ":32"});
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    Tweet tweet = controller.postTweet(new String[]{"post", "text", "18:32"});
    assertNotNull(tweet);
    assertNotNull(tweet.getText());
  }

  @Test
  public void showTweet() {
    when(service.showTweet(notNull(), any()))
        .thenReturn(TweetBuilder.buildTweet("Welcome to the party", 18f, 32f));

    try {
      controller.showTweet(new String[]{"show", "text only", "extra arg", "extra arg"});
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    try {
      controller.showTweet(new String[]{"show"});
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    Tweet findTweet = controller.showTweet(new String[]{"show", "01925712571212"});
    assertNotNull(findTweet);
    assertNotNull(findTweet.getText());

  }

  @Test
  public void deleteTweet() {
    final int delNum = 2;
    when(service.postTweet(notNull()))
        .thenReturn(TweetBuilder.buildTweet("text", 18f, 32f));
    String delId1 = "1378185319594524675";
    String delId2 = "1378187944083722244";
    String[] deleteIDs = {delId1, delId2};
    List<Tweet> deletedTweets = new ArrayList<>();
    Tweet tweet = controller.postTweet(new String[]{"post", "text", "18:32"});
    for (int i = 0; i < 3; i++) {
      deletedTweets.add(tweet);
    }
    when(service.deleteTweets(notNull())).thenReturn(deletedTweets);

    try {
      controller.deleteTweet(new String[]{"delete", "1378185319594524675", "1378185319594524675"});
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    controller.deleteTweet(new String[]{"delete", "1378185319594524675,1378185319594524675"})
        .forEach(streamTweet -> {
          assertNotNull(streamTweet);
          assertNotNull(streamTweet.getText());
          assertEquals(32, streamTweet.getCoordinates().getCoordinates()[1], 0.01);
        });

  }

}