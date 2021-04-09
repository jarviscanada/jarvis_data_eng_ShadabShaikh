package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Returns the index values of two numbers in an array that add up to the target sum.
 * https://www.notion.so/jarviscanada/Two-Sum-a2ca50b965f94256b17c2b377f3be1b4
 * Big O notation: O(n^2) time to go through the array twice cross-checking with each value.
 *
 */

public class TwoSum {

  public static int[] twoSum(int[] nums, int target) {
    int sum = 0;
    for (int i = 0; i < nums.length; i++) {
      for (int j = 0; j < nums.length; j++) {
        sum = nums[i] + nums[j];
        if (sum == target && i != j) {
          return new int[]{i, j};
        }
      }
    }
    throw new IllegalArgumentException("There is no two sum solution in the array.");
  }

  /**
   * Approach 2: Sorted
   * Big O notation: O(n + nlogn) to sort and then iterate through the loop once.
   *
   */

  public static int[] twoSumSort(int[] nums, int target) {
    int i = 0;
    int j = nums.length - 1; //last index

    Arrays.sort(nums);

    while (i < j) {
      int sum = nums[i] + nums[j];
      if (sum == target) {
        return new int[] {i, j};
      } else if (sum < target) {
        i++;
      } else {
        j--;
      }
    }
    throw new IllegalArgumentException("There is no two sum solution in the array.");
  }

  /**
   * Approach 3: HashMap
   * Big O notation: O(n). The array is stored in a map in O(n) space and iterate through the array while mapping.
   *
   */

  public static int[] twoSumHash(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++){
      int numPair = target - nums[i];
      if (map.containsKey(numPair)){
        return new int[] {map.get(numPair), i};
      }
      map.put(nums[i], i);
    }
    throw new IllegalArgumentException("There is no two sum solution in the array.");
  }
}

