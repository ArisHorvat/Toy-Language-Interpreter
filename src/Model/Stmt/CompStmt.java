package Model.Stmt;

import Exceptions.StatementException;
import Model.PrgState;
import Model.Type.Type;
import Utils.ADT.MyIDictionary;
import Utils.Containers.MyIExeStack;

public class CompStmt implements IStmt{
    private IStmt first;
    private IStmt second;

    public CompStmt(IStmt first, IStmt second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyIExeStack stack = state.getExeStack();
        stack.push(second);
        stack.push(first);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        return second.typeCheck(first.typeCheck(typeEnv));
    }

    @Override
    public IStmt deepCopy(){
        return new CompStmt(first.deepCopy(), second.deepCopy());
    }

    @Override
    public String toString(){
        return first.toString() + "; " + second.toString();
    }

    public IStmt getFirst(){
        return this.first;
    }

    public IStmt getSecond(){
        return this.second;
    }
}
