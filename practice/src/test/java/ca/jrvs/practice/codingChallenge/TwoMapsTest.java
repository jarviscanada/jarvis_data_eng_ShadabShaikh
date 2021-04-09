package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class TwoMapsTest {

  TwoMaps twoMaps;

  @Before
  public void setUp() throws Exception {
    twoMaps = new TwoMaps();
  }

  @Test
  public void compareMaps() {


    Map<String, String> asiaCapital1 = new HashMap<String, String>();
    asiaCapital1.put("Japan", "Tokyo");
    asiaCapital1.put("South Korea", "Seoul");

    Map<String, String> asiaCapital2 = new HashMap<String, String>();
    asiaCapital2.put("Japan", "Tokyo");
    asiaCapital2.put("South Korea", "Seoul");

    assertTrue(twoMaps.compareMaps(asiaCapital1, asiaCapital2));


  }
}