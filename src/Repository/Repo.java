package Repository;

import Exceptions.MyException;
import Model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repo implements IRepo{
    private List<PrgState> states;
    private String logFilePath;
    private final int currentPosition;

    public Repo(String logFilePath) {
        states = new ArrayList<>();
        this.logFilePath = logFilePath;
        currentPosition = 0;

        try {
            FileWriter writer = new FileWriter(this.logFilePath, false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(PrgState state){
        states.add(state);
    }

    @Override
    public void logPrgStateExec(PrgState currentProgram) throws MyException {
        try {
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
            logFile.println(currentProgram);
            logFile.close();
        } catch (IOException e) {
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public List<PrgState> getPrgList(){
        return states;
    }

    @Override
    public void setPrgList(List<PrgState> prgList){
        states = prgList;
    }

    @Override
    public PrgState getCurrentPrgState(){
        return states.get(currentPosition);
    }
}
