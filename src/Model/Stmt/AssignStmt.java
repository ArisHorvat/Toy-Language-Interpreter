package Model.Stmt;

import Exceptions.DictException;
import Exceptions.ExpressionException;
import Exceptions.StatementException;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.Type;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.ADT.MyIHeap;
import Utils.Containers.MyISymTable;

public class AssignStmt implements IStmt{
    private String id;
    private Exp exp;

    public AssignStmt(String id, Exp exp){
        this.id = id;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyISymTable symTable = state.getSymTable();
        MyIHeap heap = state.getHeap();
        Value value;

        if(symTable.containsKey(id)){
            try {
                value = exp.evaluate(symTable, heap);
            }
            catch(ExpressionException e){
                throw new StatementException(e.getMessage());
            }
            try {
                Type typeId = (symTable.lookUp(id).getType());
                if (value.getType().equals(typeId)) {
                    symTable.update(id, value);
                }
                else {
                    throw new StatementException("declared type of variable" + id + " and type of the assigned expression" +
                            "do not match");
                }
            }
            catch(DictException e){
                throw new StatementException(e.getMessage());
            }
        }
        else{
            throw new StatementException("the used variable " + id + " was not declared before");
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        Type typeVar = typeEnv.lookUp(id);
        Type typExp = exp.typeCheck(typeEnv);

        if (typeVar.equals(typExp))
            return typeEnv;
        else
            throw new StatementException("Assignment: right hand side and left hand " +
                    "side have different types ");
}

    @Override
    public String toString(){
        return id + "=" + exp.toString();
    }

    @Override
    public IStmt deepCopy(){
        return new AssignStmt(this.id, this.exp.deepCopy());
    }

}
