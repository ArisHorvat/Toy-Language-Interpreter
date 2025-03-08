package Repository;

import Exceptions.MyException;
import Model.PrgState;
import java.util.List;

public interface IRepo {
    void add(PrgState state);
    void logPrgStateExec(PrgState currentProgram) throws MyException;
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);
    PrgState getCurrentPrgState();
}
