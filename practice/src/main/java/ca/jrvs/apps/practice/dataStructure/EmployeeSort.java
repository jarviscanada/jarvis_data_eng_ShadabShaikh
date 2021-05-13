package ca.jrvs.apps.practice.dataStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EmployeeSort {

  public static void main(String[] args) {
    List<EmployeeComparableSort> employeeList1 = new ArrayList<>();
    employeeList1.add(new EmployeeComparableSort(1, "Bob", 37, 60000));
    employeeList1.add(new EmployeeComparableSort(2, "John", 23, 80000));
    employeeList1.add(new EmployeeComparableSort(3, "Jane", 37, 75000));
    employeeList1.add(new EmployeeComparableSort(4, "Peter", 40, 100000));
    Collections.sort(employeeList1);
    employeeList1.stream().forEach(System.out::println);

    List<Employee> employeeList2 = new ArrayList<>();
    employeeList2.add(new Employee(1, "Jen", 35, 90000));
    employeeList2.add(new Employee(2, "Chris", 27, 70000));
    employeeList2.add(new Employee(3, "Tina", 29, 50000));
    employeeList2.add(new Employee(4, "Bill", 27, 64000));
    Collections.sort(employeeList2, new EmployeeComparatorSort());
    employeeList2.stream().forEach(System.out::println);
  }

  public static class EmployeeComparableSort extends Employee implements
      Comparable<EmployeeComparableSort> {

    EmployeeComparableSort() {
      super();
    }

    EmployeeComparableSort(int id, String name, int age, long salary) {
      super(id, name, age, salary);
    }

    @Override
    public int compareTo(EmployeeComparableSort o) {
        if (this.getSalary() < o.getSalary()) {
          return -1;
        } else if (this.getSalary() > o.getSalary()) {
          return 1;
        } else {
          return 0;
        }
      }
    }

  public static class EmployeeComparatorSort implements Comparator<Employee> {

    @Override
    public int compare(Employee o1, Employee o2) {
      if (o1.getAge() != o2.getAge()) {
        return o1.getAge() - o2.getAge();
      } else {
        if (o1.getSalary() < o2.getSalary()) {
          return -1;
        } else if (o1.getSalary() > o2.getSalary()) {
          return 1;
        } else {
          return 0;
        }
      }
    }
  }
}