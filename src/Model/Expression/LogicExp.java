package Model.Expression;

import Exceptions.ExpressionException;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyIFileTable;
import Utils.Containers.MyISymTable;

import java.io.BufferedReader;

public class LogicExp implements Exp{
    private Exp e1;
    private Exp e2;
    private String op;

    public LogicExp(String op, Exp e1, Exp e2){
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public Value evaluate(MyISymTable symTable, MyIHeap heap) throws ExpressionException{
        Value v1, v2;
        try {
            v1 = e1.evaluate(symTable, heap);
        }
        catch(ExpressionException e){
            throw new ExpressionException(e.getMessage());
        }
        if(v1.getType().equals(new BoolType())){
            try {
                v2 = e2.evaluate(symTable, heap);
            }
            catch(ExpressionException e){
                throw new ExpressionException(e.getMessage());
            }
            if(v2.getType().equals(new BoolType())){
                BoolValue i1 = (BoolValue)v1;
                BoolValue i2 = (BoolValue)v2;
                boolean n1, n2;
                n1 = i1.getValue();
                n2 = i2.getValue();
                if(op.equals("and")){
                    return new BoolValue(n1 && n2);
                }
                else if(op.equals("or")){
                    return new BoolValue(n1 || n2);
                }
            }
            else{
                throw new ExpressionException("second operand is not a boolean value");
            }
        }
        else{
            throw new ExpressionException("first operand is not a boolean value");
        }
        throw new ExpressionException("Invalid code");
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException{
        Type typ1, typ2;
        typ1 = e1.typeCheck(typeEnv);
        typ2 = e2.typeCheck(typeEnv);

        if(typ1.equals(new BoolType())){
            if(typ2.equals(new BoolType())){
                return new BoolType();
            }
            else
                throw new ExpressionException("second operand is not a boolean");
        }
        else
            throw new ExpressionException("first operand is not a boolean");
    }

    @Override
    public String toString(){
        return "(" + e1.toString() + " " + op + " " +  e2.toString() + ")";
    }

    @Override
    public Exp deepCopy(){
        return new LogicExp(op, e1.deepCopy(), e2.deepCopy());
    }
}
