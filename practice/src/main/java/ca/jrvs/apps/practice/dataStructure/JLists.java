package ca.jrvs.apps.practice.dataStructure;

import java.util.Collection;

interface JLists<E> {

  boolean add(E e);

  Object[] toArray();

  public int size();

  public boolean isEmpty();

  int indexOf(Object o);

  boolean contains(Object o);

  E get(int index);

  E remove(int index);

  void clear();
}
