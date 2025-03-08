package Utils.ADT;

import Exceptions.LatchException;

import java.util.Map;
import java.util.Set;

public interface MyILatchTable{
    void put(Integer key, Integer value) throws LatchException;
    int put(Integer value) throws LatchException;

    void update(int position, int value) throws LatchException;

    boolean containsKey(int position);
    int get(int position) throws LatchException;
    int getFirstFreeLocation();


    Map<Integer, Integer> getContent();
    void setContent(Map<Integer, Integer> newMap);
    Set<Integer> keySet();
}
