package ca.jrvs.practice.codingChallenge;

/**
 * Rotates the left-most letter in A to the right-most and checks if A contains B
 * https://www.notion.so/jarvisdev/Rotate-String-f765f4428e644cc58ad8844d6dc8e94a Big O-notation:
 * O(n)^2 since contains() checks indexOf (O(n)) for each input n*n = n^2
 */

public class RotateString {

  public boolean rotateString(String A, String B) {
    return A.length() == B.length() && (A + A).contains(B);
  }
}

