package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class RotateStringTest {

  RotateString rotateString = new RotateString();

  @Test
  public void rotateString() {
    String A = "abcde";
    String B = "cdeab";
    String C = "cdaeb";

    assertTrue(rotateString.rotateString(A,B));
    assertFalse(rotateString.rotateString(A,C));
  }
}