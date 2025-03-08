package Model.Stmt.Latches;

import Exceptions.StatementException;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Stmt.IStmt;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;

public class NewLatchStmt implements IStmt {
    private String variableName;
    private Exp expression;

    public NewLatchStmt(String variableName, Exp expression){
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        Value expressionValue = expression.evaluate(state.getSymTable(), state.getHeap());
        if (!expressionValue.getType().equals(new IntType()))
            throw new StatementException(String.format("Expression '%s' should have evaluate to an integer", expression.toString()));

        int latchValue = ((IntValue) expressionValue).getValue();
        int latchLocation = state.getLatchTable().put(latchValue);

        Value variableValue = state.getSymTable().lookUp(variableName);
        if (variableValue == null)
            throw new StatementException(String.format("Variable '%s' has not been declared", variableName));
        if (!variableValue.getType().equals(new IntType()))
            throw new StatementException(String.format("Variable '%s' should be of integer type", variableName));

        state.getSymTable().update(variableName, new IntValue(latchLocation));

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws StatementException {
        Type variableType = typeEnv.lookUp(variableName);
        if (variableName == null)
            throw new StatementException(String.format("Variable '%s' has not been declared", variableName));
        if (!variableType.equals(new IntType()))
            throw new StatementException(String.format("Variable '%s' should be of integer type", variableName));

        Type expressionType = expression.typeCheck(typeEnv);
        if (!expressionType.equals(new IntType()))
            throw new StatementException(String.format("Expression '%s' should be of integer type", expressionType));

        return typeEnv;
    }

    @Override
    public IStmt deepCopy() {
        return new NewLatchStmt(variableName, expression);
    }

    @Override
    public String toString() {
        return String.format("newLatch(%s, %s)", variableName, expression.toString());
    }
}
