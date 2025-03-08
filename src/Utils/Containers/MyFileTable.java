package Utils.Containers;

import Exceptions.DictException;
import Model.Value.StringValue;
import Utils.ADT.MyDictionary;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyFileTable implements MyIFileTable{
    private MyDictionary<StringValue, BufferedReader> table;

    public MyFileTable() {
        table = new MyDictionary<StringValue, BufferedReader>();
    }

    @Override
    public void open(StringValue key, BufferedReader value){
        table.put(key, value);
    }

    @Override
    public BufferedReader getBuffer(StringValue key) throws DictException {
        return table.lookUp(key);
    }

    @Override
    public boolean containsFile(StringValue key){
        return table.containsKey(key);
    }


    @Override
    public BufferedReader close(StringValue key) throws DictException {
        if(!this.table.containsKey(key)){
            throw new DictException("Key " + key.toString() + " not found");
        }
        return table.remove(key);
    }

    @Override
    public Set<StringValue> getFiles(){
        return table.keys();
    }

    @Override
    public String toString(){
        return table.toString();
    }
}
