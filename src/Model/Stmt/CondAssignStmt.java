package Model.Stmt;

import Exceptions.StatementException;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;
import Utils.Containers.MyIExeStack;
import Utils.Containers.MyISymTable;

public class CondAssignStmt implements IStmt{
    private final String key;
    private final Exp exp1;
    private final Exp exp2;
    private final Exp exp3;

    public CondAssignStmt(String key, Exp exp1, Exp exp2, Exp exp3) {
        this.key = key;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        MyISymTable symbolTable = state.getSymTable();
        MyIExeStack exeStack = state.getExeStack();

        if (symbolTable.containsKey(key)) {
            Value value1 = exp1.evaluate(symbolTable, state.getHeap());
            Value value2 = exp2.evaluate(symbolTable, state.getHeap());
            Value value3 = exp3.evaluate(symbolTable, state.getHeap());
            Type typeId = (symbolTable.lookUp(key)).getType();

            if (value2.getType().equals(typeId) && value3.getType().equals(typeId)) {
                if (value1.getType().equals(new BoolType())) {
                    IStmt ifStmt = new IfStmt(exp1, new AssignStmt(key, exp2), new AssignStmt(key, exp3));
                    exeStack.push(ifStmt);
                    state.setExeStack(exeStack);
                } else {
                    throw new StatementException("The Expression of Conditional Assignment Statement has to be of bool type!");
                }
            } else {
                throw new StatementException("Declared Types of variable " + key + " and Types of the assigned Expressions do not match.");
            }
        } else {
            throw new StatementException("The used variable " + key + " was not declared before.");
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        Type typeVar = typeEnv.lookUp(key);
        Type typeExpr2 = exp2.typeCheck(typeEnv);
        Type typeExpr3 = exp3.typeCheck(typeEnv);
        if (typeVar.equals(typeExpr2) && typeVar.equals(typeExpr3)){
            Type typeExpr1 = exp1.typeCheck(typeEnv);
            if (typeExpr1.equals(new BoolType())) {
                return typeEnv;
            }
            else
                throw new StatementException("The Expression of Conditional Assignment Statement has to be of bool type!");
        }
        else
            throw new StatementException("Conditional Assignment Statement: right hand side and left hand side have different types.");
    }

    @Override
    public IStmt deepCopy() {
        return new CondAssignStmt(key, exp1.deepCopy(), exp2.deepCopy(), exp3.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("%s = %s ? %s : %s", key, exp1, exp2, exp3);
    }
}
