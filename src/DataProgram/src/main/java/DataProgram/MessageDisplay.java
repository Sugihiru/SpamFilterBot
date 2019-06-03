package DataProgram;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

public class MessageDisplay {
    private String givenText;
    public TextArea message;
    public RadioButton yes;
    public RadioButton no;

    void initEventRadioButton(RadioButton radioButton)
    {
        radioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
                if (isNowSelected) {
                    // ...
                } else {
                    // ...
                }
            }
        });
    }

    @FXML
    public void initialize() {
    }

    public void init(String message)
    {
        this.givenText = message;
        this.message.setText(givenText);
        initEventRadioButton(yes);
        initEventRadioButton(no);
    }

}
