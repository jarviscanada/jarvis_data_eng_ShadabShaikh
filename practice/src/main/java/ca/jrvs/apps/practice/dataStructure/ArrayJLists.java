package ca.jrvs.apps.practice.dataStructure;

public class ArrayJLists<E> implements JLists<E> {

  private static final int DEFAULT_CAPACITY = 10;

  transient Object[] elementData;

  private int size;

  public ArrayJLists(int initialCapacity) {
    if (initialCapacity > 0) {
      this.elementData = new Object[initialCapacity];
    } else {
      throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
    }
  }

  public ArrayJLists() {
    this(DEFAULT_CAPACITY);
  }

  @Override
  public boolean add(E e) {
    return false;
  }

  @Override
  public Object[] toArray(){
    return new Object[0];
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public int indexOf(Object o) {
    return 0;
  }

  @Override
  public boolean contains(Object o) {
    return false;
  }

  @Override
  public E get(int index) {
    return null;
  }

  public E remove(int index) {
    return null;
  }

  @Override
  public void clear(){

  }

}
