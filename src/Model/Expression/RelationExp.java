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

public class RelationExp implements Exp {
    private Exp ex1;
    private Exp ex2;
    String op;

    public RelationExp(String op, Exp ex1, Exp ex2) {
        this.op = op;
        this.ex1 = ex1;
        this.ex2 = ex2;
    }

    @Override
    public Value evaluate(MyISymTable symTable, MyIHeap heap) throws ExpressionException{
        Value v1, v2;
        v1 = ex1.evaluate(symTable, heap);
        if(v1.getType().equals(new IntType())){
            v2 = ex2.evaluate(symTable, heap);
            if(v2.getType().equals(new IntType())){
                int intV1 = ((IntValue)v1).getValue();
                int intV2 = ((IntValue)v2).getValue();

                if(op.equals("<")){
                    return new BoolValue(intV1 < intV2);
                }
                else if(op.equals("<=")){
                    return new BoolValue(intV1 <= intV2);
                }
                else if(op.equals(">")){
                    return new BoolValue(intV1 > intV2);
                }
                else if(op.equals(">=")){
                    return new BoolValue(intV1 >= intV2);
                }
                else if(op.equals("==")){
                    return new BoolValue(intV1 == intV2);
                }
                else if(op.equals("!=")){
                    return new BoolValue(intV1 != intV2);
                }
                else{
                    throw new ExpressionException("Invalid relation operator");
                }
            }
            else{
                throw new ExpressionException("the second operand is not an integer");
            }
        }
        else{
            throw new ExpressionException("the first operand is not an integer");
        }
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException{
        Type typ1, typ2;
        typ1 = ex1.typeCheck(typeEnv);
        typ2 = ex2.typeCheck(typeEnv);

        if(typ1.equals(new IntType())){
            if(typ2.equals(new IntType())){
                return new BoolType();
            }
            else
                throw new ExpressionException("second operand is not an integer");
        }
        else
            throw new ExpressionException("first operand is not an integer");
    }

    @Override
    public Exp deepCopy() {
        return new RelationExp(op, ex1.deepCopy(), ex2.deepCopy());
    }

    @Override
    public String toString(){
        return ex1.toString() + " " + op + " " + ex2.toString();
    }
}
