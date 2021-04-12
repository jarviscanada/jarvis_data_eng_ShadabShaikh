package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MyQueueTest {

  MyQueue myQueue = new MyQueue();
  private final int x = 2;
  private final int y = 3;
  private final int z = 4;

  @Test
  public void approach1() {

    myQueue.push(x);
    int param_2 = myQueue.pop();
    assertEquals(2, param_2);
    myQueue.push(y);
    int param_3 = myQueue.peek();
    assertEquals(3, param_3);
    myQueue.push(z);

    assertEquals(4, myQueue.pop());

    boolean param_4 = myQueue.empty();
    assertFalse(param_4);

    myQueue.pop();
    param_4 = myQueue.empty();
    assertTrue(param_4);
  }

  @Test
  public void approach2() {

    myQueue.push2(x);
    assertEquals(x, myQueue.peek2());
    myQueue.pop2();
    myQueue.push(y);
    int param_3 = myQueue.peek2();
    assertEquals(3, param_3);
    myQueue.push2(z);

    boolean param_4 = myQueue.empty2();
    assertFalse(param_4);

    myQueue.pop();
    param_4 = myQueue.empty2();
    assertFalse(param_4);
  }
}