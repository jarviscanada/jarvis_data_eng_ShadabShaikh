package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FibonacciTest {

  @Test
  public void fibTest() {
    Fibonacci fibonacci = new Fibonacci();
    assertEquals(377, fibonacci.fibRecursive(14));
    assertEquals(1, fibonacci.fibRecursive(2));
    assertEquals(13, fibonacci.fibRecursive(7));

    assertEquals(377, fibonacci.fibDynamic(14));
    assertEquals(1, fibonacci.fibDynamic(2));
    assertEquals(13, fibonacci.fibDynamic(7));

  }

  @Test
  public void fibDynamicTest() {

  }
}