package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PrintLetterIndexTest {

  String testString = "abcdEe";
  PrintLetterIndex printLetterIndex = new PrintLetterIndex();

  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void letterIndex() {
    assertEquals("a1b2c3d4E31e5", printLetterIndex.letterIndex(testString));
  }
}