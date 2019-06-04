package DataProgram;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JudgmentRequestedWindowController
{
    @FXML
    public Pagination pagination;

    @FXML
    private
    String path;

    @FXML
    private
    ParsingMessagesFromFile parsing;

    private List<MessageComponentController> components;
    private PrintWriter writer;

    private List<String> messages;

    @FXML
    public void initialize(){

    }

    private int itemsPerPage() {
        return 20;
    }

    private void initMessagesPage(int page, VBox vBox) {
        for (int i = page; i < page + itemsPerPage(); i++) {
            if (!(i < messages.size()))
                break;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/MessageComponent.fxml"));
                AnchorPane messageDisplayFXML = fxmlLoader.load();
                MessageComponentController messageDisplayController = fxmlLoader.getController();
                components.add(messageDisplayController);
                messageDisplayController.init(messages.get(i), writer);
                vBox.getChildren().add(messageDisplayFXML);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private javafx.scene.control.ScrollPane createPage(int pageIndex) {
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();
        VBox vbox = new VBox();
        vbox.prefWidthProperty().bind(scrollPane.widthProperty());
        scrollPane.setContent(vbox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToHeight(true);

        int page = pageIndex * itemsPerPage();
        initMessagesPage(page, vbox);
        return scrollPane;
    }

    private void initPagination()
    {
        int nbPage = (int)Math.ceil(this.messages.size() / (double)itemsPerPage());
        pagination.setPageCount(nbPage);
        pagination.setPageFactory(this::createPage);
    }

    void init(String path) throws IOException
    {
        this.writer = new PrintWriter("Data.txt", StandardCharsets.UTF_8);
        this.components = new ArrayList<>();
        this.path = path;
        parsing = new ParsingMessagesFromFile(this.path);
        this.messages = parsing.getMessages();
        initPagination();
    }

    void close()
    {
        for (MessageComponentController component : components)
        {
            component.close();
        }
        this.writer.close();
    }
}
