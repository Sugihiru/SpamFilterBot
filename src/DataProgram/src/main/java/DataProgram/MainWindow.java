package DataProgram;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainWindow extends Application {

    private String openExplorer()
    {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        return selectedFile.getAbsolutePath();
    }

    @Override
    public void start(Stage stage) throws IOException {

        String path = openExplorer();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/JudgmentRequestedWindow.fxml"));
        AnchorPane textJudgmentRequestedFXML = fxmlLoader.load();
        JudgmentRequestedWindowController textJudgmentRequestedController = fxmlLoader.getController();
        textJudgmentRequestedController.init(path);
        Scene scene = new Scene(textJudgmentRequestedFXML, 1000, 700);
        stage.setMinHeight(400);
        stage.setMinWidth(700);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            textJudgmentRequestedController.close();
            Platform.exit();
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
