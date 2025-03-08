package Utils.Containers;

import Exceptions.StackException;
import Model.Stmt.IStmt;
import Utils.ADT.MyIStack;
import Utils.ADT.MyStack;

import java.util.*;

import static java.util.Collections.reverse;

public class MyExeStack implements MyIExeStack{
    private MyStack<IStmt> stack;

    public MyExeStack(){
        stack = new MyStack<IStmt>();
    }

    @Override
    public void push(IStmt item) {
        stack.push(item);
    }

    @Override
    public IStmt pop() throws StackException {
        if(stack.isEmpty())
            throw new StackException("Stack is empty");
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public List<IStmt> getReverse() {
        return stack.getReverse();
    }

    @Override
    public List<IStmt> toList() {
        return  stack.toList();
    }

    public Iterator<IStmt> iterator(){
        return this.getReverse().iterator();
    }

    @Override
    public String toString(){
        return stack.toString();
    }
}
