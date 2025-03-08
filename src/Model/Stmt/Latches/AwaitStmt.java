package Model.Stmt.Latches;

import Exceptions.StatementException;
import Model.PrgState;
import Model.Stmt.IStmt;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;
import Utils.ADT.MyIDictionary;

public class AwaitStmt implements IStmt {
    private String variableName;

    public AwaitStmt(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeTable) throws StatementException {
        try {
            Type variableType = typeTable.lookUp(variableName);
            if (variableType == null)
                throw new StatementException(String.format("Variable %s has not been declared!", variableName));
            if (!variableType.equals(new IntType()))
                throw new StatementException(String.format("Variable %s should be of integer type!", variableName));

        } catch (StatementException e) {
            throw new StatementException(e.getMessage());
        }

        return typeTable;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        try {
            Value variableValue = state.getSymTable().lookUp(variableName);
            if (variableValue == null)
                throw new StatementException(String.format("Variable %s has not been declared!", variableName));
            if (!variableValue.getType().equals(new IntType()))
                throw new StatementException(String.format("Variable %s should be of integer type!", variableName));

            Integer latchLocation = ((IntValue) variableValue).getValue();
            Integer latchValue = state.getLatchTable().get(latchLocation);

            if (latchValue == null)
                throw new StatementException("Invalid latch table location!");
            if (latchValue != 0)
                state.getExeStack().push(this);

        } catch (StatementException e) {
            throw new StatementException(e.getMessage());
        }

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new AwaitStmt(variableName);
    }

    @Override
    public String toString() {
        return String.format("await(%s)", variableName);
    }
}
