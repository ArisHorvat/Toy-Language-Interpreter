package gui;

import Model.PrgState;
import Model.Stmt.IStmt;
import Model.Value.StringValue;
import Model.Value.Value;
import Utils.ADT.MyIHeap;
import Utils.ADT.MyILatchTable;
import Utils.Containers.MyIExeStack;
import Utils.Containers.MyIFileTable;
import Utils.Containers.MyIOutput;
import Utils.Containers.MyISymTable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import Controller.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProgramExecutor {
    private Controller ctrl;

    @FXML
    private TableColumn<Pair<Integer, Value>, Integer> addressColumn;

    @FXML
    private ListView<String> exeStackView;

    @FXML
    private ListView<String> fileTableView;

    @FXML
    private TableView<Pair<Integer, Value>> heapView;

    @FXML
    private TextField noOfPrgStatesText;

    @FXML
    private ListView<String> outputView;

    @FXML
    private ListView<Integer> prgStatesIdentifiersView;

    @FXML
    private Button runOneStepButton;

    @FXML
    private TableView<Pair<String, Value>> symbolTableView;

    @FXML
    private TableColumn<Pair<Integer, Value>, String> valueColumn;

    @FXML
    private TableColumn<Pair<String, Value>, String> varNameColumn;

    @FXML
    private TableColumn<Pair<String, Value>, String> varValueColumn;

    @FXML
    private TableColumn<Pair<Integer, Integer>, String> latchLocationColumn;

    @FXML
    private TableView<Pair<Integer, Integer>> latchTableView;

    @FXML
    private TableColumn<Pair<Integer, Integer>, String> latchValueColumn;


    @FXML
    void changeProgramState(MouseEvent event) {
        populateExeStackView();
        populateSymbolTableView();
    }

    @FXML
    void runOneStep(ActionEvent event) {
        if(ctrl != null) {
            try{
                prgStatesIdentifiersView.getItems().clear();
                noOfPrgStatesText.setText("0");

                ctrl.oneStepForAllPrg();

                if(!ctrl.getRepo().getPrgList().isEmpty()) {
                    populate();
                }
                else{
                    runOneStepButton.setDisable(true);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Execution Info");
                    alert.setHeaderText("Program Status");
                    alert.setContentText("The program has reached its end point");
                    alert.showAndWait();
                }
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Execution error");
                alert.setHeaderText("An error occurred");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText("No program selected!");
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        prgStatesIdentifiersView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
        varNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        varValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        latchLocationColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey().toString()));
        latchValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));
    }

    public void setController(Controller ctrl){
        this.ctrl = ctrl;
        this.runOneStepButton.setDisable(false);
        populate();
    }

    public PrgState getCurrentPrgState(){
        if(ctrl.getRepo().getPrgList().size() == 0)
            return null;
        else{
            int id = prgStatesIdentifiersView.getSelectionModel().getSelectedIndex();
            if(id == -1)
                return ctrl.getRepo().getPrgList().get(0);
            else
                return ctrl.getRepo().getPrgList().get(id);
        }
    }

    public void populate(){
        populateNrOfPrgStatesText();
        populateHeapTableView();
        populateOutputView();
        populateFileTableView();
        populatePrgStateIdentifiersView();
        populateSymbolTableView();
        populateExeStackView();
        populateLatchTable();
    }

    private void populateNrOfPrgStatesText(){
        List<PrgState> prgStates = this.ctrl.getRepo().getPrgList();
        noOfPrgStatesText.setText(String.valueOf(prgStates.size()));

        if(prgStates.isEmpty())
            runOneStepButton.setDisable(true);
    }

    private void populateHeapTableView(){
        PrgState prgState = getCurrentPrgState();
        MyIHeap heap = prgState.getHeap();
        ArrayList<Pair<Integer, Value>> heapEntries = new ArrayList<>();
        for(Map.Entry<Integer, Value> entry: heap.getContent().entrySet()){
            heapEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        heapView.setItems(FXCollections.observableArrayList(heapEntries));
    }

    private void populateOutputView(){
        PrgState prgState = getCurrentPrgState();
        MyIOutput output = prgState.getOut();
        List<String> outputEntries = new ArrayList<>();
        for(int index = 0; index < output.size(); index++){
            outputEntries.add(output.get(index).toString());
        }
        outputView.setItems(FXCollections.observableArrayList(outputEntries));
    }

    private void populateFileTableView(){
        PrgState prgState = getCurrentPrgState();
        MyIFileTable fileTable = prgState.getFileTable();
        List<String> fileEntries = new ArrayList<>();
        for(StringValue files: fileTable.getFiles()){
            fileEntries.add(files.getValue());
        }
        fileTableView.setItems(FXCollections.observableArrayList(fileEntries));
    }

    private void populatePrgStateIdentifiersView(){
        List<PrgState> prgStates = this.ctrl.getRepo().getPrgList();
        List<Integer> ids = new ArrayList<>();
        for(PrgState prgState: prgStates){
            ids.add(prgState.getId());
        }
        prgStatesIdentifiersView.setItems(FXCollections.observableArrayList(ids));
        populateNrOfPrgStatesText();
    }

    private void populateSymbolTableView(){
        PrgState prgState = getCurrentPrgState();
        MyISymTable symTable = prgState.getSymTable();
        ArrayList<Pair<String, Value>> symTableEntries = new ArrayList<>();
        for(Map.Entry<String, Value> entry: symTable.getContent().entrySet()){
            symTableEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        symbolTableView.setItems(FXCollections.observableArrayList(symTableEntries));
    }

    private void populateExeStackView(){
        PrgState prgState = getCurrentPrgState();
        MyIExeStack exeStack = prgState.getExeStack();
        List<String> exeStackEntries = new ArrayList<>();
        if(prgState != null){
            for(IStmt stmt: exeStack.getReverse()){
                exeStackEntries.add(stmt.toString());
            }
        }
        exeStackView.setItems(FXCollections.observableArrayList(exeStackEntries));
    }

    private void populateLatchTable(){
        PrgState programState = getCurrentPrgState();
        MyILatchTable latchTable = Objects.requireNonNull(programState).getLatchTable();
        ArrayList<Pair<Integer, Integer>> lockTableEntries = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry: latchTable.getContent().entrySet()) {
            lockTableEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        latchTableView.setItems(FXCollections.observableArrayList(lockTableEntries));
    }

}
