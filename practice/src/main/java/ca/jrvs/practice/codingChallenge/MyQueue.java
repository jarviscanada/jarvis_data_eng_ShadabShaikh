package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/jarvisdev/Implement-Queue-using-Stacks-21ac2aea43744899959d887f9681f21c
 * Implements a queue using two stacks
 * Approach 1: O(n) per push
 */

import java.util.Stack;

public class MyQueue {

  Stack<Integer> stack1 = new Stack();
  Stack<Integer> stack2 = new Stack();
  private int front;

  public void push(int x) {
    if(stack1.empty()){
      front = x;
    }
    while (!stack1.isEmpty()) {
      stack2.push(stack1.pop());
    }
    stack1.push(x);
    while (!stack2.isEmpty()) {
      stack1.push(stack2.pop());
    }
  }

  public int pop() {
    stack1.pop();
    if (!stack1.empty())
     front = stack1.peek();
    return front;
  }

  public int peek() {
    front = stack1.peek();
    return front;
  }

  public boolean empty() {
    return stack1.isEmpty();
  }

  /** Approach 2:
   * O(1) per operation
   * @param x
   */

  public void push2(int x){
    if (stack1.empty())
      front = x;
    stack1.push(x);
  }

  public void pop2(){
    if (stack2.isEmpty()) {
      while (!stack1.isEmpty())
        stack2.push(stack1.pop());
    }
    stack2.pop();
  }

  public int peek2() {
    if (!stack2.isEmpty()) {
      return stack2.peek();
    }
    return front;
  }

  public boolean empty2(){
    return stack1.isEmpty();
  }
}
