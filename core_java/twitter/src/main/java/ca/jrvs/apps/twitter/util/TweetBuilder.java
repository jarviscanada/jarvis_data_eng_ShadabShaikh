package ca.jrvs.apps.twitter.util;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import com.google.gdata.util.common.base.PercentEscaper;
import java.util.Arrays;

public class TweetBuilder {

  public static Tweet buildTweet (String text, float longt, float lat) {
    Tweet tweet = new Tweet();
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    Coordinates coordinates = new Coordinates();
    coordinates.setCoordinates(new float[]{longt, lat});
    coordinates.setType("Point");

    tweet.setText(percentEscaper.escape(text));
    tweet.setCoordinates(coordinates);
    return tweet;
  }

}
