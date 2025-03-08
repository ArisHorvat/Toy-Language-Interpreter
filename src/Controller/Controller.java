package Controller;

import Exceptions.StatementException;
import Model.PrgState;
import Model.Stmt.IStmt;
import Model.Type.Type;
import Model.Value.RefValue;
import Model.Value.Value;
import Repository.IRepo;
import Exceptions.MyException;
import Utils.ADT.MyDictionary;
import Utils.ADT.MyIDictionary;
import Utils.Containers.MyIExeStack;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private IRepo repo;
    private ExecutorService executor;

    public Controller(IRepo repo){
        this.repo = repo;
        this.executor = Executors.newFixedThreadPool(2);
    }

    public void addProgram(IStmt statement){
        MyIDictionary<String, Type> typeTable = new MyDictionary<>();
        try {
            statement.typeCheck(typeTable);
        }
        catch(StatementException e){
            System.out.println(e.getMessage());
        }
        repo.add(new PrgState(statement));
    }

    public void allStep() throws MyException, InterruptedException {
        removeCompletedPrg();
        while(this.repo.getPrgList().size() > 0){
            conservativeGarbageCollector(this.repo.getPrgList());
            try {
                oneStepForAllPrg();
            }
            catch (InterruptedException | MyException e){
                System.out.println("Interrupted");
                removeCompletedPrg();
                break;
            }
            removeCompletedPrg();
        }
        executor.shutdownNow();

    }

    public void oneStepForAllPrg() throws MyException, InterruptedException{
        this.removeCompletedPrg();

        this.repo.getPrgList().forEach(prg -> repo.logPrgStateExec(prg));

        if(this.repo.getPrgList().isEmpty())
            this.executor.shutdownNow();
        else {
            List<Callable<PrgState>> callList = this.repo.getPrgList().stream()
                    .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                    .collect(Collectors.toList());

            List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (ExecutionException | InterruptedException e) {
                            throw new MyException(e.getMessage());
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            newPrgList.forEach(e -> this.repo.add(e));
            this.conservativeGarbageCollector(this.repo.getPrgList());

            this.repo.getPrgList().forEach(prg -> repo.logPrgStateExec(prg));
        }
    }

    public void removeCompletedPrg(){
        this.repo.setPrgList(this.repo.getPrgList().stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList()));
    }

    public List<Integer> getAddresses(Collection<Value> tableVals){
        return tableVals.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue vAux = (RefValue) v; return vAux.getAddress();})
                .collect(Collectors.toList());
    }

    public void conservativeGarbageCollector(List<PrgState> programStates){
        List<Integer> symTableAddr = Objects.requireNonNull(programStates.stream()
                .map(p -> getAddresses(p.getSymTable().getContent().values())))
                .map(Collection::stream)
                .reduce(Stream::concat)
                .orElse(null)
                .collect(Collectors.toList());

        programStates.forEach(
                p-> {
                    p.getHeap().setContent(this.safeGarbageCollector(symTableAddr,
                            this.getAddresses(p.getHeap().getContent().values()),
                                    p.getHeap().getContent()));
                }
        );
    }

    Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapAddr, Map<Integer, Value> heap){
        return heap.entrySet().stream()
                .filter(e -> (symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public IRepo getRepo(){
        return repo;
    }

    public void setPrgStates(List<PrgState> prgStates){
        repo.setPrgList(prgStates);
    }

    public void executeOneStep(PrgState prgState) throws StatementException{
        MyIExeStack exeStack = prgState.getExeStack();

        if(exeStack.isEmpty()){
            throw new StatementException("Exe Stack Error: Execution stack is empty.");
        }

        IStmt currentStmt = exeStack.pop();
        currentStmt.execute(prgState);

        System.out.println(prgState + "\n");
        repo.logPrgStateExec(prgState);
    }
}
