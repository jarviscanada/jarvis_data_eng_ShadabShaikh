package ca.jrvs.apps.practice.dataStructure;

import java.util.Arrays;
import java.util.LinkedList;

public class LinkedListAPIs {
    public static void main(String[] args) {

      LinkedList<String> animal = new LinkedList<>();
      animal.add("this");
      animal.add("that");
      animal.add("there");

      animal.addFirst("The_one");

      animal.addLast("The_only");

      String oneelement = animal.getLast();
      System.out.println(oneelement);

      System.out.println(Arrays.toString(animal.toArray()));
    }

  }
