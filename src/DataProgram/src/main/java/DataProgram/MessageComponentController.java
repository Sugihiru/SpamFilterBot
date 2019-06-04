package DataProgram;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    void initEventRadioButton(RadioButton radioButton, boolean valueRadioButton)
    {
        radioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                value = ((isNowSelected) == valueRadioButton);
            }
        });
    }

    @FXML
    public void initialize() {
    }

    public void init(String message, PrintWriter writer)
    {
        this.writer = writer;
        this.givenText = message;
        this.message.setText(givenText);
        initEventRadioButton(yes, true);
        initEventRadioButton(no, false);
    }

    public void close()
    {
        if (yes.isSelected() || no.isSelected())
        writer.println(givenText + "::" + value);
    }

}
