package Model.Expression;

import Exceptions.ExpressionException;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyIFileTable;
import Utils.Containers.MyISymTable;

import java.io.BufferedReader;

public class ValueExp implements Exp{
    private Value value;

    public ValueExp(Value value){
        this.value = value;
    }

    @Override
    public Value evaluate(MyISymTable symTable, MyIHeap heap) throws ExpressionException{
        return this.value;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException{
        return value.getType();
    }

    @Override
    public String toString(){
        return this.value.toString();
    }

    @Override
    public Exp deepCopy(){
        return new ValueExp(this.value.deepCopy());
    }
}
