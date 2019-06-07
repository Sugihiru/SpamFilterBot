package dataGenerator;

import dataGenerator.dataStorage.DataBase;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;

import java.sql.SQLException;

public class MessageComponentController {
    private DataBase dataBase;
    private String givenText;

    @FXML
    public TextArea message;

    @FXML
    public RadioButton yes;

    @FXML
    public RadioButton no;

    @FXML
    public ToggleGroup YesNo;

    private void initToggleGroup()
    {
        YesNo.selectedToggleProperty().addListener((obs, wasPreviouslySelected, isNowSelected) ->
        {
            if (YesNo.getSelectedToggle() != null) {
                dataBase.write(givenText, (boolean)YesNo.getSelectedToggle().getUserData());
            }
        });
    }

    @FXML
    public void initialize() {
    }

    private void checkYesNoDB()
    {
        try {
            if (this.dataBase.messageAlreadyExists(this.givenText))
            {
                Boolean value = this.dataBase.getValue(this.givenText);

                if (value == null) {
                    return;
                }
                else if (value)
                    yes.setSelected(true);
                else
                    no.setSelected(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void init(String message, DataBase dataBase) {
        this.dataBase = dataBase;
        this.givenText = message;
        this.message.setText(message);
        checkYesNoDB();
        yes.setUserData(true);
        no.setUserData(false);
        initToggleGroup();
    }
}
