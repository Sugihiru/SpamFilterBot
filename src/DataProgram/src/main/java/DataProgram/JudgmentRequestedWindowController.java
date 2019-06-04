package DataProgram;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JudgmentRequestedWindowController
{
    @FXML
    public ListView<AnchorPane> list;

    @FXML
    String path;

    @FXML
    ParsingMessagesFromFile parsing;

    private List<MessageComponentController> components;
    private PrintWriter writer;

    @FXML
    public void initialize(){

    }

    void init(String path) throws IOException
    {
        this.writer = new PrintWriter("Data.txt", "UTF-8");
        this.components = new ArrayList<>();
        this.path = path;
        parsing = new ParsingMessagesFromFile(this.path);
        List<String> messages = parsing.getMessages();
        for (String message : messages)
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/MessageComponent.fxml"));
            AnchorPane messageDisplayFXML = fxmlLoader.load();
            MessageComponentController messageDisplayController = fxmlLoader.getController();
            components.add(messageDisplayController);
            messageDisplayController.init(message, writer);
            list.getItems().add(messageDisplayFXML);
        }
    }

    public void close()
    {
        for (MessageComponentController component : components)
        {
            component.close();
        }
        this.writer.close();
    }
}
