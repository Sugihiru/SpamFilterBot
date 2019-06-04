package DataProgram;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import java.sql.Connection;

public class MessageComponentController {
    private boolean value = true;
    private Connection connection;
    private String givenText;

    @FXML
    public TextArea message;

    @FXML
    public RadioButton yes;

    @FXML
    public RadioButton no;

    private void initEventRadioButton(RadioButton radioButton, boolean valueRadioButton)
    {
        radioButton.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) ->
                {
                    value = ((isNowSelected) == valueRadioButton);
                    SaveDataDB.write(connection, givenText, value);
                });
    }

    @FXML
    public void initialize() {
    }

    void init(String message, Connection connection) {
        this.connection = connection;
        this.givenText = message;
        this.message.setText(message);
        initEventRadioButton(yes, true);
        initEventRadioButton(no, false);
    }

    void close()
    {
        /*if (writer != null) {
            if (yes.isSelected() || no.isSelected())
                writer.println(givenText + "::" + value);
        }*/
    }

}
