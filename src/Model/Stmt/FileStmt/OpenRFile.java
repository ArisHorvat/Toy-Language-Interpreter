package Model.Stmt.FileStmt;

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
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenRFile implements IStmt {
    private Exp exp;

    public OpenRFile(Exp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyISymTable symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        MyIFileTable fileTable = state.getFileTable();
        Value value;
        value = exp.evaluate(symTable, heap);

        //static StringType
        if(value.getType().equals(new StringType())){
            if(!fileTable.containsFile((StringValue)value)){
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(((StringValue) value).getValue()));
                    fileTable.open((StringValue) value, br);
                }
                catch (FileNotFoundException e) {
                    throw new StatementException(e.getMessage());
                }
            }
            else{
                throw new StatementException("File already exists");
            }
        }
        else{
            throw new StatementException("Invalid type of expression");
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
        return "Open file from " + exp.toString();
    }

    @Override
    public IStmt deepCopy(){
        return new OpenRFile(this.exp.deepCopy());
    }
}
