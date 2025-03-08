package Utils.ADT;

import Exceptions.HeapException;
import Model.Value.Value;

import java.util.Map;

public interface MyIHeap {
    Integer insert(Value content);
    void update(int address, Value content) throws HeapException;
    Value delete(int address) throws HeapException;
    boolean containsAddress(int address);
    Value getValue(int address) throws HeapException;
    Map<Integer, Value> getContent();
    void setContent(Map<Integer, Value> newMap);
}
