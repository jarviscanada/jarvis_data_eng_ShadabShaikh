package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetBuilder;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller {

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";

  private final Service service;

  @Autowired
  public TwitterController(Service service) {
    this.service = service;
  }

  @Override
  public Tweet postTweet(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException(
          "USAGE: TwitterCLIApp post \"tweet text\" \"latitude:longitude\"");
    }
    String text = args[1];
    String coordinates = args[2];
    String[] splitCoords = coordinates.split(COORD_SEP);
    if (splitCoords.length != 2 || text.isEmpty()) {
      throw new IllegalArgumentException("Please provide tweet text and the correct "
          + "number of coordinates.\nUsage:"
          + " TwitterAppCLI post \"text\" \"longitude:latitude\"");
    }

    float latitude, longitude;

    try {
      latitude = Float.parseFloat(splitCoords[0]);
      longitude = Float.parseFloat(splitCoords[1]);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Coordinates not valid.\nUsage: TwitterAppCLI post \"text\" \"longitude:latitude\"");
    }

    Tweet postableTweet = TweetBuilder.buildTweet(text, longitude, latitude);
    return service.postTweet(postableTweet);
  }

  @Override
  public Tweet showTweet(String[] args) {
    if (args.length < 2 || args.length > 3) {
      throw new IllegalArgumentException(
          "Too few or too many arguments. \nUSAGE: TwitterCLIApp show \"tweet_id\" \"[field1, field2]]\"");
    }
    String id = args[1];
    String[] splitFields = null;
    if (args.length == 3) {
      String fields = args[2];
      splitFields = fields.split(COMMA);
      return service.showTweet(id, splitFields);
    }
    return service.showTweet(id, null);
  }

  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException(
          "Invalid number of arguments. USAGE: TwitterCLIApp delete \"[id1,id2,..]\" ");
    }
    String ids = args[1];
    String[] splitIds = ids.split(COMMA);

    return service.deleteTweets(splitIds);
  }
}
