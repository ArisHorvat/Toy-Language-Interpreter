package Model.Stmt;

import Exceptions.StatementException;
import Model.PrgState;
import Model.Type.Type;
import Utils.ADT.MyIDictionary;

public class NopStmt implements IStmt {
    public NopStmt() {}
    @Override
    public PrgState execute(PrgState state) throws StatementException {
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "NopStatement";
    }

    @Override
    public IStmt deepCopy(){
        return new NopStmt();
    }
}
