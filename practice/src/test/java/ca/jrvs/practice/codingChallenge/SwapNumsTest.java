package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class SwapNumsTest {

  SwapNums swapNums = new SwapNums();

  @Test
  public void swapNum() {
    int num1 = 2;
    int num2 = 4;

    int[] swappedPair = swapNums.swapNum(num1, num2);

    assertEquals(4, swappedPair[0]);
    assertEquals(2, swappedPair[1]);
    num1 = 3;
    num2 = 7;
    int[] swappedPair2 = swapNums.swapNumArith(num1, num2);
    assertEquals(7, swappedPair2[0]);
    assertEquals(3, swappedPair2[1]);
  }
}
