package Model;


import Exceptions.MyException;
import Exceptions.StackException;
import Model.Stmt.CompStmt;
import Model.Stmt.IStmt;
import Model.Stmt.NopStmt;
import Model.Value.StringValue;
import Model.Value.Value;
import Utils.ADT.*;
import Utils.Containers.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PrgState {
    private MyIExeStack exeStack;
    private MyISymTable symTable;
    private final MyIOutput out;
    private final MyIFileTable fileTable;
    private final MyIHeap heap;
    private MyILatchTable latchTable;
    private int id = 0;
    private static int finalId = 0;

    public PrgState(MyIExeStack stack, MyISymTable table, MyIOutput output,
                    MyIFileTable fTable, MyIHeap heapInput, MyILatchTable latchTableInput) {
        exeStack = stack;
        symTable = table;
        out = output;
        fileTable = fTable;
        heap = heapInput;
        latchTable = latchTableInput;
        id = setId();
    }

    public PrgState(IStmt originalProgram) {
        exeStack = new MyExeStack();
        symTable = new MySymTable();
        out = new MyOutput();
        fileTable = new MyFileTable();
        heap = new MyHeap();
        latchTable = new MyLatchTable();
        id = setId();

        exeStack.push(originalProgram);
    }

    public MyIExeStack getExeStack(){
        return exeStack;
    }

    public MyISymTable getSymTable(){
        return symTable;
    }

    public MyIOutput getOut(){
        return out;
    }

    public MyIFileTable getFileTable(){
        return fileTable;
    }

    public MyIHeap getHeap(){
        return heap;
    }

    public MyILatchTable getLatchTable(){
        return latchTable;
    }

    public int getId(){
        return id;
    }

    public synchronized int setId() {
        finalId++;
        return finalId;
    }

    public void setSymTable(MyISymTable symbolTable) {
        this.symTable = symbolTable;
    }

    public void setExeStack(MyIExeStack exeStack) {
        this.exeStack = exeStack;
    }

    public void setLatchTable(MyILatchTable latchTable) {
        this.latchTable = latchTable;
    }

    public String exeStackString(){
        StringBuilder exeStackString = new StringBuilder();

        for(IStmt stmt: distinctStatements()){
            if(!(stmt instanceof NopStmt))
                exeStackString.append(stmt).append("\n");
        }
        return exeStackString.toString();
    }

    public List<IStmt> distinctStatements() {
        MyTree<IStmt> tree =  new MyTree<IStmt>();
        List<IStmt> inOrderList=new LinkedList<IStmt>();
        if(getExeStack().toList().size()>0) {
            tree.setRoot(toTree( (IStmt)getExeStack().toList().get(0)));
            tree.inorderTraversal(inOrderList, tree.getRoot());
        }
        return inOrderList;
    }

    private Node<IStmt> toTree(IStmt stmt) {
        Node<IStmt> node;
        if (stmt instanceof CompStmt){
            CompStmt comptStmt = (CompStmt) stmt;
            node = new Node<>(new NopStmt());
            node.setLeft(new Node<>(comptStmt.getFirst()));
            node.setRight(toTree( comptStmt.getSecond()));
        }
        else {
            node = new Node<>(stmt);
        }
        return node;

    }

    public String symTableString(){
        StringBuilder symTableString = new StringBuilder();

        for(String key: symTable.keys()){
            symTableString.append(key).append(" --> ").append(symTable.lookUp(key)).append("\n");
        }

        return symTableString.toString();
    }

    public String outString(){
        StringBuilder outString = new StringBuilder();

        for(Value val: out){
            outString.append(val.toString()).append("\n");
        }

        return outString.toString();
    }

    public String fileTableString(){
        StringBuilder fileTableString = new StringBuilder();
        for(StringValue files: fileTable.getFiles()){
            fileTableString.append(files.toString()).append("\n");
        }
        return fileTableString.toString();
    }

    public String heapString(){
        StringBuilder heapString = new StringBuilder();
        Map<Integer, Value> map = heap.getContent();
        for(Integer address: map.keySet()){
            heapString.append(address).append(" --> ").append(map.get(address)).append("\n");
        }
        return heapString.toString();
    }

    public String latchTableString(){
        StringBuilder latchString = new StringBuilder();
        Map<Integer, Integer> map = latchTable.getContent();
        for(Integer address: map.keySet()){
            latchString.append(address).append(" --> ").append(map.get(address)).append("\n");
        }
        return latchString.toString();
    }

    public boolean isNotCompleted(){
        return !this.exeStack.isEmpty();
    }

    public PrgState oneStep() throws MyException {
        if(exeStack.isEmpty()){
            throw new MyException("Stack is empty");
        }

        IStmt stmt;
        stmt = exeStack.pop();
        return stmt.execute(this);
    }

    @Override
    public String toString(){
        return  "Program ID = " + String.valueOf(id)
                + "\n--Execution stack--\n" + exeStackString()
                +" \n--Symbol table--\n" + symTableString()
                + "\n--Output--\n" + outString()
                + "\n--File table--\n" + fileTableString()
                + "\n--Heap--\n" + heapString()
                + "\n--Latch table--\n" + latchTableString();
    }
}
