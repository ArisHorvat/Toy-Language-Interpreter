package gui;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Program Chooser Loader
        FXMLLoader programChooserLoader = new FXMLLoader();
        programChooserLoader.setLocation(MainWindow.class.getResource("resources/ProgramChooser.fxml"));

        Parent rootChooser = programChooserLoader.load();
        Scene sceneChooser = new Scene(rootChooser, 500, 500);

        ProgramChooser programChooserController = programChooserLoader.getController();
        primaryStage.setTitle("Select a program");
        primaryStage.setScene(sceneChooser);
        primaryStage.show();

        //Program Executor Loader
        FXMLLoader programExecutorLoader = new FXMLLoader();
        programExecutorLoader.setLocation(MainWindow.class.getResource("resources/ProgramExecutor.fxml"));

        Parent rootExecutor = programExecutorLoader.load();
        Scene sceneExecutor = new Scene(rootExecutor);

        ProgramExecutor programExecutorController = programExecutorLoader.getController();
        programChooserController.setProgramExecutor(programExecutorController);

        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Toy Language Interpreter");
        secondaryStage.setScene(sceneExecutor);
        secondaryStage.show();
    }
}
