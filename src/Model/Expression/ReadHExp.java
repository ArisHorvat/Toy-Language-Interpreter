package Model.Expression;

import Exceptions.ExpressionException;
import Model.Type.RefType;
import Model.Type.Type;
import Model.Value.RefValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyISymTable;


public class ReadHExp implements Exp{
    private Exp exp;

    public ReadHExp(Exp exp) {
        this.exp = exp;
    }

    @Override
    public Value evaluate(MyISymTable symTable, MyIHeap heap) throws ExpressionException {
        Value value = exp.evaluate(symTable, heap);
        if(!(value instanceof RefValue)){
            throw new ExpressionException("Expression expected a Ref value");
        }

        RefValue refValue = (RefValue)value;
        return heap.getValue(refValue.getAddress());
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException{
        Type typ = exp.typeCheck(typeEnv);

        if (typ instanceof RefType) {
            RefType refT = (RefType)typ;
            return refT.getInner();
        } else
            throw new ExpressionException("the rH argument is not a Ref Type");
    }

    @Override
    public Exp deepCopy(){
        return new ReadHExp(exp.deepCopy());
    }

    @Override
    public String toString(){
        return "HeapRead(" + exp.toString() + ")";
    }
}
