package ca.jrvs.practice.codingChallenge;

import java.util.Map;

public class TwoMaps {

  /**
   * Checks whether two maps have the same key/value pairs.
   * https://www.notion.so/jarviscanada/How-to-compare-two-maps-17cefd4114dc42d09ce800eae8957473
   * Big O notation: O(n) time to go through each key/value pair and then to run equals against the pairs.
   * @param m1
   * @param m2
   * @param <K>
   * @param <V>
   * @return
   */

  public <K,V> boolean compareMaps(Map<K,V> m1, Map<K,V> m2){
    return m1.equals(m2);

  }

}
