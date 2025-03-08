package Model.Expression;

import Exceptions.ExpressionException;
import Model.Type.Type;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyISymTable;

public interface Exp {
    Value evaluate(MyISymTable symTable, MyIHeap heap) throws ExpressionException;
    Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException;
    Exp deepCopy();
}
