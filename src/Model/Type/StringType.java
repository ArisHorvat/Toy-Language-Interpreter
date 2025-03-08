package Model.Type;

import Model.Value.StringValue;
import Model.Value.Value;

public class StringType implements Type{
    public StringType() {}

    @Override
    public boolean equals(Object another){
        return another instanceof StringType;
    }

    @Override
    public String toString(){
        return "string";
    }

    @Override
    public Type deepCopy(){
        return new StringType();
    }

    @Override
    public Value defaultValue(){
        return new StringValue("");
    }
}
