package Utils.Containers;

import Exceptions.DictException;
import Model.Value.Value;
import Utils.ADT.MyDictionary;

import javax.print.DocFlavor;
import java.util.Map;
import java.util.Set;

public class MySymTable implements MyISymTable{
    private MyDictionary<String, Value> table;

    public MySymTable(){
        table = new MyDictionary<String, Value>();
    }

    @Override
    public void put(String key, Value value){
        table.put(key, value);
    }

    @Override
    public Value lookUp(String key) throws DictException {
        return table.lookUp(key);
    }

    @Override
    public boolean containsKey(String key){
        return table.containsKey(key);
    }

    @Override
    public void update(String key, Value value) throws DictException {
        if(!this.table.containsKey(key)){
            throw new DictException("Key " + key.toString() + " not found");
        }
        table.put(key, value);
    }

    @Override
    public Value remove(String key) throws DictException {
        if(!this.table.containsKey(key)){
            throw new DictException("Key " + key.toString() + " not found");
        }
        return table.remove(key);
    }

    @Override
    public Set<String> keys(){
        return table.keys();
    }

    @Override
    public Map<String, Value> getContent(){
        return table.getContent();
    }

    @Override
    public MyISymTable clone(){
        MyISymTable cloneSymTable = new MySymTable();

        for(String key: keys()){
            cloneSymTable.put(key, lookUp(key));
        }

        return cloneSymTable;
    }

    @Override
    public String toString(){
        return table.toString();
    }
}
