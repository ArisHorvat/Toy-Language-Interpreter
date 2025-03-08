package View;

import Model.Stmt.FileStmt.CloseRFile;
import Model.Stmt.FileStmt.OpenRFile;
import Model.Stmt.FileStmt.ReadFile;
import Model.Stmt.HeapStmt.NewH;
import Model.Stmt.HeapStmt.WriteH;
import Repository.*;
import Controller.*;
import Model.Expression.*;
import Model.Stmt.*;
import Model.Type.*;
import Model.Value.*;
import Model.PrgState;
import Utils.ADT.*;
import Utils.Containers.*;
import View.Command.*;

import java.io.BufferedReader;

class Interpreter {

    public static void main(String[] args) {

        //int v; v=2; Print(v)  is represented as:
        IStmt ex1 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v")))
        );

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

        //bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)   is represented as
        IStmt ex3 =  new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new
                                        IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new
                                        VarExp("v"))))));

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

        //int a; int b; a=8; b=3; if(a>b) then print(a) else print(b)
        IStmt ex5 = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(8))),
                                new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(3))),
                                        new IfStmt(new RelationExp(">", new VarExp("a"), new VarExp("b")),
                                                new PrintStmt(new VarExp("a")),
                                                        new PrintStmt(new VarExp("b")))))));

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

        // Ref int v; new(v, 20); Ref Ref int a; new(a, v); print(v); print(a)
        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewH("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewH("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")),
                                                new PrintStmt(new VarExp("a")))))));

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

        // Ref int v; new(v, 20); print(rH(v)); wh(v, 30); print(rh(v)+5);
        IStmt ex9 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewH("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new PrintStmt(new ReadHExp(new VarExp("v"))),
                                new CompStmt(new WriteH("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ArithExp('+',
                                                new ReadHExp(new VarExp("v")),
                                                new ValueExp(new IntValue(5))))))));

        // Ref int v; new(v, 20); Ref Ref int a; new(a, v); new(v, 30); print(rH(rH(a)))
        IStmt ex10 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewH("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewH("a", new VarExp("v")),
                                        new CompStmt(new NewH("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(new ReadHExp(new ReadHExp(new VarExp("a")))))))));

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


        IRepo repo1 = new Repo("log1.txt");
        Controller ctrl1 = new Controller(repo1);
        ctrl1.addProgram(ex1);


        IRepo repo2 = new Repo("log2.txt");
        Controller ctrl2 = new Controller(repo2);
        ctrl2.addProgram(ex2);


        IRepo repo3 = new Repo("log3.txt");
        Controller ctrl3 = new Controller(repo3);
        ctrl3.addProgram(ex3);


        IRepo repo4 = new Repo("log4.txt");
        Controller ctrl4 = new Controller(repo4);
        ctrl4.addProgram(ex4);


        IRepo repo5 = new Repo("log5.txt");
        Controller ctrl5 = new Controller(repo5);
        ctrl5.addProgram(ex5);


        IRepo repo6 = new Repo("log6.txt");
        Controller ctrl6 = new Controller(repo6);
        ctrl6.addProgram(ex6);


        IRepo repo7 = new Repo("log7.txt");
        Controller ctrl7 = new Controller(repo7);
        ctrl7.addProgram(ex7);


        IRepo repo8 = new Repo("log8.txt");
        Controller ctrl8 = new Controller(repo8);
        ctrl8.addProgram(ex8);


        IRepo repo9 = new Repo("log9.txt");
        Controller ctrl9 = new Controller(repo9);
        ctrl9.addProgram(ex9);


        IRepo repo10 = new Repo("log10.txt");
        Controller ctrl10 = new Controller(repo10);
        ctrl10.addProgram(ex10);

        IRepo repo11 = new Repo("log11.txt");
        Controller ctrl11 = new Controller(repo11);
        ctrl11.addProgram(ex11);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExCommand("1", ex1.toString(), ctrl1));
        menu.addCommand(new RunExCommand("2", ex2.toString(), ctrl2));
        menu.addCommand(new RunExCommand("3", ex3.toString(), ctrl3));
        menu.addCommand(new RunExCommand("4", ex4.toString(), ctrl4));
        menu.addCommand(new RunExCommand("5", ex5.toString(), ctrl5));
        menu.addCommand(new RunExCommand("6", ex6.toString(), ctrl6));
        menu.addCommand(new RunExCommand("7", ex7.toString(), ctrl7));
        menu.addCommand(new RunExCommand("8", ex8.toString(), ctrl8));
        menu.addCommand(new RunExCommand("9", ex9.toString(), ctrl9));
        menu.addCommand(new RunExCommand("10", ex10.toString(), ctrl10));
        menu.addCommand(new RunExCommand("11", ex11.toString(), ctrl11));
        menu.show();
    }
}