package DataProgram;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
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

    public int itemsPerPage() {
        return 20;
    }

    public void initMessagePage(int page, VBox vBox) {
        for (int i = page; i < page + itemsPerPage(); i++) {
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

    public javafx.scene.control.ScrollPane createPage(int pageIndex) {
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();
        VBox vbox = new VBox();
        vbox.prefWidthProperty().bind(scrollPane.widthProperty());
        scrollPane.setContent(vbox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToHeight(true);

        int page = pageIndex * itemsPerPage();
        initMessagePage(page, vbox);
        return scrollPane;
    }

    private void initPagination()
    {
        int nbPage = (int)Math.ceil(this.messages.size() / (double)itemsPerPage());
        System.out.println(this.messages.size());
        System.out.println(itemsPerPage());
        System.out.println(nbPage);
        pagination.setPageCount(nbPage);
        pagination.setPageFactory(new Callback<Integer, javafx.scene.Node>() {
            @Override
            public javafx.scene.Node call(Integer pageIndex)  {
                return createPage(pageIndex);
            }
        });
    }

    void init(String path) throws IOException
    {
        this.writer = new PrintWriter("Data.txt", Charset.forName("ISO-8859-1"));
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
