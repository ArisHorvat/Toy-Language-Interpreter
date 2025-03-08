package Model.Value;

import Model.Type.StringType;
import Model.Type.Type;

public class StringValue implements Value{
    private String value;

    public StringValue(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    @Override
    public boolean equals(Object another){
        if(another instanceof StringValue && ((StringValue)another).getValue().equals(this.value))
            return true;
        return false;
    }

    @Override
    public String toString(){
        return value;
    }

    @Override
    public Type getType(){
        return new StringType();
    }

    @Override
    public Value deepCopy(){
        return new StringValue(value);
    }
}
