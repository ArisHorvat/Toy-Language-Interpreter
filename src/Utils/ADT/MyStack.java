package Utils.ADT;

import Exceptions.StackException;

import java.util.*;

import static java.util.Collections.reverse;

public class MyStack<T> implements MyIStack<T> {
    private Stack<T> stack;

    public MyStack(){
        stack = new Stack<>();
    }

    @Override
    public void push(T item){
        stack.push(item);
    }

    @Override
    public T pop() throws StackException {
        if(stack.isEmpty())
            throw new StackException("Stack is empty");
        return stack.pop();
    }

    @Override
    public boolean isEmpty(){
        return stack.isEmpty();
    }

    @Override
    public List<T> getReverse(){
        List<T> reversedList = new ArrayList<>(stack);
        reverse(reversedList);
        return reversedList;
    }

    public List<T> toList() {
        return  (List<T>)Arrays.asList(stack.toArray());
    }

    @Override
    public Iterator<T> iterator(){
        return this.getReverse().iterator();
    }

    @Override
    public String toString(){
        return stack.toString();
    }
}
