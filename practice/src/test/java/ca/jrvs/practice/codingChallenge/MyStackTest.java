package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MyStackTest {

  MyStack myStack = new MyStack();
  MyStack myStack2 = new MyStack();
  private final int x = 2;
  private final int y = 3;
  private final int z = 4;
  
  @Test
  public void approach1() {

    assertTrue(myStack.empty());

    myStack.push(x);
    int param_2 = myStack.pop();
    assertEquals(2, param_2);
    myStack.push(x);
    myStack.push(y);
    assertEquals(3, myStack.top());
    myStack.push(z);

    assertEquals(4, myStack.pop());
    assertFalse(myStack.empty());
  }

  @Test
  public void approach2() {
    assertTrue(myStack.empty());

    myStack2.push2(x);
    int param_2 = myStack2.pop2();
    assertEquals(2, param_2);
    myStack2.push2(x);
    myStack2.push2(y);
    assertEquals(3, myStack2.top2());
    myStack2.push2(z);

    assertEquals(4, myStack2.pop2());
    assertFalse(myStack2.empty2());


  }
}