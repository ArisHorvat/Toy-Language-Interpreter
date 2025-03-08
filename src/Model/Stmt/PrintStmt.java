package Model.Stmt;

import Exceptions.ExpressionException;
import Exceptions.StatementException;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.Type;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyISymTable;

public class PrintStmt implements IStmt{
    private Exp exp;
    public PrintStmt(Exp exp){
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyISymTable symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();

        try {
            state.getOut().add(this.exp.evaluate(symTable, heap));
        }
        catch(ExpressionException e){
            throw new StatementException(e.getMessage());
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        exp.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString(){
        return "Print(" + exp.toString() + ")";
    }

    @Override
    public IStmt deepCopy(){
        return new PrintStmt(exp.deepCopy());
    }
}
