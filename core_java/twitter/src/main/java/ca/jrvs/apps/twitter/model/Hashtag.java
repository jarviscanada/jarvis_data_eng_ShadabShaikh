package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Hashtag {

  private int[] index;
  private String text;

  public int[] getIndex() {
    return index;
  }

  public void setIndex(int[] index) {
    this.index = index;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return "Hashtag{" +
        "index=" + Arrays.toString(index) +
        ", text='" + text + '\'' +
        '}';
  }
}
