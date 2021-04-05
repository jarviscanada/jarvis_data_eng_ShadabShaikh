package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Coordinates {

  private float[] coordinates;
  private String type;

  public float[] getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(float[] coordinates) {
    this.coordinates = coordinates;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Coordinates{" +
        "coordinates=" + Arrays.toString(coordinates) +
        ", type='" + type + '\'' +
        '}';
  }
}
