package Model.Stmt.FileStmt;

import Exceptions.DictException;
import Exceptions.StatementException;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Stmt.IStmt;
import Model.Type.StringType;
import Model.Type.Type;
import Model.Value.StringValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyIFileTable;
import Utils.Containers.MyISymTable;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFile implements IStmt {
    private Exp exp;

    public CloseRFile(Exp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyISymTable symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        MyIFileTable fileTable = state.getFileTable();
        Value value;

        value = exp.evaluate(symTable, heap);
        if(value.getType().equals(new StringType())){
            if(fileTable.containsFile((StringValue)value)){
                try {
                    BufferedReader br = fileTable.getBuffer((StringValue)value);
                    br.close();
                }
                catch (IOException e) {
                    throw new StatementException(e.getMessage());
                }

                try {
                    fileTable.close((StringValue) value);
                }
                catch(DictException e){
                    throw new StatementException(e.getMessage());
                }
            }
            else{
                throw new StatementException("File not found");
            }
        }
        else{
            throw new StatementException("Invalid expression");
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        Type type1 = exp.typeCheck(typeEnv);

        if(type1.equals(new StringType())){
            return typeEnv;
        }
        else
            throw new StatementException("The file needs to be of String type");
    }

    @Override
    public String toString(){
        return "Closing file from " + exp.toString();
    }

    @Override
    public IStmt deepCopy(){
        return new CloseRFile(exp.deepCopy());
    }
}
