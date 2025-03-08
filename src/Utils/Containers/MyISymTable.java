package Utils.Containers;

import Exceptions.DictException;
import Model.Value.Value;

import java.util.Map;
import java.util.Set;

public interface MyISymTable {
    void put(String key, Value value);
    Value lookUp(String key) throws DictException;
    boolean containsKey(String key);
    void update(String key, Value value) throws DictException;
    Value remove(String key) throws DictException;
    Set<String> keys();
    Map<String, Value> getContent();
    MyISymTable clone();
}
