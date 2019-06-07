package dataGenerator;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class LoadFileController {

    private SceneController sceneController;
    private JudgmentRequestedController judgmentRequestedController;

    @FXML
    TextField nameGeneratedFile;

    void initSceneController(SceneController sceneController)
    {
        this.sceneController = sceneController;
    }

    void giveJudgmentRequestedController(JudgmentRequestedController judgmentRequestedController)
    {
        this.judgmentRequestedController = judgmentRequestedController;
    }

    void init()
    {
        initTextField();
    }

    private void initTextField()
    {
        Pattern pattern = Pattern.compile("[a-z]*");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (pattern.matcher(c.getControlNewText()).matches()) {
                return c ;
            } else {
                return null ;
            }
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        nameGeneratedFile.setTextFormatter(formatter);
    }

    @FXML
    void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String path = selectedFile.getAbsolutePath();
            judgmentRequestedController.setValue(path, nameGeneratedFile.getText());
            sceneController.activate(JudgmentRequestedController.class.getSimpleName());
        }
    }
}
