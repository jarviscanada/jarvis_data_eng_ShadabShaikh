package ca.jrvs.apps.practice.dataStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayListAPIs {
  public static void main(String[] args) {

    List<String> animals = new ArrayList<>();

    animals.add("Lion2");
    animals.add("Tiger");
    animals.add(2,"Cat");

    int s = animals.size();

    String firstElement = animals.get(0);

    Boolean hasCat = animals.contains("Cat");

    int catIndex = animals.indexOf("Cat");

    boolean isCatRemoved = animals.remove("Cat");
    String removedElement = animals.remove(1);

    //sort
    animals.sort(String::compareToIgnoreCase);

    System.out.println(Arrays.toString(animals.toArray()));

  }
}