package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValidPalindromeTest {

  ValidPalindrome validPalindrome = new ValidPalindrome();

  @Test
  public void isPalindrome() {
    String palin = "A man, a plan, a canal: Panama";
    String notPalin = "Race a car";
    String definitelyNotPalin = "Pineapple butter";
    assertTrue(validPalindrome.isPalindrome(palin));
    assertFalse(validPalindrome.isPalindrome(notPalin));
    assertFalse(validPalindrome.isPalindrome(definitelyNotPalin));

    assertTrue(validPalindrome.isPalindrome2(palin));
    assertFalse(validPalindrome.isPalindrome2(notPalin));
    assertFalse(validPalindrome.isPalindrome2(definitelyNotPalin));
  }
}