package ca.jrvs.practice.codingChallenge;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements a stack using queue.
 * https://www.notion.so/jarvisdev/Implement-Stack-using-Queue-32bc44724bda4b77950634067beffbc8
 * Big O notation: Push O(1), pop O(n). Push is simple. Pop requires cycling through the queue.
 */

public class MyStack {

  private Queue<Integer> queue1 = new LinkedList<>();
  private final Queue<Integer> queue2 = new LinkedList<>();
  private final Queue<Integer> queue3 = new LinkedList<>();
  private int top;

  public MyStack() {
  }

   // Push element x onto stack.
  public void push(int x) {
    queue1.add(x);
    top = x;
  }

   // Removes the element on top of the stack and returns that element.
    public int pop() {
    while (queue1.size() > 1) {
      top = queue1.remove();
      queue2.add(top);
    }
    int popped = queue1.remove();
    Queue<Integer> temp = queue1;
    queue1 = queue2;
    queue1 = temp;
    return popped;
  }

  // Get the top element.
  public int top() {
    return top;
  }

   // Returns whether the stack is empty.
  public boolean empty() {
    return queue1.isEmpty();
  }

  /**
   * Approach two: Using one queue.
   * Big O notation: Push O(n), pop O(1). Push has a more complex task of cycling through the whole
   * queue. Pop is constant.
   */

  // Push element x onto stack.
  public void push2(int x) {
    queue3.add(x);
    for (int i = 1; i < queue3.size(); i++) {
      queue3.add(queue3.remove());
    }
  }

  // Removes the element on top of the stack and returns that element.
  public int pop2() {
    return queue3.poll();
  }

  // Get the top element.
  public int top2() {
    return queue3.peek();
  }

  // Returns whether the stack is empty.
  public boolean empty2() {
    return queue3.isEmpty();
  }

}