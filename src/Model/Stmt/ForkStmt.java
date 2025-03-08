package Model.Stmt;

import Exceptions.StatementException;
import Model.PrgState;
import Model.Type.Type;
import Utils.ADT.MyIDictionary;
import Utils.Containers.MyExeStack;
import Utils.Containers.MyIExeStack;

public class ForkStmt implements IStmt {
    private IStmt stmt;

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state){
        MyIExeStack exeStack = new MyExeStack();
        exeStack.push(stmt);
        return new PrgState(exeStack, state.getSymTable().clone(),
                state.getOut(), state.getFileTable(), state.getHeap(), state.getLatchTable());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        return stmt.typeCheck(typeEnv);
    }

    @Override
    public String toString() {
        return "fork(" + stmt + ")";
    }

    @Override
    public IStmt deepCopy(){
        return new ForkStmt(stmt.deepCopy());
    }
}
