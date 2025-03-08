package Model.Stmt.FileStmt;

import Exceptions.StatementException;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Stmt.IStmt;
import Model.Type.IntType;
import Model.Type.StringType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyIFileTable;
import Utils.Containers.MyISymTable;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStmt {
    private Exp exp;
    private String varName;

    public ReadFile(Exp exp, String varName){
        this.exp = exp;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyISymTable symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        MyIFileTable fileTable = state.getFileTable();
        Value value;
        Value file;

        value = exp.evaluate(symTable, heap);
        if(fileTable.containsFile((StringValue)value)){
            value = symTable.lookUp(varName);
            if(value.getType().equals(new IntType())){
                file = exp.evaluate(symTable, heap);
                if(file.getType().equals(new StringType())){
                    if(fileTable.containsFile((StringValue)file)){
                        BufferedReader br = fileTable.getBuffer((StringValue)file);
                        try {
                            Value newValue;
                            String line = br.readLine();
                            if(line != null){
                                newValue = new IntValue(Integer.parseInt(line));
                            }
                            else{
                                newValue = new IntValue(0);
                            }
                            symTable.put(varName, newValue);
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        throw new StatementException("File does not exist");
                    }
                }
                else{
                    throw new StatementException("Variable '" + varName + "' not found");
                }
            }
            else{
                throw new StatementException("Variable '" + varName + "' is not of type Int");
            }
        }
        else{
            throw new StatementException("Variable '" + varName + "' not found");
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        Type type1 = exp.typeCheck(typeEnv);
        Type type2 = typeEnv.lookUp(varName);

        if(type1.equals(new StringType())){
            if(type2.equals(new IntType())){
                return typeEnv;
            }
            else
                throw new StatementException("Variable '" + varName + "' is not of type Int");
        }
        else
            throw new StatementException("The file needs to be of String type");

    }

    @Override
    public String toString(){
        return "Store value from file " + exp.toString() + " in " + varName;
    }

    @Override
    public IStmt deepCopy() {
        return new ReadFile(exp.deepCopy(), varName);
    }
}
