package Utils.ADT;

import Exceptions.ListException;

public interface MyIList<T> extends Iterable<T>{
    void add(T item);
    T get(int index) throws ListException;
    int size();
}
