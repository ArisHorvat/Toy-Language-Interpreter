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
import Utils.Containers.MyISymTable;

public class WhileStmt implements IStmt{
    private Exp exp;
    private IStmt stmt;

    public WhileStmt(Exp exp, IStmt stmt) {
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyISymTable symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();

        Value expValue;
        try{
            expValue = exp.evaluate(symTable, heap);
        }
        catch(ExpressionException e){
            throw new StatementException(e.getMessage());
        }

        if(!expValue.getType().equals(new BoolType())){
            throw new StatementException("Value is not of type Bool");
        }

        if(((BoolValue)expValue).getValue()){
            state.getExeStack().push(this);
            state.getExeStack().push(stmt);
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        Type type1 = exp.typeCheck(typeEnv);

        if(type1.equals(new BoolType())){
            stmt.typeCheck(typeEnv.copy());
            return typeEnv;
        }
        else{
            throw new StatementException("Value is not of type Bool");
        }

    }

    @Override
    public IStmt deepCopy(){
        return new WhileStmt(exp.deepCopy(), stmt.deepCopy());
    }

    @Override
    public String toString() {
        return "while(" + exp.toString() + ") " + stmt.toString();
    }
}
