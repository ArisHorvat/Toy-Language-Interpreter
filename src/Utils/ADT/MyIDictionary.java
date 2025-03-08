package Utils.ADT;

import Exceptions.DictException;

import java.util.Map;
import java.util.Set;

public interface MyIDictionary<K, V> {
    void put(K key, V value);
    V lookUp(K key) throws DictException;
    boolean containsKey(K key);
    void update(K key, V value) throws DictException;
    V remove(K key) throws DictException;
    Set<K> keys();
    Map<K, V> getContent();
    MyIDictionary<K, V> copy();
}
