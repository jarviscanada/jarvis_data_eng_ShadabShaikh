package ca.jrvs.practice.codingChallenge;

/**
 * Checks a string to determine if it's a valid palindrome
 * https://www.notion.so/jarvisdev/Valid-Palindrome-221070e7fb2e472e84e660008e11578a
 * Big O-notation: O(n) due to the for loop running n/2 times.
 */


public class ValidPalindrome {

  public boolean isPalindrome(String s) {
    String str = s;
    str = str.replaceAll(
        "[^a-zA-Z0-9]", "").toLowerCase();
    char[] ch = str.toCharArray();
    if (ch.length < 2) {
      return true;
    }
    int i;
    int j = ch.length - 1;

    for (i = 0; i < j; i++) {
      if (ch[i] != ch[j]) {
        return false;
      } else {
        j--;
      }
    }
    return true;
  }

  /**
   * Approach 2: Using recursion. Big O-notation: O(n) due to continuous method calls until loop is
   * finished.
   *
   * @param s
   * @return boolean for palindrome
   */


  public boolean isPalindrome2(String s) {
    if (s.length() < 2) {
      return true;
    }
    s = s.replaceAll(
        "[^a-zA-Z0-9]", "").toLowerCase();
    int start = 0;
    int end = s.length() - 1;

    if (s.charAt(start) != s.charAt(end)) {
      return false;
    }
    return isPalindrome2(s.substring(start + 1, end));
  }
}



