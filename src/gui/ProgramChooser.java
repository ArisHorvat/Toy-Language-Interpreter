package gui;

import Controller.Controller;
import Exceptions.ExpressionException;
import Exceptions.StatementException;
import Model.Expression.*;
import Model.Stmt.*;
import Model.Stmt.FileStmt.CloseRFile;
import Model.Stmt.FileStmt.OpenRFile;
import Model.Stmt.FileStmt.ReadFile;
import Model.Stmt.HeapStmt.NewH;
import Model.Stmt.HeapStmt.WriteH;
import Model.Stmt.Latches.AwaitStmt;
import Model.Stmt.Latches.CountDownStmt;
import Model.Stmt.Latches.NewLatchStmt;
import Model.Type.*;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Repository.IRepo;
import Repository.Repo;
import Utils.ADT.MyDictionary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import javafx.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ProgramChooser {

    private ProgramExecutor programExecutor;

    @FXML
    private ListView<IStmt> programsList;

    @FXML
    private Button showButton;

    public void setProgramExecutor(ProgramExecutor programExecutor) {
        this.programExecutor = programExecutor;
    }

    @FXML
    public void initialize() {
        programsList.setItems(getStatementsList());
        programsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    public ObservableList<IStmt> getStatementsList() {
        List<IStmt> statements = new ArrayList<IStmt>();

        //int v; v=2; Print(v)  is represented as:
        IStmt ex1 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v")))
        );
        statements.add(ex1);

        //int a; int b; a = 2 + 3 *5; b = a + 1; Print(b)
        IStmt ex2 = new CompStmt(
                new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntValue(2)),
                                new ArithExp('*', new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b",
                                        new ArithExp('+', new VarExp("a"), new ValueExp(new IntValue(1)))),
                                        new PrintStmt(new VarExp("b")))))
        );
        statements.add(ex2);

        //bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)   is represented as
        IStmt ex3 =  new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new
                                        IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new
                                        VarExp("v"))))));
        statements.add(ex3);

        //string varf; varf="test.in"; openRFile(varf); int varc;
        //readFile(varf, varc);print(varc); readFile(varf, varc);print(varc); closeRFile(varf)
        IStmt ex4 =  new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("PrgState.txt"))),
                        new CompStmt(new OpenRFile(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(new ReadFile(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new ReadFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFile(new VarExp("varf")))))))))
        );
        statements.add(ex4);

        //int a; int b; a=8; b=3; if(a>b) then print(a) else print(b)
        IStmt ex5 = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(8))),
                                new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(3))),
                                        new IfStmt(new RelationExp(">", new VarExp("a"), new VarExp("b")),
                                                new PrintStmt(new VarExp("a")),
                                                new PrintStmt(new VarExp("b")))))));
        statements.add(ex5);

        // int a; a=4; (while(a>0) print(a); a=a-1); print(a)
        IStmt ex6 = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(4))),
                        new CompStmt(
                                new WhileStmt
                                        (new RelationExp(">", new VarExp("a"), new ValueExp(new IntValue(0))),
                                                new CompStmt(new PrintStmt(new VarExp("a")),
                                                        new AssignStmt("a",
                                                                new ArithExp('-', new VarExp("a"),
                                                                        new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("a")))));
        statements.add(ex6);

        // Ref int v; new(v, 20); Ref Ref int a; new(a, v); print(v); print(a)
        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewH("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewH("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")),
                                                new PrintStmt(new VarExp("a")))))));
        statements.add(ex7);

        // Ref int v; new(v, 20); Ref Ref int a; new(a, v); print(rH(v)); print(rH(rH(a))+5)
        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewH("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewH("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new ReadHExp(new VarExp("v"))),
                                                new PrintStmt(
                                                        new ArithExp('+',
                                                                new ReadHExp(new ReadHExp(new VarExp("a"))),
                                                                new ValueExp(new IntValue(5)))))))));
        statements.add(ex8);

        // Ref int v; new(v, 20); print(rH(v)); wh(v, 30); print(rh(v)+5);
        IStmt ex9 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewH("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new PrintStmt(new ReadHExp(new VarExp("v"))),
                                new CompStmt(new WriteH("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ArithExp('+',
                                                new ReadHExp(new VarExp("v")),
                                                new ValueExp(new IntValue(5))))))));
        statements.add(ex9);

        // Ref int v; new(v, 20); Ref Ref int a; new(a, v); new(v, 30); print(rH(rH(a)))
        IStmt ex10 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewH("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewH("a", new VarExp("v")),
                                        new CompStmt(new NewH("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(new ReadHExp(new ReadHExp(new VarExp("a")))))))));
        statements.add(ex10);

        // int v; Ref int a; v=10; new(a, 22); fork(wH(a, 30); v=32; print(v); print(rH(a))); print(v); print(rH(a))
        IStmt ex11 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(new NewH("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(
                                                new ForkStmt(new CompStmt(new WriteH("a", new ValueExp(new IntValue(30))),
                                                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                                new CompStmt(new PrintStmt(new VarExp("v")),
                                                                        new PrintStmt(new ReadHExp(new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")),
                                                        new PrintStmt(new ReadHExp(new VarExp("a")))))))));
        statements.add(ex11);

        IStmt ex12 = new CompStmt(
                        new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(
                                new VarDeclStmt("b", new RefType(new IntType())),
                                new CompStmt(
                                        new VarDeclStmt("v", new IntType()),
                                        new CompStmt(
                                                new NewH("a", new ValueExp(new IntValue(0))),
                                                new CompStmt(
                                                        new NewH("b", new ValueExp(new IntValue(0))),
                                                        new CompStmt(
                                                                new WriteH("a", new ValueExp(new IntValue(1))),
                                                                new CompStmt(
                                                                        new WriteH("b", new ValueExp(new IntValue(2))),
                                                                        new CompStmt(
                                                                                new CondAssignStmt(
                                                                                        "v",
                                                                                        new RelationExp(
                                                                                                "<",
                                                                                                new ReadHExp(new VarExp("a")),
                                                                                                new ReadHExp(new VarExp("b"))
                                                                                        ),
                                                                                        new ValueExp(new IntValue(100)),
                                                                                        new ValueExp(new IntValue(200))
                                                                                ),
                                                                                new CompStmt(
                                                                                        new PrintStmt(new VarExp("v")),
                                                                                        new CompStmt(
                                                                                                new CondAssignStmt(
                                                                                                        "v",
                                                                                                        new RelationExp(
                                                                                                                ">",
                                                                                                                new ArithExp(
                                                                                                                        '-',
                                                                                                                        new ReadHExp(new VarExp("b")),
                                                                                                                        new ValueExp(new IntValue(2))
                                                                                                                ),
                                                                                                                new ReadHExp(new VarExp("a"))
                                                                                                        ),
                                                                                                        new ValueExp(new IntValue(100)),
                                                                                                        new ValueExp(new IntValue(200))
                                                                                                ),
                                                                                                new PrintStmt(new VarExp("v"))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
        statements.add(ex12);

        //Ref int v1; Ref int v2; Ref int v3; int cnt;
        //new(v1,2);new(v2,3);new(v3,4);newLatch(cnt,rH(v2));
        //fork(
        //      wh(v1,rh(v1)*10));print(rh(v1));countDown(cnt);
        //      fork(
        //          wh(v2,rh(v2)*10));print(rh(v2));countDown(cnt);
        //          fork(
        //              wh(v3,rh(v3)*10));print(rh(v3));countDown(cnt))
        //              )
        //           );
        //await(cnt);
        //print(100);
        //countDown(cnt);
        //print(100)
        IStmt ex13 = new CompStmt(
                new VarDeclStmt("v1", new RefType(new IntType())),
                new CompStmt(
                        new VarDeclStmt("v2", new RefType(new IntType())),
                        new CompStmt(
                                new VarDeclStmt("v3", new RefType(new IntType())),
                                new CompStmt(
                                        new VarDeclStmt("cnt", new IntType()),
                                        new CompStmt(
                                                new NewH("v1", new ValueExp(new IntValue(2))),
                                                new CompStmt(
                                                        new NewH("v2", new ValueExp(new IntValue(3))),
                                                        new CompStmt(
                                                                new NewH("v3", new ValueExp(new IntValue(4))),
                                                                new CompStmt(
                                                                        new NewLatchStmt("cnt", new ReadHExp(new VarExp("v2"))),
                                                                        new CompStmt(
                                                                                new ForkStmt(
                                                                                        new CompStmt(
                                                                                                new WriteH("v1", new ArithExp('*', new ReadHExp(new VarExp("v1")), new ValueExp(new IntValue(10)))),
                                                                                                new CompStmt(
                                                                                                        new PrintStmt(new ReadHExp(new VarExp("v1"))),
                                                                                                            new CompStmt(new CountDownStmt("cnt"),
                                                                                                                            new ForkStmt(
                                                                                                                                    new CompStmt(
                                                                                                                                            new WriteH("v2", new ArithExp('*', new ReadHExp(new VarExp("v2")), new ValueExp(new IntValue(10)))),
                                                                                                                                            new CompStmt(
                                                                                                                                                    new PrintStmt(new ReadHExp(new VarExp("v2"))),
                                                                                                                                                    new CompStmt(new CountDownStmt("cnt"),
                                                                                                                                                            new ForkStmt(
                                                                                                                                                                    new CompStmt(
                                                                                                                                                                            new WriteH("v3", new ArithExp('*', new ReadHExp(new VarExp("v3")), new ValueExp(new IntValue(10)))),
                                                                                                                                                                            new CompStmt(
                                                                                                                                                                                    new PrintStmt(new ReadHExp(new VarExp("v3"))),
                                                                                                                                                                                    new CountDownStmt("cnt")
                                                                                                                                                                            )
                                                                                                                                                                    )
                                                                                                                                                            )
                                                                                                                                                    )
                                                                                                                                            )
                                                                                                                                    )
                                                                                                                            )
                                                                                                            )
                                                                                                )
                                                                                        )
                                                                                ),
                                                                                                new CompStmt(
                                                                                                        new AwaitStmt("cnt"),
                                                                                                        new CompStmt(
                                                                                                                new PrintStmt(new ValueExp(new IntValue(100))),
                                                                                                                new CompStmt(
                                                                                                                        new CountDownStmt("cnt"),
                                                                                                                        new PrintStmt(new ValueExp(new IntValue(100)))
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )

                                                                )
                                                        )
                                                )
                                        )
                                )
                        );
        statements.add(ex13);


        return FXCollections.observableArrayList(statements);
    }

    @FXML
    void showProgram(ActionEvent event) {
        IStmt selectedStmt = programsList.getSelectionModel().getSelectedItem();
        if(selectedStmt != null) {
            int index = programsList.getSelectionModel().getSelectedIndex();
            try{
                selectedStmt.typeCheck(new MyDictionary<String, Type>());
                IRepo repo = new Repo("log" + (index + 1) + ".txt");
                Controller ctrl = new Controller(repo);
                ctrl.addProgram(selectedStmt);
                programExecutor.setController(ctrl);
            }
            catch(StatementException | ExpressionException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No statement selected");
            alert.showAndWait();
        }
    }
}
