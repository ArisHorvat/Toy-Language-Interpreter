package Utils.ADT;

import Exceptions.LatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyLatchTable implements MyILatchTable{
    private Map<Integer, Integer> latchTable;
    private int nextFreeLocation;


    public MyLatchTable() {
        this.latchTable = new HashMap<>();
        this.nextFreeLocation = 1;
    }

    @Override
    public void put(Integer key, Integer value) throws LatchException {
        if (!key.equals(nextFreeLocation))
            throw new LatchException("Invalid latch table location!");
        synchronized (this) {
            latchTable.put(key, value);
            nextFreeLocation++;
        }
    }

    @Override
    public int put(Integer value) throws LatchException {
        synchronized (this) {
            latchTable.put(nextFreeLocation, value);
            nextFreeLocation++;
        }
        return nextFreeLocation-1;
    }


    @Override
    public int getFirstFreeLocation(){
        int locationAddress = 1;
        while (this.latchTable.get(locationAddress) != null)
            locationAddress++;
        return locationAddress;
    }


    @Override
    public Map<Integer, Integer> getContent() {
        synchronized (this) {
            return latchTable;
        }
    }

    @Override
    public boolean containsKey(int position) {
        synchronized (this) {
            return latchTable.containsKey(position);
        }
    }

    @Override
    public int get(int position) throws LatchException {
        synchronized (this) {
            if (!latchTable.containsKey(position))
                throw new LatchException(String.format("%d is not present in the table!", position));
            return latchTable.get(position);
        }
    }

    @Override
    public void update(int position, int value) throws LatchException {
        synchronized (this) {
            if (latchTable.containsKey(position)) {
                latchTable.replace(position, value);
            } else {
                throw new LatchException(String.format("%d is not present in the table!", position));
            }
        }
    }

    @Override
    public void setContent(Map<Integer, Integer> newMap) {
        synchronized (this) {
            this.latchTable = newMap;
        }
    }

    @Override
    public Set<Integer> keySet() {
        synchronized (this) {
            return latchTable.keySet();
        }
    }

    @Override
    public String toString() {
        return latchTable.toString();
    }

}
