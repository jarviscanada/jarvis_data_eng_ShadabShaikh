package ca.jrvs.practice.search;

import java.util.Arrays;
import java.util.Optional;

public class BinarySearch {

  /**
   * find the the target index in a sorted array
   *
   * @param arr    input arry is sorted
   * @param target value to be searched
   * @return target index or Optional.empty() if not ound
   */
  public <E extends Comparable<E>> Optional<Integer> binarySearchRecursion(E[] arr, E target) {

    if (arr.length == 0) {
      return Optional.empty();
    }

    int half_Array = arr.length / 2;
    //Determine which half to continue search with
    if (arr[half_Array].compareTo(target) < 0) {
      E[] newArr = Arrays.copyOfRange(arr, half_Array + 1, arr.length);
      Optional<Integer> result = binarySearchRecursion(newArr, target);
      return result.map(integer -> integer + half_Array + 1);
    } else if (arr[half_Array].compareTo(target) > 0) {
      E[] newArr = Arrays.copyOfRange(arr, 0, half_Array);
      return binarySearchRecursion(newArr, target);
    } else {
      return Optional.of(half_Array);
    }
  }

  /**
   * find the the target index in a sorted array
   *
   * @param arr    input arry is sorted
   * @param target value to be searched
   * @return target index or Optional.empty() if not ound
   */
  public <E extends Comparable<E>> Optional<Integer> binarySearchIteration(E[] arr, E target) {
    int left = 0;
    int right = arr.length - 1;
    while (left <= right) {
      int half_Array = (left + right) / 2;
      if (arr[half_Array].compareTo(target) < 0) {
        left = half_Array + 1;
      } else if (arr[half_Array].compareTo(target) > 0) {
        right = half_Array - 1;
      } else {
        return Optional.of(half_Array);
      }
    }
    return Optional.empty();
  }
}
