package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TwoSumTest {

  private TwoSum twoSumObj;
  private TwoSum twoSumObj2;

  @Before
  public void setUp() throws Exception {
    twoSumObj = new TwoSum();
    twoSumObj2 = new TwoSum();
  }

  @Test
  public void twoSum() {

    int[] testArray1 = {2, 7, 12, 15};
    int[] testArray2 = {14, 6, 3, 8, 11, 20, 4};

    int[] Array1 = {0, 1};
    int[] Array2 = {0, 5};


    assertArrayEquals(Array1, twoSumObj.twoSum(testArray1, 9));
    assertArrayEquals(Array2, twoSumObj.twoSum(testArray2, 19));

  }


}