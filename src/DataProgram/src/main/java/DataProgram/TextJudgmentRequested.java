package DataProgram;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class TextJudgmentRequested
{
    @FXML
    public ListView<AnchorPane> list;

    @FXML
    String path;

    @FXML
    ParsingMessageFromFile parsing;

    @FXML
    public void initialize(){

    }

    void init(String path) throws IOException
    {
        this.path = path;
        parsing = new ParsingMessageFromFile(this.path);
        List<String> messages = parsing.getMessages();
        for (String message : messages)
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/MessageDisplay.fxml"));
            AnchorPane messageDisplayFXML = fxmlLoader.load();
            MessageDisplay messageDisplayController = fxmlLoader.getController();
            messageDisplayController.init(message);
            list.getItems().add(messageDisplayFXML);
        }
    }
}
