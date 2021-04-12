package ca.jrvs.practice.codingChallenge;

/**
 * https://www.notion.so/jarvisdev/String-to-Integer-atoi-357472032eb848deb154c1b87e7cd2b6
 * Converts string to int manually using the numeric value extracted from the string
 * Time complexity: O(n) worst case. n are the number of characters in the String.
 */

public class StringToInt {

  public int myAtoi(String str) {
    int sign = 1,
        base = 0, i = 0,
        INT_MAX = Integer.MAX_VALUE, INT_MIN = Integer.MIN_VALUE;

    while (i < str.length() && str.charAt(i) == ' ') {
      i++;
    }

    if (i >= str.length()) {
      return 0;
    }

    if (str.charAt(i) == '+' || str.charAt(i) == '-') {
      if (str.charAt(i) == '-') {
        sign = -1;
      }
      i++;
    }

    while (i < str.length() && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
      if (base > INT_MAX / 10 || base == INT_MAX / 10) {
        if (sign == -1) {
          return INT_MIN;
        } else {
          return INT_MAX;
        }
      }
      base = 10 * base + (str.charAt(i++) - '0');
    }

    return base * sign;

  }

}
