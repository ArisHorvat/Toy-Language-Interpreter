package Model.Stmt.HeapStmt;

import Exceptions.ExpressionException;
import Exceptions.StatementException;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Stmt.IStmt;
import Model.Type.RefType;
import Model.Type.Type;
import Model.Value.RefValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyISymTable;

public class WriteH implements IStmt {
    private String varName;
    private Exp exp;

    public WriteH(String varName, Exp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyISymTable symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();

        if(!symTable.containsKey(varName)) {
            throw new StatementException("Variable '" + varName + "' not found");
        }

        Value varValue = symTable.lookUp(varName);
        if(!(varValue.getType() instanceof RefType)) {
            throw new StatementException("Variable '" + varName + "' is not a Reference type");
        }

        if(!heap.containsAddress(((RefValue)varValue).getAddress())){
            throw new StatementException("The value from '" + varName + "' is not an address in the Heap");
        }

        Value expValue;
        try {
            expValue = exp.evaluate(symTable, heap);
        }
        catch(ExpressionException e){
            throw new StatementException(e.getMessage());
        }

        Type varType = ((RefValue)varValue).getLocationType();
        if(!(varType.equals(expValue.getType()))) {
            throw new StatementException("Variable '" + varName + "' is not of type " + expValue.getType());
        }

        heap.update(((RefValue)varValue).getAddress(), expValue);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        if(typeEnv.lookUp(varName).equals(new RefType(exp.typeCheck(typeEnv)))) {
            return typeEnv;
        }
        else
            throw new StatementException("Variable '" + varName + "' is not a Reference type");
    }

    @Override
    public IStmt deepCopy(){
        return new WriteH(varName, exp.deepCopy());
    }

    @Override
    public String toString(){
        return "WriteHeap(" + varName + ", " + exp.toString() + ")";
    }
}
