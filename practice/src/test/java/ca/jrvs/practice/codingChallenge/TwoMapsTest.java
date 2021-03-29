package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class TwoMapsTest {

  @Test
  public void compareMaps() {

    Map<String, String> asiaCapital1 = new HashMap<String, String>();
    asiaCapital1.put("Japan", "Tokyo");
    asiaCapital1.put("South Korea", "Seoul");

    Map<String, String> asiaCapital2 = new HashMap<String, String>();
    asiaCapital2.put("Japan", "Tokyo");
    asiaCapital2.put("South Korea", "Seoul");

    assertTrue(compareMaps(asiaCapital1, asiaCapital2));



  }

  private <K,V> boolean compareMaps(Map<K,V> m1, Map<K,V> m2){
    return m1.equals(m2);

  }
}