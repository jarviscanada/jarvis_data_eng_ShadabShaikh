package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class TwitterService implements Service {

  private final CrdDao dao;

  @Autowired
  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }

  @Override
  public Tweet postTweet(Tweet tweet) {
    if (tweet.getText().length() > 140 || tweet.getText().length() < 1) {
      throw new IllegalArgumentException("Text length cannot be blank or exceed 140 characters.");
    }
    if (tweet.getCoordinates().getCoordinates()[0] < -180
        || tweet.getCoordinates().getCoordinates()[0] > 180) {
      throw new IllegalArgumentException("Longitude value invalid. Must be between -180 and +180");
    }
    if (tweet.getCoordinates().getCoordinates()[1] < -90f
        || tweet.getCoordinates().getCoordinates()[1] > 90f) {
      throw new IllegalArgumentException("Latitude value invalid. Must be between -90 and +90");
    }

    return (Tweet) dao.create(tweet);
  }

  @Override
  public Tweet showTweet(String id, String[] fields) {
    if (!validateID(id)) {
      throw new IllegalArgumentException("Status ID must only contain numbers");
    }
    return (Tweet) dao.findById(id);
  }

  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List<Tweet> deletedTweets = new ArrayList<Tweet>();
    // Displaying elements in Stream
    Arrays.stream(ids)
        .forEach(
            id -> {
              if (!validateID(id)) {
                throw new IllegalArgumentException("Status ID:" + id + "must only contain numbers");
              }
              deletedTweets.add((Tweet) dao.deleteById(id));
            });
    return deletedTweets;
  }

  public boolean validateID(String id) {
    return id.matches("[0-9]+");
  }

}
