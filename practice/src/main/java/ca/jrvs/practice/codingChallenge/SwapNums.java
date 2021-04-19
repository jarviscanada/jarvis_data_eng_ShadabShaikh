package ca.jrvs.practice.codingChallenge;

/**
 * Swap input numbers without using a third variable.
 * https://www.notion.so/jarvisdev/Swap-two-numbers-ee96d9ec445443a6bcbb0ef22d82de47
 * Big O Notation: O(1) constant time operations.
 */

public class SwapNums {

  public int[] swapNum(int x, int y){
    x = x ^ y;
    y = x ^ y;
    x = x ^ y;

    System.out.println("num1 = " + x + " num2 = " + y);
    return new int[]{x,y};
  }

  public int[] swapNumArith(int x, int y){
    x = x + y;
    y = x - y;
    x = x - y;

    System.out.println("num1 = " + x + " num2 = " + y);
    return new int[]{x,y};
  }

}
