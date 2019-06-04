package DataProgram;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

import java.io.PrintWriter;

public class MessageComponentController {
    private boolean value = true;
    private PrintWriter writer;
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
                value = ((isNowSelected) == valueRadioButton));
    }

    @FXML
    public void initialize() {
    }

    void init(String message, PrintWriter writer) {
        this.writer = writer;
        this.givenText = message;
        this.message.setText(message);
        initEventRadioButton(yes, true);
        initEventRadioButton(no, false);
    }

    void close()
    {
        if (writer != null) {
            if (yes.isSelected() || no.isSelected())
                writer.println(givenText + "::" + value);
        }
    }

}
