package Model.Expression;

import Exceptions.ExpressionException;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyIFileTable;
import Utils.Containers.MyISymTable;

import java.io.BufferedReader;

public class VarExp implements Exp{
    private String id;

    public VarExp(String id){
        this.id = id;
    }

    @Override
    public Value evaluate(MyISymTable symTable, MyIHeap heap) throws ExpressionException{
        return symTable.lookUp(this.id);
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException{
        return typeEnv.lookUp(id);
    }

    @Override
    public String toString(){
        return this.id;
    }

    @Override
    public Exp deepCopy(){
        return new VarExp(this.id);
    }
}
