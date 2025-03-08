package Model.Stmt;

import Exceptions.ExpressionException;
import Exceptions.StatementException;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyIExeStack;
import Utils.Containers.MyISymTable;

public class IfStmt implements IStmt{
    private Exp exp;
    private IStmt thenS;
    private IStmt elseS;

    public IfStmt(Exp exp, IStmt thenS, IStmt elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyIExeStack stack = state.getExeStack();
        MyISymTable symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        Value value;

        try {
            value = exp.evaluate(symTable, heap);
        }
        catch(ExpressionException e) {
            throw new StatementException(e.getMessage());
        }

        if(value.getType().equals(new BoolType())){
            if(((BoolValue)value).getValue())
                stack.push(thenS);
            else
                stack.push(elseS);
            return null;
        }
        else{
            throw new StatementException("Conditional Expression is not a boolean");
        }
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        Type typExp = exp.typeCheck(typeEnv);

        if(typExp.equals(new BoolType())) {
            thenS.typeCheck(typeEnv.copy()); // clone
            elseS.typeCheck(typeEnv.copy()); // clone
            return typeEnv;
        }
        else
            throw new StatementException("The condition of IF has not the type bool");
    }

    @Override
    public String toString(){
        return "IF(" + exp.toString() + ") " + "THEN(" + thenS.toString() + ") ELSE(" + elseS.toString() + ")";
    }

    @Override
    public IStmt deepCopy(){
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }
}
