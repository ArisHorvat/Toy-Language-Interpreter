package Model.Value;

import Model.Type.IntType;
import Model.Type.Type;

public class IntValue implements Value{
    private int value;

    public IntValue(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    @Override
    public boolean equals(Object another){
        if(another instanceof IntValue && ((IntValue)another).getValue() == this.value)
            return true;
        return false;
    }

    @Override
    public String toString(){
        return String.valueOf(value);
    }

    @Override
    public Type getType(){
        return new IntType();
    }

    @Override
    public Value deepCopy(){
        return new IntValue(this.value);
    }

}
