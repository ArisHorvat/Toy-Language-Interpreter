package Model.Stmt;

import Exceptions.StatementException;
import Model.PrgState;
import Model.Type.Type;
import Utils.ADT.MyIDictionary;

public interface IStmt {
    PrgState execute(PrgState state) throws StatementException;
    MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException;
    IStmt deepCopy();
}
