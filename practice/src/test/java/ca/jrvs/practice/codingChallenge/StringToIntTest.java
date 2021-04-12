package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StringToIntTest {

  StringToInt stringToInt;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void myAtoi() {
    stringToInt = new StringToInt();
    assertEquals(-62, stringToInt.myAtoi("    -62"));


  }
}