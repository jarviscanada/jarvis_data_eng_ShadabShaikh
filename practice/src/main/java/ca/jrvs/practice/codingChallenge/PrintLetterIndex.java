package ca.jrvs.practice.codingChallenge;

/**
 * Prints input string with numbers corresponding to their index
 * https://www.notion.so/jarvisdev/Print-letter-with-number-d5bcd28665db4513a990031cc9ea0e35
 * Big O notation: O(n) due to for loop iterating for each n entry
 */

public class PrintLetterIndex {

  public String letterIndex(String string) {
    StringBuilder builtString = new StringBuilder();
    for (int i = 0; i < string.length(); i++) {
      char ch = string.charAt(i);
      builtString.append(ch);
      if (ch < 'a') {
        builtString.append(ch - 'A' + 27);
      } else {
        builtString.append(ch - 'a' + 1);
      }
    }
    return builtString.toString();
  }
}
