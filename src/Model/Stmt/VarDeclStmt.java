package Model.Stmt;

import Exceptions.StatementException;
import Model.Type.Type;
import Model.PrgState;
import Utils.ADT.MyIDictionary;
import Utils.Containers.MyISymTable;

public class VarDeclStmt implements IStmt{
    private String name;
    private Type type;

    public VarDeclStmt(String name, Type type){
        this.name = name;
        this.type = type;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyISymTable symTable = state.getSymTable();
        if(symTable.containsKey(name)){
            throw new StatementException("Variable " + this.name + " already exists");
        }
        symTable.put(name, type.defaultValue());
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        typeEnv.put(name, type);
        return typeEnv;
    }

    @Override
    public String toString(){
        return type.toString() + " " + name;
    }

    @Override
    public IStmt deepCopy(){
        return new VarDeclStmt(name, type.deepCopy());
    }
}
