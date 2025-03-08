package Model.Stmt.HeapStmt;

import Exceptions.ExpressionException;
import Exceptions.HeapException;
import Exceptions.StatementException;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Stmt.IStmt;
import Model.Type.RefType;
import Model.Type.Type;
import Model.Value.RefValue;
import Model.Value.StringValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyISymTable;

public class NewH implements IStmt {
    private String varName;
    private Exp exp;

    public NewH(String varName, Exp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException{
        MyISymTable symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();

        if(!symTable.containsKey(varName)) {
            throw new StatementException("Variable '" + varName + "' not found");
        }

        Value varValue = symTable.lookUp(varName);
        if(!(varValue.getType() instanceof RefType)){
            throw new StatementException("Variable '" + varName + "' is not a reference type");
        }

        Value expValue;
        try {
            expValue = exp.evaluate(symTable, heap);
        }
        catch(ExpressionException e) {
            throw new StatementException(e.getMessage());
        }
        Type varType = ((RefValue)varValue).getLocationType();

        if(!varType.equals(expValue.getType())){
            throw new StatementException("Variable '" + varName + "' is not a reference type");
        }

        int oldAddress = ((RefValue) varValue).getAddress();
        if(oldAddress != 0)
            heap.delete(oldAddress);

        Integer newAddress = heap.insert(expValue);
        symTable.update(varName, new RefValue(newAddress, varType));

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        Type typeVar = typeEnv.lookUp(varName);
        Type typExp = exp.typeCheck(typeEnv);

        if (typeVar.equals(new RefType(typExp)))
            return typeEnv;
        else
            throw new StatementException("NEW stmt: right hand side and " +
                    "left hand side have different types ");
    }

    @Override
    public IStmt deepCopy(){
        return new NewH(varName, exp.deepCopy());
    }

    @Override
    public String toString(){
        return "New(" + varName + ", " + exp + ")";
    }
}
