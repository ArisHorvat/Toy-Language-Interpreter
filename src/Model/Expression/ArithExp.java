package Model.Expression;

import Exceptions.ExpressionException;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyIFileTable;
import Utils.Containers.MyISymTable;

import java.io.BufferedReader;

public class ArithExp implements Exp {
    private Exp e1;
    private Exp e2;
    private Character op;

    public ArithExp(Character op, Exp e1, Exp e2){
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public Value evaluate(MyISymTable symTable, MyIHeap heap) throws ExpressionException{
        Value v1, v2;
        v1 = e1.evaluate(symTable, heap);
        if(v1.getType().equals(new IntType())){
            v2 = e2.evaluate(symTable, heap);
            if(v2.getType().equals(new IntType())){
                IntValue i1 = (IntValue)v1;
                IntValue i2 = (IntValue)v2;
                int n1, n2;
                n1 = i1.getValue();
                n2 = i2.getValue();
                if(op == '+'){
                    return new IntValue(n1 + n2);
                }
                else if (op == '-') {
                    return new IntValue(n1 - n2);
                }
                else if (op == '*'){
                    return new IntValue(n1 * n2);
                }
                else if (op == '/'){
                    if(n2 == 0)
                        throw new ExpressionException("division by zero");
                    return new IntValue(n1 / n2);
                }
            }
            else{
                throw new ExpressionException("second operand is not an integer");
            }
        }
        else{
            throw new ExpressionException("first operand is not an integer");
        }
        throw new ExpressionException("Invalid code");
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException{
        Type typ1, typ2;
        typ1 = e1.typeCheck(typeEnv);
        typ2 = e2.typeCheck(typeEnv);

        if(typ1.equals(new IntType())){
            if(typ2.equals(new IntType())){
                return new IntType();
            }
            else
                throw new ExpressionException("second operand is not an integer");
        }
        else
            throw new ExpressionException("first operand is not an integer");
    }

    @Override
    public String toString(){
        return "(" + e1.toString() + op + e2.toString() + ")";
    }

    @Override
    public Exp deepCopy(){
        return new ArithExp(this.op, this.e1.deepCopy(), this.e2.deepCopy());
    }
}
