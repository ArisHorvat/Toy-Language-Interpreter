package Model.Value;

import Model.Type.RefType;
import Model.Type.Type;

public class RefValue implements Value{
    private int address;
    private Type locationType;

    public RefValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress(){
        return address;
    }

    public Type getLocationType(){
        return locationType;
    }

    @Override
    public Type getType(){
        return new RefType(locationType);
    }

    @Override
    public boolean equals(Object another){
        if(another instanceof RefValue){
            return address == ((RefValue)another).getAddress();
        }
        return false;
    }

    @Override
    public Value deepCopy(){
        return new RefValue(address, locationType.deepCopy());
    }

    @Override
    public String toString(){
        return "(" + address + ", " + locationType + ")";
    }
}
