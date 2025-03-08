package Utils.Containers;

import Exceptions.StackException;
import Model.Stmt.IStmt;
import Utils.ADT.MyIStack;

import java.util.List;

public interface MyIExeStack extends Iterable<IStmt>{
    void push(IStmt item);
    IStmt pop() throws StackException;
    boolean isEmpty();
    List<IStmt> getReverse();
    List<IStmt> toList();
}
