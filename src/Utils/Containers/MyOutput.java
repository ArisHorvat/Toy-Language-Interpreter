package Utils.Containers;

import Exceptions.ListException;
import Model.Value.Value;
import Utils.ADT.MyList;

import java.util.Iterator;

public class MyOutput implements MyIOutput{
    private MyList<Value> list;

    public MyOutput(){
        list = new MyList<Value>();
    }

    @Override
    public void add(Value item){
        list.add(item);
    }

    @Override
    public Value get(int index) throws ListException{
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
    public Iterator<Value> iterator(){
        return list.iterator();
    }

    @Override
    public String toString(){
        return list.toString();
    }
}
