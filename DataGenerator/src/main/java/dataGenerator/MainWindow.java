package dataGenerator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {

    private SceneController sceneController;

    private Parent textJudgmentRequestedFXML;
    private JudgmentRequestedController textJudgmentRequestedController;

    private Parent loadFileFXML;
    private LoadFileController loadFileController;

    private void initJudgmentRequested() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/JudgmentRequested.fxml"));
        this.textJudgmentRequestedFXML = fxmlLoader.load();
        this.textJudgmentRequestedController = fxmlLoader.getController();
        this.textJudgmentRequestedController.init();
    }

    private void initLoadFile() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/LoadFile.fxml"));
        this.loadFileFXML  = fxmlLoader.load();
        this.loadFileController = fxmlLoader.getController();
        this.loadFileController.giveJudgmentRequestedController(this.textJudgmentRequestedController);
        this.loadFileController.init();
    }

    @Override
    public void start(Stage stage) throws IOException {

        initJudgmentRequested();
        initLoadFile();
        Scene scene = new Scene(this.loadFileFXML, 700, 400);
        this.sceneController = new SceneController(scene);
        sceneController.add(LoadFileController.class.getSimpleName(), this.loadFileFXML);
        sceneController.add(JudgmentRequestedController.class.getSimpleName(), this.textJudgmentRequestedFXML);
        this.loadFileController.initSceneController(sceneController);
        this.textJudgmentRequestedController.initSceneController(sceneController);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            this.textJudgmentRequestedController.close();
            Platform.exit();
        });
        stage.setMinWidth(700);
        stage.setMinHeight(400);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
