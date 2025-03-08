package Utils.ADT;

import Exceptions.DictException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    public Map<K, V> table;

    public MyDictionary() {
        table = new HashMap<>();
    }

    @Override
    public void put(K key, V value){
        table.put(key, value);
    }

    @Override
    public V lookUp(K key) throws DictException {
        return table.get(key);
    }

    @Override
    public boolean containsKey(K key){
        return table.containsKey(key);
    }

    @Override
    public void update(K key, V value) throws DictException {
        if(!this.table.containsKey(key)){
            throw new DictException("Key " + key.toString() + " not found");
        }
        table.put(key, value);
    }

    @Override
    public V remove(K key) throws DictException {
        if(!this.table.containsKey(key)){
            throw new DictException("Key " + key.toString() + " not found");
        }
        return table.remove(key);
    }

    @Override
    public Set<K> keys(){
        return table.keySet();
    }

    @Override
    public Map<K, V> getContent(){
        return table;
    }

    @Override
    public MyIDictionary<K, V> copy(){
        MyIDictionary<K, V> copy = new MyDictionary<>();

        for(K key : table.keySet()){
            copy.put(key, table.get(key));
        }

        return copy;
    }

    @Override
    public String toString(){
        return table.toString();
    }
}
