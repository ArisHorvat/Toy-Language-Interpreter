package Utils.ADT;

import Exceptions.HeapException;
import Model.Value.Value;

import java.util.HashMap;
import java.util.Map;

public class MyHeap implements MyIHeap{
    private Map<Integer, Value> heap;
    private Integer currentAddress;

    public MyHeap(){
        heap = new HashMap<Integer, Value>();
        currentAddress = 1;
    }

    @Override
    public Integer insert(Value content){
        heap.put(currentAddress, content);
        Integer returnAddress = currentAddress;
        currentAddress = newAddress();
        return returnAddress;
    }

    public Integer newAddress(){
        currentAddress += 1;
        while(heap.containsKey(currentAddress)){
            currentAddress += 1;
        }
        return currentAddress;
    }

    @Override
    public void update(int address, Value content) throws HeapException{
        if(!this.heap.containsKey(address)){
            throw new HeapException("Address " + String.valueOf(address) + " not found");
        }
        heap.put(address, content);
    }

    @Override
    public Value delete(int address) throws HeapException{
        if(!this.heap.containsKey(address)){
            throw new HeapException("Address " + String.valueOf(address) + " not found");
        }
        return heap.remove(address);
    }

    @Override
    public boolean containsAddress(int address){
        return heap.containsKey(address);
    }

    @Override
    public Value getValue(int address) throws HeapException {
        if(!this.heap.containsKey(address)){
            throw new HeapException("Address " + String.valueOf(address) + " not found");
        }
        return heap.get(address);
    }

    @Override
    public Map<Integer, Value> getContent(){
        return heap;
    }

    @Override
    public void setContent(Map<Integer, Value> newMap){
        heap.clear();
        for(Integer i: newMap.keySet()){
            heap.put(i, newMap.get(i));
        }
    }

    @Override
    public String toString(){
        return heap.toString();
    }
}
