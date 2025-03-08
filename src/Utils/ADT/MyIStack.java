package Utils.ADT;

import Exceptions.StackException;

import java.util.List;

public interface MyIStack<T> extends Iterable<T> {
    void push(T item);
    T pop() throws StackException;
    boolean isEmpty();
    List<T> getReverse();
    List<T> toList();
}
