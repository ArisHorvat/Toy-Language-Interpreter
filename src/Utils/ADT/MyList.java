package Utils.ADT;

import Exceptions.ListException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyList<T> implements MyIList<T> {
    private List<T> list;

    public MyList(){
        list = new ArrayList<>();;
    }

    @Override
    public void add(T item){
        list.add(item);
    }

    @Override
    public T get(int index) throws ListException{
        if(index < 0 || index >= this.size()){
            throw new ListException("Index " + String.valueOf(index) + " out of bounds");
        }
        return list.get(index);
    }

    @Override
    public int size(){
        return list.size();
    }

    @Override
    public Iterator<T> iterator(){
        return list.iterator();
    }

    @Override
    public String toString(){
        return list.toString();
    }
}
